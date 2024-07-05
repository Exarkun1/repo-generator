package com.propcool.repo_generator.api;

import com.propcool.repo_generator.utils.GitUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;

/**
 * Класс с реализацией методов взаимодействующих с JGit,
 * то есть только с git операциями
 * */
@RequiredArgsConstructor
public abstract class AbstractGitApi implements GitApi {
    @Override
    public void cloneRepository(String repoName, File repoPath) {
        CredentialsProvider cp = gitUtil.credentialsProvider(username(), password());
        gitUtil.cloneRepository(repoPath, remoteUrl(repoName), remoteName, cp);
    }

    @Override
    public void pullRepository(File repoPath) {
        CredentialsProvider cp = gitUtil.credentialsProvider(username(), password());
        gitUtil.pullRepository(repoPath, remoteName, cp);
    }

    @Override
    public void addRemote(String repoName, File repoPath) {
        gitUtil.addRemote(repoPath, remoteUrl(repoName), remoteName);
    }

    @Override
    public void pushRepository(File repoPath) {
        CredentialsProvider cp = gitUtil.credentialsProvider(username(), password());
        gitUtil.pushRepository(repoPath, remoteName, cp);
    }

    protected abstract String remoteUrl(String repoName);

    protected abstract String username();

    protected abstract String password();

    @Setter
    protected String remoteName;

    protected final GitUtil gitUtil;
}
