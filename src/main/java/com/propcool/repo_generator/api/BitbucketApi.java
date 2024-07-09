package com.propcool.repo_generator.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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

    private static final String REPO_URL = "https://api.bitbucket.org/2.0/repositories";

    @Autowired
    public BitbucketApi(GitUtil gitUtil, RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(gitUtil, restTemplate, objectMapper, Remote.BITBUCKET);
    }

    @Override
    public List<String> getAllRemoteRepositories() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    reposUrl(),
                    HttpMethod.GET,
                    request,
                    String.class
            );
            String responseBody = response.getBody();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.findValuesAsText("full_name").stream()
                    .map(fullName -> fullName.replaceAll("\\w+/", "")).toList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createRemoteRepository(String repoName) {
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
        return REPO_URL + "/" + workspace + "/" + repoName;
    }

    protected String reposUrl() {
        return REPO_URL + "/" + workspace;
    }
}
