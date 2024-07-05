package com.propcool.repo_generator.api;

import com.propcool.repo_generator.utils.GitUtil;
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
    @Override
    public List<String> getAllCloudRepositories() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCloudRepository(String repoName) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //headers.setBearerAuth(createToken());
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
    protected String username() {
        return username;
    }

    @Override
    protected String password() {
        return password;
    }

    protected String createRepoUrl(String repoName) {
        return createRepoUrl + "/" + workspace + "/" + repoName;
    }

    @Autowired
    public BitbucketApi(GitUtil gitUtil) {
        super(gitUtil);
    }

    @Value("${bitbucket.workspace}")
    private String workspace;

    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.password}")
    private String password;

    private final String createRepoUrl = "https://api.bitbucket.org/2.0/repositories";
}
