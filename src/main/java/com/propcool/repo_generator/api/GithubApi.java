package com.propcool.repo_generator.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.propcool.repo_generator.utils.GitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Класс для взаимодействия с api облачного сервиса github
 * */
@Component
public class GithubApi extends AbstractGitApi {
    @Override
    public List<String> getAllCloudRepositories() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    allReposUrl,
                    HttpMethod.GET,
                    request,
                    String.class
            );
            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            return root.findValuesAsText("name");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCloudRepository(String repoName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String remoteUrl(String repoName) {
        return "https://github.com/" + username + "/" + repoName;
    }

    @Override
    protected String username() {
        return username;
    }

    @Override
    protected String password() {
        return password;
    }

    @Autowired
    public GithubApi(GitUtil gitUtil) {
        super(gitUtil);
    }

    @Value("${github.username}")
    private String username;

    @Value("${github.password}")
    private String password;

    private final String allReposUrl = "https://api.github.com/user/repos";
}
