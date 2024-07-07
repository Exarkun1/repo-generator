package com.propcool.repo_generator.dto;

import com.propcool.repo_generator.validation.annotations.ExistService;
import com.propcool.repo_generator.validation.annotations.ExistSourceRepository;

@ExistSourceRepository
public record UpdateLocalDto(
        @ExistService
        String service,
        String repoName
) {}
