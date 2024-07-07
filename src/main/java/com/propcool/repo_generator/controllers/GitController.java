package com.propcool.repo_generator.controllers;

import com.propcool.repo_generator.dto.UpdateLocalDto;
import com.propcool.repo_generator.dto.UpdateTargetDto;
import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.services.TargetGitService;
import com.propcool.repo_generator.utils.Remote;
import com.propcool.repo_generator.dto.Violation;
import com.propcool.repo_generator.dto.ViolationResponse;
import com.propcool.repo_generator.validation.annotations.ExistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST-контроллер для взаимодействия с приложением
 * */
@Tag(name = "Git контроллер", description = "Контроллер для переноса репозиториев с одного облачного сервиса на другой")
@RestController
@RequestMapping("/git")
@Validated
@RequiredArgsConstructor
public class GitController {
    private final SourceGitService sourceGitService;

    private final TargetGitService targetGitService;

    @Operation(summary = "Получение всех репозитории с сервиса источника")
    @GetMapping("/repositories")
    public List<String> repositories(@RequestParam(name = "service") @ExistService @Valid String service) {
        return sourceGitService.getAllRepositories(Remote.getByRemoteName(service));
    }

    @Operation(summary = "Обновление локального репозитория")
    @PostMapping("/update/local")
    public void updateLocalRepository(@RequestBody @Valid UpdateLocalDto dto) {
        sourceGitService.updateRepository(dto.repoName(), Remote.getByRemoteName(dto.service()));
    }

    @Operation(summary = "Обновление всех локальных репозиториев")
    @PostMapping("/update-all/local")
    public void updateAllLocalRepositories(@RequestParam(name = "service") @ExistService @Valid String service) {
        sourceGitService.updateAllRepositories(Remote.getByRemoteName(service));
    }

    @Operation(summary = "Обновление репозитория в целевом сервисе")
    @PostMapping("/update/cloud")
    public void updateCloudRepository(@RequestBody @Valid UpdateTargetDto dto) {
        targetGitService.updateRepository(dto.repoName(), Remote.getByRemoteName(dto.service()));
    }

    @Operation(summary = "Обновление всех репозиториев в целевом сервисе")
    @PostMapping("/update-all/cloud")
    public void updateAllCloudRepositories(@RequestParam(name = "service") @ExistService @Valid String service) {
        targetGitService.updateAllRepositories(Remote.getByRemoteName(service));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationResponse onConstraintValidationException(ConstraintViolationException e) {
        List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString().replaceAll(".+\\.", ""),
                        violation.getMessage()
                        )
                ).toList();
        return new ViolationResponse(violations);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViolationResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Violation> fieldViolations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage())).toList();
        List<Violation> globalViolations = e.getBindingResult().getGlobalErrors().stream()
                .map(error -> new Violation("#global", error.getDefaultMessage())).toList();
        List<Violation> violations = new ArrayList<>();
        violations.addAll(fieldViolations);
        violations.addAll(globalViolations);
        return new ViolationResponse(violations);
    }
}
