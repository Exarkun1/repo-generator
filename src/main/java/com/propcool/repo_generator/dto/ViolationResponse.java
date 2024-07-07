package com.propcool.repo_generator.dto;

import java.util.List;

public record ViolationResponse(
        List<Violation> violations
) {}
