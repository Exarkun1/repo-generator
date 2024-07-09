package com.propcool.repo_generator.controllers;

import com.propcool.repo_generator.dto.ErrorResponse;
import com.propcool.repo_generator.dto.UpdateDto;
import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.services.TargetGitService;
import com.propcool.repo_generator.utils.Remote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

/**
 * REST-контроллер для взаимодействия с приложением
 * */
@Tag(name = "Git контроллер", description = "Контроллер для переноса репозиториев с одного облачного сервиса на другой")
@RestController
@RequestMapping("/git")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GitController {
    private final SourceGitService sourceGitService;

    private final TargetGitService targetGitService;

    @Operation(summary = "Получение всех репозитории из сервиса источника")
    @GetMapping("/repos/cloud")
    public List<String> cloudRepositories(@RequestParam(name = "service") String service) {
        throwIfServiceIsNotExist(service);
        return sourceGitService.getAllRemoteRepositories(Remote.getByRemoteName(service));
    }

    @Operation(summary = "Обновление локального репозитория")
    @PostMapping("/update/local")
    public void updateLocalRepository(@RequestBody UpdateDto dto) {
        throwIfServiceIsNotExist(dto.service());
        Remote remote = Remote.getByRemoteName(dto.service());
        throwIfSourceRepoIsNotExist(remote, dto.repoName());
        sourceGitService.updateLocalRepository(dto.repoName(), remote);
    }

    @Operation(summary = "Обновление всех локальных репозиториев")
    @PostMapping("/update-all/local")
    public void updateAllLocalRepositories(@RequestParam(name = "service") String service) {
        throwIfServiceIsNotExist(service);
        sourceGitService.updateAllLocalRepositories(Remote.getByRemoteName(service));
    }

    @Operation(summary = "Получение всех репозитории из локального хранилища")
    @GetMapping("/repos/local")
    public List<String> localRepositories() {
        return targetGitService.getAllLocalRepositories();
    }

    @Operation(summary = "Обновление репозитория в целевом сервисе")
    @PostMapping("/update/cloud")
    public void updateCloudRepository(@RequestBody UpdateDto dto) {
        throwIfServiceIsNotExist(dto.service());
        throwIfLocalRepoNotExist(dto.repoName());
        targetGitService.updateRemoteRepository(dto.repoName(), Remote.getByRemoteName(dto.service()));
    }

    @Operation(summary = "Обновление всех репозиториев в целевом сервисе")
    @PostMapping("/update-all/cloud")
    public void updateAllCloudRepositories(@RequestParam(name = "service") String service) {
        throwIfServiceIsNotExist(service);
        targetGitService.updateAllRemoteRepositories(Remote.getByRemoteName(service));
    }

    @Operation(summary = "Получение всех облачных сервисов для хранения репозиториев")
    @GetMapping("/services")
    public List<String> services() {
        return Remote.getAllNames();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notFoundHandler(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    private void throwIfServiceIsNotExist(String service){
        if(!Remote.containsByRemoteName(service)) throw new NotFoundException("Сервис с данным именем не найден.");
    }

    private void throwIfSourceRepoIsNotExist(Remote remote, String repoName) {
        if(!sourceGitService.containsRemoteRepository(remote, repoName)) throw new NotFoundException("Репозиторий с данным именем в сервисе-источнике не найден.");
    }

    private void throwIfLocalRepoNotExist(String repoName){
        if(!targetGitService.containsLocalRepository(repoName)) throw new NotFoundException("Репозиторий с данным именем в локальном хранилище не найден.");
    }
}
