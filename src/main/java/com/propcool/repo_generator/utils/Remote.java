package com.propcool.repo_generator.utils;

import com.propcool.repo_generator.api.GitApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum Remote {
    GITHUB("github"), BITBUCKET("bitbucket");

    private final String remoteName;

    @Setter
    private GitApi gitApi;
}
