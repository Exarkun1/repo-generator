package com.propcool.repo_generator.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Класс с методами для взаимодействия с git через JGit
 * */
@Component
public class GitUtil {
    public CredentialsProvider credentialsProvider(String username, String password) {
        return new UsernamePasswordCredentialsProvider(username, password);
    }

    public void cloneRepository(File repoPath, String repoUrl, String remoteName, CredentialsProvider cp) {
        repoPath.mkdir();
        try(Git git = Git.cloneRepository()
                .setCredentialsProvider(cp)
                .setURI(repoUrl)
                .setRemote(remoteName)
                .setDirectory(repoPath)
                .call()) {
            resolveBranches(git, repoPath, remoteName, cp);
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void pullRepository(File repoPath, String remoteName, CredentialsProvider cp) {
        try(Git git = Git.open(repoPath)) {
            removeNonExistBranches(git, repoPath, remoteName, cp);
            git.pull()
                    .setCredentialsProvider(cp)
                    .setRemote(remoteName)
                    .call();
            resolveBranches(git, repoPath, remoteName, cp);
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRemote(File repoPath, String remoteUrl, String remoteName) {
        try(Git git = Git.open(repoPath)) {
            git.remoteAdd()
                    .setName(remoteName)
                    .setUri(new URIish(remoteUrl))
                    .call();
        } catch (GitAPIException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushRepository(File repoPath, String remoteName, CredentialsProvider cp) {
        try(Git git = Git.open(repoPath)) {
            List<String> branches = branchNames(git.branchList().call());
            for(var branch : branches) {
                git.checkout().setName(branch).call();
                git.push()
                        .setCredentialsProvider(cp)
                        .setRemote(remoteName)
                        .call();
            }
            removeNonExistBranchesOnRemote(git, repoPath, remoteName, cp);
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeNonExistBranches(Git git, File repoPath, String remoteName, CredentialsProvider cp) throws GitAPIException {
        List<String> branches = branchNames(git.branchList().call());
        List<String> remoteBranches = branchNames(branchesByRemote(repoPath, remoteName, cp));
        List<String> branchesToDelete = branchesToDelete(branches, remoteBranches);
        git.checkout().setName(remoteBranches.getFirst()).call();
        git.branchDelete()
                .setBranchNames(branchesToDelete)
                .setForce(true)
                .call();
    }

    private void removeNonExistBranchesOnRemote(Git git, File repoPath, String remoteName, CredentialsProvider cp) throws GitAPIException {
        List<String> branches = branchNames(git.branchList().call());
        List<String> remoteBranches = branchNames(branchesByRemote(repoPath, remoteName, cp));
        List<String> remoteBranchesToDelete = branchesToDelete(remoteBranches, branches);
        for(var branch : remoteBranchesToDelete) {
            RefSpec refSpec = new RefSpec()
                    .setSource(null)
                    .setDestination("refs/heads/" + branch);
            git.push()
                    .setCredentialsProvider(cp)
                    .setRefSpecs(refSpec)
                    .setRemote(remoteName)
                    .call();
        }
    }

    private void resolveBranches(Git git, File repoPath, String remoteName, CredentialsProvider cp) throws IOException, GitAPIException {
        List<String> branches = branchNames(branchesByRemote(repoPath, remoteName, cp));
        String startBranch = git.getRepository().getBranch();
        for(var branch : branches) {
            git.branchCreate()
                    .setStartPoint(remoteName + "/" + branch)
                    .setName(branch)
                    .setForce(true)
                    .call();
        }
        git.checkout().setName(startBranch).call();
    }

    private List<String> branchNames(Collection<Ref> branches) {
        return branches
                .stream().map(Ref::getName)
                .map(s -> s.replaceAll("\\w+/", "")).toList();
    }

    private Collection<Ref> branchesByRemote(File repoPath, String remoteName, CredentialsProvider cp) {
        try(Git git = Git.open(repoPath)) {
            return git.lsRemote()
                    .setCredentialsProvider(cp)
                    .setRemote(remoteName)
                    .setHeads(true)
                    .call();
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> branchesToDelete(List<String> branches, List<String> existBranches) {
        List<String> branchesToDelete = new ArrayList<>();
        for (String branch : branches) {
            if (!existBranches.contains(branch)) {
                branchesToDelete.add(branch);
            }
        }
        return branchesToDelete;
    }
}
