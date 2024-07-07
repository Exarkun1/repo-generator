package com.propcool.repo_generator.validation.validators;

import com.propcool.repo_generator.dto.UpdateLocalDto;
import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.utils.Remote;
import com.propcool.repo_generator.validation.annotations.ExistSourceRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistSourceRepositoryValidator implements ConstraintValidator<ExistSourceRepository, UpdateLocalDto> {
    private final SourceGitService sourceGitService;

    @Override
    public boolean isValid(UpdateLocalDto dto, ConstraintValidatorContext context) {
        return !Remote.containsByRemoteName(dto.service())
                || sourceGitService.containsRemoteRepository(Remote.getByRemoteName(dto.service()), dto.repoName());
    }
}
