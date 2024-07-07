package com.propcool.repo_generator.validation.annotations;

import com.propcool.repo_generator.validation.validators.ExistSourceRepositoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistSourceRepositoryValidator.class)
public @interface ExistSourceRepository {
    String message() default "Отсутствует репозиторий с данным именем в сервисе-источнике.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
