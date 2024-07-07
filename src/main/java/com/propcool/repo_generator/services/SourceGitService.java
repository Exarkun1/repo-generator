package com.propcool.repo_generator.services;

import com.propcool.repo_generator.api.GitApi;
import com.propcool.repo_generator.utils.Remote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Сервис для взаимодействия с облачным сервисом источником
 * */
@Service
public class SourceGitService {
    @Value("${directory}")
    private String directory;

    public List<String> getAllRepositories(Remote remote) {
        return remote.getGitApi().getAllRemoteRepositories();
    }

    public void updateRepository(String repoName, Remote remote) {
        File repoPath = new File(directory + "/" + repoName);
        GitApi gitApi = remote.getGitApi();
        if(!repoPath.exists()) {
            gitApi.cloneRepository(repoName, repoPath);
        } else {
            gitApi.pullRepository(repoPath);
        }
    }

    public void updateAllRepositories(Remote remote) {
        List<String> repositories = getAllRepositories(remote);
        for(var repoName : repositories) {
            updateRepository(repoName, remote);
        }
    }

    public boolean containsRemoteRepository(Remote remote, String repoName) {
        return getAllRepositories(remote).contains(repoName);
    }
}
