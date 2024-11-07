package com.eSignify.common.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class GoogleAuthService {
    
    private final RestTemplate restTemplate = new RestTemplate();

	@Value("${my.client_id}")
	private String clientId;
	
	@Value("${my.redirect_uri}")
	private String redirectUri;
	
	@Value("${my.client_secret}")
	private String clientSecret;
	
	
    public String getAccessToken(String authorizationCode) {
        String url = "https://oauth2.googleapis.com/token";

        // ��û �ٵ� ���� 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "code=" + authorizationCode +
                             "&client_id=" + clientId +
                             "&client_secret=" + clientSecret +
                             "&redirect_uri=" + redirectUri +
                             "&grant_type=authorization_code";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // ��û ����
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            // ���信�� �׼��� ��ū ����
            JSONObject json = new JSONObject(response.getBody());
            return json.getString("access_token");
        } else {
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }
    }
}