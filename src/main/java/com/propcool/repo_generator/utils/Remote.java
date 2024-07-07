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
        return Stream.of(values()).filter(remote -> remote.getRemoteName().equals(remoteName)).findAny().orElseThrow();
    }
}
