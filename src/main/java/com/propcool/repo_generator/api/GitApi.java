package com.propcool.repo_generator.api;

import java.io.File;
import java.util.List;

/**
 * Интерфейс с основными операциями для переноса репозиториев,
 * cloud методы взаимодействуют с api интерфейсом облачного сервиса,
 * остальные методы работают с JGit, то есть с git операциями
 * */
public interface GitApi {
    List<String> getAllRemoteRepositories();
    void createRemoteRepository(String repoName);
    void cloneRepository(String repoName, File repoPath);
    void pullRepository(File repoPath);
    void addRemote(String repoName, File repoPath);
    void pushRepository(File repoPath);
}
