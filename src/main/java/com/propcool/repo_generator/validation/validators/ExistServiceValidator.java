package com.propcool.repo_generator.validation.validators;

import com.propcool.repo_generator.utils.Remote;
import com.propcool.repo_generator.validation.annotations.ExistService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistServiceValidator implements ConstraintValidator<ExistService, String> {
    @Override
    public boolean isValid(String remoteName, ConstraintValidatorContext context) {
        return Remote.containsByRemoteName(remoteName);
    }
}
