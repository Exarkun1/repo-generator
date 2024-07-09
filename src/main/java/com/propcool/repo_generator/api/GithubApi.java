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
 * Класс для взаимодействия с api облачного сервиса github
 * */
@Component
public class GithubApi extends AbstractGitApi {
    @Value("${github.username}")
    private String username;

    @Value("${github.password}")
    private String password;

    private static final String REPO_URL = "https://api.github.com/user/repos";

    @Autowired
    public GithubApi(GitUtil gitUtil, RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(gitUtil, restTemplate, objectMapper, Remote.GITHUB);
    }

    @Override
    public List<String> getAllRemoteRepositories() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    REPO_URL,
                    HttpMethod.GET,
                    request,
                    String.class
            );
            String responseBody = response.getBody();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.findValuesAsText("name");
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
        jsonBody.put("name", repoName);
        jsonBody.put("private", true);
        var request = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                REPO_URL,
                HttpMethod.POST,
                request,
                String.class
        );
    }

    @Override
    protected String remoteUrl(String repoName) {
        return "https://github.com/" + username + "/" + repoName;
    }

    @Override
    protected CredentialsProvider credentialsProvider() {
        return gitUtil.credentialsProvider(username, password);
    }
}
