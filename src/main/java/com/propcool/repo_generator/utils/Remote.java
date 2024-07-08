package com.propcool.repo_generator.utils;

import com.propcool.repo_generator.api.GitApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Remote {
    GITHUB("github"), BITBUCKET("bitbucket");

    private final String remoteName;

    @Setter
    private GitApi gitApi;

    public static Remote getByRemoteName(String remoteName) {
        for(var remote : values()) {
            if(remote.getRemoteName().equals(remoteName)) {
                return remote;
            }
        }
        return null;
    }

    public static boolean containsByRemoteName(String remoteName) {
        return getByRemoteName(remoteName) != null;
    }
}
