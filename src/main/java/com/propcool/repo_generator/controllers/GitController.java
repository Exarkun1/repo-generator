package com.propcool.repo_generator.controllers;

import com.propcool.repo_generator.dto.UpdateLocalDto;
import com.propcool.repo_generator.dto.UpdateTargetDto;
import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.services.TargetGitService;
import com.propcool.repo_generator.utils.Remote;
import com.propcool.repo_generator.validation.annotations.ExistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

/**
 * REST-контроллер для взаимодействия с приложением
 * */
@Tag(name = "Git контроллер", description = "Контроллер для переноса репозиториев с одного облачного сервиса на другой")
@RestController
@RequestMapping("/git")
@RequiredArgsConstructor
public class GitController {
    private final SourceGitService sourceGitService;

    private final TargetGitService targetGitService;

    @Operation(summary = "Получение всех репозитории с сервиса источника")
    @GetMapping("/repositories")
    public List<String> repositories(@RequestParam(name = "service") @ExistService @Valid String serviceName) {
        return sourceGitService.getAllRepositories(Remote.getByRemoteName(serviceName));
    }

    @Operation(summary = "Обновление локального репозитория")
    @PostMapping("/update/local")
    public void updateLocalRepository(@RequestBody @Valid UpdateLocalDto dto) {
        sourceGitService.updateRepository(dto.repoName(), Remote.getByRemoteName(dto.service()));
    }

    @Operation(summary = "Обновление всех локальных репозиториев")
    @PostMapping("/update-all/local")
    public void updateAllLocalRepositories(@RequestParam(name = "service") @ExistService @Valid String serviceName) {
        sourceGitService.updateAllRepositories(Remote.getByRemoteName(serviceName));
    }

    @Operation(summary = "Обновление репозитория в целевом сервисе")
    @PostMapping("/update/cloud")
    public void updateCloudRepository(@RequestBody @Valid UpdateTargetDto dto) {
        targetGitService.updateRepository(dto.repoName(), Remote.getByRemoteName(dto.service()));
    }

    @Operation(summary = "Обновление всех репозиториев в целевом сервисе")
    @PostMapping("/update-all/cloud")
    public void updateAllCloudRepositories(@RequestParam(name = "service") @ExistService @Valid String serviceName) {
        targetGitService.updateAllRepositories(Remote.getByRemoteName(serviceName));
    }
}
