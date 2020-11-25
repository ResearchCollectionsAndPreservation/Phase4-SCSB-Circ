package org.recap.ils.service;


import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rajeshbabuk on 9/12/16.
 */

@Service
public class NyplOauthTokenApiService {

    private static final Logger logger = LoggerFactory.getLogger(NyplOauthTokenApiService.class);

    @Value("${ils.nypl.oauth.token.api}")
    public String nyplOauthTokenApiUrl;

    @Autowired
    RestTemplate restTemplate;

    public String generateAccessTokenForNyplApi(String operatorUserId, String operatorPassword) throws Exception {
        String authorization = "Basic " + new String(Base64Utils.encode((operatorUserId + ":" + operatorPassword).getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", authorization);

        HttpEntity<String> requestEntity = new HttpEntity("grant_type=client_credentials", headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(nyplOauthTokenApiUrl, HttpMethod.POST, requestEntity, String.class);
        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        return (String) jsonObject.get("access_token");
    }
}
