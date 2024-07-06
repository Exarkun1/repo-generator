package com.propcool.repo_generator.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propcool.repo_generator.utils.GitUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * Класс с реализацией методов взаимодействующих с JGit,
 * то есть только с git операциями
 * */
@RequiredArgsConstructor
public abstract class AbstractGitApi implements GitApi {
    @Setter
    protected String remoteName;

    protected final GitUtil gitUtil;

    protected final RestTemplate restTemplate;

    protected final ObjectMapper objectMapper;

    @Override
    public void cloneRepository(String repoName, File repoPath) {
        CredentialsProvider cp = credentialsProvider();
        gitUtil.cloneRepository(repoPath, remoteUrl(repoName), remoteName, cp);
    }

    @Override
    public void pullRepository(File repoPath) {
        CredentialsProvider cp = credentialsProvider();
        gitUtil.pullRepository(repoPath, remoteName, cp);
    }

    @Override
    public void addRemote(String repoName, File repoPath) {
        gitUtil.addRemote(repoPath, remoteUrl(repoName), remoteName);
    }

    @Override
    public void pushRepository(File repoPath) {
        CredentialsProvider cp = credentialsProvider();
        gitUtil.pushRepository(repoPath, remoteName, cp);
    }

    protected abstract String remoteUrl(String repoName);

    protected abstract CredentialsProvider credentialsProvider();
}
