package com.propcool.repo_generator.services;

import com.propcool.repo_generator.api.GitApi;
import com.propcool.repo_generator.utils.Remote;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RemoteConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    public void updateRepository(String repoName, Remote remote) {
        File repoPath = new File(directory + "/" + repoName);
        GitApi gitApi = remote.getGitApi();
        try(Git git = Git.open(repoPath)) {
            List<String> services = git.remoteList().call().stream().map(RemoteConfig::getName).toList();
            if(!services.contains(remote.getRemoteName())) {
                gitApi.createRemoteRepository(repoName);
                gitApi.addRemote(repoName, repoPath);
            }
            gitApi.pushRepository(repoPath);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAllRepositories(Remote remote) {
        Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
                .map(File::getName).forEach(repoName -> updateRepository(repoName, remote));
    }
}
