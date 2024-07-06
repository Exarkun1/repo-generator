package com.propcool.repo_generator.controllers;

import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.services.TargetGitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для взаимодействия с приложением
 * */
@RestController
@RequestMapping("/git")
@RequiredArgsConstructor
public class GitController {
    private final SourceGitService sourceGitService;

    private final TargetGitService targetGitService;

    @GetMapping("/repositories")
    public List<String> repositories() {
        return sourceGitService.getAllRepositories("github");
    }

    @PostMapping("/update/{repoName}/local")
    public void updateLocalRepository(@PathVariable("repoName") String repoName) {
        sourceGitService.updateRepository(repoName, "github");
    }

    @PostMapping("/update-all/local")
    public void updateAllLocalRepositories() {
        sourceGitService.updateAllRepositories("github");
    }

    @PostMapping("/update/{repoName}/cloud")
    public void updateCloudRepository(@PathVariable("repoName") String repoName) {
        targetGitService.updateRepository(repoName, "bitbucket");
    }

    @PostMapping("/update-all/cloud")
    public void updateAllCloudRepositories() {
        targetGitService.updateAllRepositories("bitbucket");
    }
}
