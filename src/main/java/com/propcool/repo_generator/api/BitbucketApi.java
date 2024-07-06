package com.propcool.repo_generator.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propcool.repo_generator.utils.GitUtil;
import com.propcool.repo_generator.utils.Remote;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для взаимодействия с api облачного сервиса bitbucket
 * */
@Component
public class BitbucketApi extends AbstractGitApi {
    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.password}")
    private String password;

    @Value("${bitbucket.workspace}")
    private String workspace;

    private static final String CREATE_REPO_URL = "https://api.bitbucket.org/2.0/repositories";

    @Autowired
    public BitbucketApi(GitUtil gitUtil, RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(gitUtil, restTemplate, objectMapper, Remote.BITBUCKET);
    }

    @Override
    public List<String> getAllCloudRepositories() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCloudRepository(String repoName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("is_private", true);
        var request = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createRepoUrl(repoName),
                HttpMethod.POST,
                request,
                String.class
        );
    }

    @Override
    protected String remoteUrl(String repoName) {
        return "https://" + username + "@bitbucket.org/" + workspace + "/" + repoName;
    }

    @Override
    protected CredentialsProvider credentialsProvider() {
        return gitUtil.credentialsProvider(username, password);
    }

    protected String createRepoUrl(String repoName) {
        return CREATE_REPO_URL + "/" + workspace + "/" + repoName;
    }
}
