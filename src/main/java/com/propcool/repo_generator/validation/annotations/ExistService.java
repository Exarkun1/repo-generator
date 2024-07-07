package com.propcool.repo_generator.validation.annotations;

import com.propcool.repo_generator.validation.validators.ExistServiceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistServiceValidator.class)
public @interface ExistService {
    String message() default "Сервиса не существует.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
