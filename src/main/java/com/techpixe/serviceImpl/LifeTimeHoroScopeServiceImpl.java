package com.techpixe.serviceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpixe.entity.LifeTimeHoroscope;
import com.techpixe.repository.LifeTimeHoroScopeRepository;
import com.techpixe.service.LifeTimeHoroScopeService;

@Service
public class LifeTimeHoroScopeServiceImpl implements LifeTimeHoroScopeService
{
	@Autowired
	private LifeTimeHoroScopeRepository lifeTimeHoroScopeRepository;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public String lifeTimeHoroscope(Map<String, Object> payload, Long userProfileId) {
	    String webhookUrl = "https://hook.eu2.make.com/tvue2uj4saxl6u52wd7c5n7u495s9qmo";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    Optional<LifeTimeHoroscope> userFound = lifeTimeHoroScopeRepository.findByUserProfileId(userProfileId);

	    if (userFound.isPresent()) {
	        LifeTimeHoroscope existingUserLifeTimeData = userFound.get();
	        //System.err.println("Condition -2 : Already data exists**");
	        return existingUserLifeTimeData.getJsonResponse();
	    } else {
	        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, payload, String.class);
	        if (response.getStatusCode() != HttpStatus.OK) {
	            throw new RuntimeException("Failed to send webhook request, HTTP status: " + response.getStatusCode());
	        }

	        String jsonResponseString = (response.getBody() != null) ? response.getBody() : "{}";

	        LifeTimeHoroscope lifeTimeHoroscope = new LifeTimeHoroscope();
	        lifeTimeHoroscope.setCreatedAt(LocalDateTime.now());
	        lifeTimeHoroscope.setIsLongTerm("True");
	        lifeTimeHoroscope.setJsonResponse(jsonResponseString);
	        lifeTimeHoroscope.setTitle("LifeTime");
	        lifeTimeHoroscope.setUserProfileId(userProfileId);

	        lifeTimeHoroScopeRepository.save(lifeTimeHoroscope);

	        //System.err.println("First Time Life Time Data " + jsonResponseString);
	        //System.err.println("Condition -3 : First Time hitting webhook  for getting**");

	        return jsonResponseString;
	    }
	}

}
