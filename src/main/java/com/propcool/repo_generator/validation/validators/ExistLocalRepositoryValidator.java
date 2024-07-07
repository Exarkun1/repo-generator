package com.propcool.repo_generator.validation.validators;

import com.propcool.repo_generator.dto.UpdateTargetDto;
import com.propcool.repo_generator.services.TargetGitService;
import com.propcool.repo_generator.utils.Remote;
import com.propcool.repo_generator.validation.annotations.ExistLocalRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistLocalRepositoryValidator implements ConstraintValidator<ExistLocalRepository, UpdateTargetDto> {
    private final TargetGitService targetGitService;

    @Override
    public boolean isValid(UpdateTargetDto dto, ConstraintValidatorContext context) {
        return !Remote.containsByRemoteName(dto.service())
                || targetGitService.containsLocalRepository(dto.repoName());
    }
}
