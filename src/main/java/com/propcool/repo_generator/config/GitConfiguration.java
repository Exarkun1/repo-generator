package com.propcool.repo_generator.config;

import com.propcool.repo_generator.api.AbstractGitApi;
import com.propcool.repo_generator.utils.GitTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class GitConfiguration {
    /**
     * Сборка таблицы со списком облачных сервисов
     * */
    @Bean
    public GitTable gitTable(Set<AbstractGitApi> gitApis) {
        GitTable table = new GitTable();
        for(var gitApi : gitApis) {
            String name = gitApi.getClass().getSimpleName().toLowerCase();
            name = name.substring(0, name.length()-3);
            gitApi.setRemoteName(name);
            table.put(name, gitApi);
        }
        return table;
    }
}
