package com.propcool.repo_generator.dto;

import com.propcool.repo_generator.validation.annotations.ExistLocalRepository;
import com.propcool.repo_generator.validation.annotations.ExistService;

@ExistLocalRepository
public record UpdateTargetDto(
        @ExistService
        String service,
        String repoName
) {}
