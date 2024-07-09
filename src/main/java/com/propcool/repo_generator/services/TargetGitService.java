package com.propcool.repo_generator.services;

import com.propcool.repo_generator.api.GitApi;
import com.propcool.repo_generator.utils.Remote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Сервис для взаимодействия с целевым облачным сервисом
 * */
@Service
public class TargetGitService {
    @Value("${directory}")
    private String directory;

    public List<String> getAllLocalRepositories() {
        return Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
                .map(File::getName).toList();
    }

    public void updateRemoteRepository(String repoName, Remote remote) {
        File repoPath = new File(directory + "/" + repoName);
        GitApi gitApi = remote.getGitApi();
        List<String> services = gitApi.remoteList(repoPath);
        if(!services.contains(remote.getRemoteName())) {
            gitApi.createRemoteRepository(repoName);
            gitApi.addRemote(repoName, repoPath);
        }
        gitApi.pushRepository(repoPath);
    }

    public void updateAllRemoteRepositories(Remote remote) {
        getAllLocalRepositories().forEach(repoName -> updateRemoteRepository(repoName, remote));
    }

    public boolean containsLocalRepository(String repoName) {
        return getAllLocalRepositories().contains(repoName);
    }
}
