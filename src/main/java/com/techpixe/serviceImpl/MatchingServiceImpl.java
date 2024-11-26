package com.techpixe.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.entity.Matching;
import com.techpixe.entity.UserProfile;
import com.techpixe.repository.MatchingRepository;
import com.techpixe.repository.UserProfileRepository;
import com.techpixe.service.MatchingService;

@Service
public class MatchingServiceImpl  implements MatchingService
{
	
	@Autowired
	private MatchingRepository matchingRepository;
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	//Final
	@Override
	public String sendToWebhookForMatching(Map<String, Object> payload, Long userProfileId) {
		
		
	    UserProfile userProfile1 = userProfileRepository.findById(userProfileId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not Found " + userProfileId));
	    
	    Optional<Matching> machedUserFound =matchingRepository.findByUserProfileUserProfileId(userProfileId);
	            
	    
	    if (userProfile1 != null)
	    {
	        if (machedUserFound.isPresent())
	        {
	        	//System.err.println("***********************2nd time updating************************");
	            // If the matching user is found, update the existing matching entry
	            Matching existingMatching = machedUserFound.get();
	            
	            // Update the matching fields based on the userProfileId and payload data
	            existingMatching.setPartnerName(String.valueOf(payload.get("partnerName")));
	            existingMatching.setPartnerGender(String.valueOf(payload.get("partnerGender")));
	            existingMatching.setPartnerPincode(String.valueOf(payload.get("partnerPincode")));
	            existingMatching.setPartnerLatitude(String.valueOf(payload.get("partnerLatitude")));
	            existingMatching.setPartnerLongitude(String.valueOf(payload.get("partnerLongitude")));
	            existingMatching.setPartnerDateOfBirth(String.valueOf(payload.get("partnerDateOfBirth")));
	            existingMatching.setPartnerTimeOfBirth(String.valueOf(payload.get("partnerTimeOfBirth")));
	            existingMatching.setPartnerLanguage(String.valueOf(payload.get("partnerLanguage")));
	            existingMatching.setTitle(String.valueOf(payload.get("title")));
	            existingMatching.setUserProfile(existingMatching.getUserProfile());
	            
	            // Optional: Save the updated matching entry
	            //matchingRepository.save(existingMatching);

	            // Prepare webhook payload
	            Map<String, Object> webhookPayload = new HashMap<>();
	            if ((boolean) payload.get("userGender"))
	            {
	            	//System.err.println("***********"+(boolean) payload.get("userGender")+"*********************");
	            	//System.err.println("************BoyFirst************");
	            	
	                webhookPayload.put("Boy_dob", payload.get("Dob"));
	                webhookPayload.put("Boy_tob", payload.get("Tob"));
	                webhookPayload.put("Boy_tz", payload.get("Tz"));
	                webhookPayload.put("Boy_lat", payload.get("Lat"));
	                webhookPayload.put("Boy_lon", payload.get("Lon"));
	                webhookPayload.put("Lang", payload.get("Lang"));
	                webhookPayload.put("Girl_dob", String.valueOf(payload.get("partnerDateOfBirth")));
	                webhookPayload.put("Girl_tob", String.valueOf(payload.get("partnerTimeOfBirth")));
	                webhookPayload.put("Girl_tz", String.valueOf(payload.get("partnerTimezone")));
	                webhookPayload.put("Girl_lat", String.valueOf(payload.get("partnerLatitude")));
	                webhookPayload.put("Girl_lon", String.valueOf(payload.get("partnerLongitude")));
	                webhookPayload.put("Lang", String.valueOf(payload.get("partnerLanguage")));
	                
	                webhookPayload.put("title", String.valueOf(payload.get("title")));
	                webhookPayload.put("cardId", payload.get("cardId"));
	                
	            } 
	            else 
	            {
	            	//System.err.println("************GirlFirst************");
	            	
	                webhookPayload.put("Girl_dob", payload.get("Dob"));
	                webhookPayload.put("Girl_tob", payload.get("Tob"));
	                webhookPayload.put("Girl_tz", payload.get("Tz"));
	                webhookPayload.put("Girl_lat", payload.get("Lat"));
	                webhookPayload.put("Girl_lon", payload.get("Lon"));
	                webhookPayload.put("Lang", payload.get("Lang"));
	                webhookPayload.put("Boy_dob", String.valueOf(payload.get("partnerDateOfBirth")));
	                webhookPayload.put("Boy_tob", String.valueOf(payload.get("partnerTimeOfBirth")));
	                webhookPayload.put("Boy_tz", String.valueOf(payload.get("partnerTimezone")));
	                webhookPayload.put("Boy_lat", String.valueOf(payload.get("partnerLatitude")));
	                webhookPayload.put("Boy_lon", String.valueOf(payload.get("partnerLongitude")));
	                webhookPayload.put("Lang", String.valueOf(payload.get("partnerLanguage")));
	                
	                webhookPayload.put("title", String.valueOf(payload.get("title")));
	                webhookPayload.put("cardId", payload.get("cardId"));
	            }
	            
	            // Sending the webhook request
	            String webhookUrl = "https://hook.eu2.make.com/tvue2uj4saxl6u52wd7c5n7u495s9qmo";
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, webhookPayload, String.class);

	            if (response.getStatusCode() != HttpStatus.OK)
	            {
	                throw new RuntimeException("Failed to send webhook request, HTTP status: " + response.getStatusCode());
	            }

	            // Save the response in the Matching entity
	            String jsonResponseString = (response.getBody() != null) ? response.getBody() : "{}";
	            existingMatching.setJsonResponse(jsonResponseString);
	            matchingRepository.save(existingMatching);
	            
	            //System.err.println("*******Json Response Matching Updated 2nd time******* " + jsonResponseString);
	            return jsonResponseString;
	        }
	        //*********************************************************************First Time***********************************************************************
	        else 
	        {
	            // If the matching user is not found, create a new matching entry
	            Matching matching = new Matching();
	            matching.setPartnerName(String.valueOf(payload.get("partnerName")));
	            matching.setPartnerGender(String.valueOf(payload.get("partnerGender")));
	            matching.setPartnerPincode(String.valueOf(payload.get("partnerPincode")));
	            matching.setPartnerLatitude(String.valueOf(payload.get("partnerLatitude")));
	            matching.setPartnerLongitude(String.valueOf(payload.get("partnerLongitude")));
	            matching.setPartnerDateOfBirth(String.valueOf(payload.get("partnerDateOfBirth")));
	            matching.setPartnerTimeOfBirth(String.valueOf(payload.get("partnerTimeOfBirth")));
	            matching.setPartnerLanguage(String.valueOf(payload.get("partnerLanguage")));
	            matching.setTitle(String.valueOf(payload.get("title")));
	            matching.setUserProfile(userProfile1);

	            // Save the new matching entry to the database
	           // matchingRepository.save(matching);
	            
	            Map<String, Object> webhookPayload = new HashMap<>();
	            if ((boolean) payload.get("userGender"))
	            {
	            	//System.err.println("************BoyFirst************");
	            	
	                webhookPayload.put("Boy_dob", payload.get("Dob"));
	                webhookPayload.put("Boy_tob", payload.get("Tob"));
	                webhookPayload.put("Boy_tz", payload.get("Tz"));
	                webhookPayload.put("Boy_lat", payload.get("Lat"));
	                webhookPayload.put("Boy_lon", payload.get("Lon"));
	                webhookPayload.put("Lang", payload.get("Lang"));
	                webhookPayload.put("Girl_dob", String.valueOf(payload.get("partnerDateOfBirth")));
	                webhookPayload.put("Girl_tob", String.valueOf(payload.get("partnerTimeOfBirth")));
	                webhookPayload.put("Girl_tz", String.valueOf(payload.get("partnerTimezone")));
	                webhookPayload.put("Girl_lat", String.valueOf(payload.get("partnerLatitude")));
	                webhookPayload.put("Girl_lon", String.valueOf(payload.get("partnerLongitude")));
	                webhookPayload.put("Lang", String.valueOf(payload.get("partnerLanguage")));
	                
	                webhookPayload.put("title", String.valueOf(payload.get("title")));
	                webhookPayload.put("cardId", payload.get("cardId"));
	            } 
	            else 
	            {
	            	//System.err.println("************GirlFirst************");
	            	
	                webhookPayload.put("Girl_dob", payload.get("Dob"));
	                webhookPayload.put("Girl_tob", payload.get("Tob"));
	                webhookPayload.put("Girl_tz", payload.get("Tz"));
	                webhookPayload.put("Girl_lat", payload.get("Lat"));
	                webhookPayload.put("Girl_lon", payload.get("Lon"));
	                webhookPayload.put("Lang", payload.get("Lang"));
	                webhookPayload.put("Boy_dob", String.valueOf(payload.get("partnerDateOfBirth")));
	                webhookPayload.put("Boy_tob", String.valueOf(payload.get("partnerTimeOfBirth")));
	                webhookPayload.put("Boy_tz", String.valueOf(payload.get("partnerTimezone")));
	                webhookPayload.put("Boy_lat", String.valueOf(payload.get("partnerLatitude")));
	                webhookPayload.put("Boy_lon", String.valueOf(payload.get("partnerLongitude")));
	                webhookPayload.put("Lang", String.valueOf(payload.get("partnerLanguage")));
	                
	                webhookPayload.put("title", String.valueOf(payload.get("title")));
	                webhookPayload.put("cardId", payload.get("cardId"));
	            }
	            
	            // Sending the webhook request
	            String webhookUrl = "https://hook.eu2.make.com/tvue2uj4saxl6u52wd7c5n7u495s9qmo";
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, webhookPayload, String.class);

	            if (response.getStatusCode() != HttpStatus.OK)
	            {
	                throw new RuntimeException("Failed to send webhook request, HTTP status: " + response.getStatusCode());
	            }

	            // Save the response in the Matching entity
	            String jsonResponseString = (response.getBody() != null) ? response.getBody() : "{}";
	            matching.setJsonResponse(jsonResponseString);
	            matchingRepository.save(matching);
	            
	            //System.err.println("*******Json Response Matching Updated 1st  time******* " + jsonResponseString);
	            return jsonResponseString;
	            
	        }
	    } 
	    else 
	    {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not Found " + userProfileId);
	    }
	}




	@Override
	public String fetchedUserLast(Long userProfileId)
	{
		Matching fetchedMatched = matchingRepository.findByUserProfileUserProfileId(userProfileId).orElseThrow(()->new ResponseStatusException(HttpStatus.OK,"Data is not Present"));
		if (fetchedMatched!=null)
		{
			return fetchedMatched.getJsonResponse();
		} 
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Data is not Present");
		}
	}
	
	
	



	
}
