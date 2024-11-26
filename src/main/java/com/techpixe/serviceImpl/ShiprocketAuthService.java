package com.techpixe.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShiprocketAuthService {

    @Value("${shiprocket.email}")
    private String email;

    @Value("${shiprocket.password}")
    private String password;

    private String token;
    private LocalDateTime tokenExpiry;

    @Autowired
    private RestTemplate restTemplate;


    public String getToken() {
        if (token == null || tokenExpiry.isBefore(LocalDateTime.now()))
        {
        	//System.err.println("***********tokenExpiry************"+tokenExpiry);
        	
        	//System.err.println("*********tokenExpiry.isBefore(LocalDateTime.now())*********"+tokenExpiry.isBefore(LocalDateTime.now()));
        	
            generateToken();
        }
        return token;
    }

    private void generateToken() {
        String url = "https://apiv2.shiprocket.in/v1/external/auth/login";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            this.token = jsonResponse.getString("token");
            this.tokenExpiry = LocalDateTime.now().plusDays(10);  // Set expiry for 10 days
        } else {
            throw new RuntimeException("Failed to authenticate with Shiprocket: " + response.getBody());
        }
    }

    // Schedule token refresh every 10 days, adjust as needed for your application.
    @Scheduled(fixedRate = 864000000) // 10 days in milliseconds
    private void refreshToken() {
        generateToken();
    }
}

