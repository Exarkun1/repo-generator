package com.propcool.repo_generator.controllers;

import com.propcool.repo_generator.services.SourceGitService;
import com.propcool.repo_generator.services.TargetGitService;
import com.propcool.repo_generator.utils.Remote;
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
        return sourceGitService.getAllRepositories(Remote.GITHUB);
    }

    @PostMapping("/update/{repoName}/local")
    public void updateLocalRepository(@PathVariable("repoName") String repoName) {
        sourceGitService.updateRepository(repoName, Remote.GITHUB);
    }

    @PostMapping("/update-all/local")
    public void updateAllLocalRepositories() {
        sourceGitService.updateAllRepositories(Remote.GITHUB);
    }

    @PostMapping("/update/{repoName}/cloud")
    public void updateCloudRepository(@PathVariable("repoName") String repoName) {
        targetGitService.updateRepository(repoName, Remote.BITBUCKET);
    }

    @PostMapping("/update-all/cloud")
    public void updateAllCloudRepositories() {
        targetGitService.updateAllRepositories(Remote.BITBUCKET);
    }
}
