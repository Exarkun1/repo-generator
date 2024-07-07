package com.propcool.repo_generator.controllers;

import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.services.TargetGitService;
import com.propcool.repo_generator.utils.Remote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public List<String> repositories(@RequestParam(name = "service") String serviceName) {
        return sourceGitService.getAllRepositories(Remote.getByRemoteName(serviceName));
    }

    @Operation(summary = "Обновление локального репозитория")
    @PostMapping("/update/{repoName}/local")
    public void updateLocalRepository(@PathVariable("repoName") String repoName, @RequestParam(name = "service") String serviceName) {
        sourceGitService.updateRepository(repoName, Remote.getByRemoteName(serviceName));
    }

    @Operation(summary = "Обновление всех локальных репозиториев")
    @PostMapping("/update-all/local")
    public void updateAllLocalRepositories(@RequestParam(name = "service") String serviceName) {
        sourceGitService.updateAllRepositories(Remote.getByRemoteName(serviceName));
    }

    @Operation(summary = "Обновление репозитория в целевом сервисе")
    @PostMapping("/update/{repoName}/cloud")
    public void updateCloudRepository(@PathVariable("repoName") String repoName, @RequestParam(name = "service") String serviceName) {
        targetGitService.updateRepository(repoName, Remote.getByRemoteName(serviceName));
    }

    @Operation(summary = "Обновление всех репозиториев в целевом сервисе")
    @PostMapping("/update-all/cloud")
    public void updateAllCloudRepositories(@RequestParam(name = "service") String serviceName) {
        targetGitService.updateAllRepositories(Remote.getByRemoteName(serviceName));
    }
}
