package com.propcool.repo_generator.services;

import com.propcool.repo_generator.api.GitApi;
import com.propcool.repo_generator.utils.GitTable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Сервис для взаимодействия с облачным сервисом источником
 * */
@Service
@RequiredArgsConstructor
public class SourceGitService {
    public List<String> getAllRepositories(String serviceName) {
        return gitTable.get(serviceName).getAllCloudRepositories();
    }

    public void updateRepository(String repoName, String serviceName) {
        File repoPath = new File(directory + "/" + repoName);
        GitApi gitApi = gitTable.get(serviceName);
        if(!repoPath.exists()) {
            gitApi.cloneRepository(repoName, repoPath);
        } else {
            gitApi.pullRepository(repoPath);
        }
    }

    public void updateAllRepositories(String serviceName) {
        List<String> repositories = getAllRepositories(serviceName);
        for(var repoName : repositories) {
            updateRepository(repoName, serviceName);
        }
    }

    @Value("${directory}")
    private String directory;

    private final GitTable gitTable;
}
