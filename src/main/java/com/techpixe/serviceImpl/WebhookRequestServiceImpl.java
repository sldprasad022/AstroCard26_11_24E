package com.techpixe.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpixe.entity.Payments;
import com.techpixe.entity.WebhookRequest;
import com.techpixe.entity.WebhookResponse;
import com.techpixe.repository.PaymentsRepository;
import com.techpixe.repository.WebhookRequestRepository;
import com.techpixe.service.WebhookRequestService;

@Service
public class WebhookRequestServiceImpl implements WebhookRequestService {
	@Autowired
	private WebhookRequestRepository webhookRequestRepository;
	
	@Autowired
	private PaymentsRepository paymentsRepository;

	private final RestTemplate restTemplate = new RestTemplate();
	
	
	//here, we are getting payment status == Success based on userProfileId .it will return true otherwise  it will return false
	public boolean areAllPaymentsSuccessful(Long userProfileId) {
	    		//System.err.println("Checking payments for userProfileId: " + userProfileId);
	    List<Payments> allPayments = paymentsRepository.findByStatusAndUserProfile("Success", userProfileId);
	    
	    if (allPayments.isEmpty())
	    {
	        	//System.err.println("No payments found for userProfileId: " + userProfileId);
	        return false; // Or false, based on your logic
	    }

	    for (Payments payment : allPayments) {
	        	//System.err.println("Payment status: " + payment.getStatus());
	        if (!"Success".equals(payment.getStatus()))
	        {
	        	// System.err.println("111 False Pid "+payment.getpId());
	            return false; // Return false immediately if any status is not "Success"
	        }
	    }

	    return true; // Return true only if all statuses are "Success"
	}



	//Final 
	@Override
	public String sendToWebhook(Map<String, Object> payload, Long userProfileId, String title, String isLongTerm)
	{
	    String webhookUrl = "https://hook.eu2.make.com/tvue2uj4saxl6u52wd7c5n7u495s9qmo";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    // Fetch the record based on userProfileId and title
	    Optional<WebhookRequest> fetchedBasedOnUserIdAndTitle = webhookRequestRepository.findByUserProfileIdAndTitle(userProfileId, title);
	    
	    
	    //here, we are getting payment status == Success based on userProfileId
	     boolean paymentStatus = areAllPaymentsSuccessful(userProfileId);
	     
	     			//System.err.println("paymentStatus : "+paymentStatus);
	     
	     // here, we are checking the particular user has payment has done or not. if the plan time beyond the out of the range the payments status will change Success to expired. 
	     if (paymentStatus) 
	     {
	    	 		// System.err.println("if block executes whoever has done the payment");
	    	 
	    	 		//System.err.println(" Condition -1 (Payment Status is Success) : if block executes whoever has done the payment");
	    	 
	    	 		// Fetch dynamic time range for the given title
			 	    LocalDateTime[] timeRange = getDynamicTimeRange(userProfileId, title);
			 	    LocalDateTime startDate = timeRange[0];
			 	    LocalDateTime endDate = timeRange[1];
		
			 	    if (fetchedBasedOnUserIdAndTitle.isPresent() && isLongTerm.equals("true"))
			 	    {
			 	    	
			 	    		//System.err.println("Condition-2 : user is Present based on title and title is Long term only");
			 	    	
			 	        WebhookRequest existingRequest = fetchedBasedOnUserIdAndTitle.get();
			 	        LocalDateTime responseDate = existingRequest.getCreatedAt();
		
			 	        // Check if the responseDate is within the range
			 	        if (responseDate != null && !responseDate.isBefore(startDate) && !responseDate.isAfter(endDate)) 
			 	        {
			 	        	//System.err.println("Condition-3 : date is with in range with respect to title");
			 	        	
			 	        	//System.err.println("**** Date is Present with in the range ****");
			 	      
			 	            return existingRequest.getJsonResponse();
			 	            
			 	        }
			 	        // Send a new webhook request and update the record if out of date range
			 	        else 
			 	        {
			 	            // Send a new webhook request and update the record if out of range
			 	            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, payload, String.class);
			 	            if (response.getStatusCode() != HttpStatus.OK)
			 	            {
			 	                throw new RuntimeException("Failed to send webhook request, HTTP status: " + response.getStatusCode());
			 	            }
		
			 	            		//System.err.println("*************Date is beyond the Range**********");
			 	            
			 	            		//System.err.println("Condition 4: Date is beyond the Range and update the records of the user and again hit the webhook");
			 	            
			 	            String jsonResponseString = (response.getBody() != null) ? response.getBody() : "{}";
			 	            existingRequest.setCreatedAt(LocalDateTime.now());
			 	            existingRequest.setIsLongTerm(isLongTerm);
			 	            existingRequest.setJsonResponse(jsonResponseString);
			 	            webhookRequestRepository.save(existingRequest);
			 	        }
			 	    }
			 	    //if the title(service) is long term
			 	    else if(isLongTerm.equals("true"))
			 	    {
			 	    				//System.err.println("Condition 5: if the title(service) is long term for the First time");
			 	    	
//			 	    	 If user Id is not Present with the specific title(service), send a new webhook request
			 		    ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, payload, String.class);
			 	
			 		    if (response.getStatusCode() != HttpStatus.OK) {
			 		        throw new RuntimeException("Failed to send webhook request, HTTP status: " + response.getStatusCode());
			 		    }
			 	
			 		    String jsonResponseString = (response.getBody() != null) ? response.getBody() : "{}";
			 	
			 		    // Create a new WebhookRequest
			 		    WebhookRequest webhookRequest = new WebhookRequest();
			 		    webhookRequest.setCreatedAt(LocalDateTime.now());
			 		    webhookRequest.setUserProfileId(userProfileId);
			 		    webhookRequest.setTitle(title);
			 		    webhookRequest.setIsLongTerm(isLongTerm);
			 		    webhookRequest.setJsonResponse(jsonResponseString);
		//	 		    webhookRequest.setResponseDate(LocalDateTime.now()); // Set the response date to today or based on your logic
			 	
			 		    // Save the new WebhookRequest
			 		    webhookRequestRepository.save(webhookRequest);
			 	
			 		    	// System.err.println("******************Webhook********************");
			 		    return jsonResponseString;
			 	    }
		
			 	    // If user Id is not present with any specific title, send a new webhook request , all short term Services
			 	    ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, payload, String.class);
			 	    if (response.getStatusCode() != HttpStatus.OK) 
			 	    {
			 	        throw new RuntimeException("Failed to send webhook request, HTTP status: " + response.getStatusCode());
			 	    }
		
			 	    String jsonResponseString = (response.getBody() != null) ? response.getBody() : "{}";
			 	    
			 	    	// System.err.println("Condition 6 : If user Id is not present with any specific title, send a new webhook request");
			 	    
			 	    return jsonResponseString;
	     }
	    else
	    {
	    				//System.err.println(" Condition -7 : else block executes whoever has not done the payment");
	    	
	    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"You need to complete your payment to access this service. Please finish the payment process....");
		}
	      
	}
	
	
	
	
	private LocalDateTime[] getDynamicTimeRange(Long userProfileId, String title)
	{
	    LocalDateTime now = LocalDateTime.now();

	    			// Check for predefined titles and return their time ranges
	    switch (title)
	    {
	        case "Yearly":
	            return new LocalDateTime[]{
	                LocalDateTime.of(now.getYear(), 1, 1, 0, 0),
	                LocalDateTime.of(now.getYear(), 12, 31, 23, 59)
	            };
	        case "Title2":
	            return new LocalDateTime[]{
	                LocalDateTime.of(now.getYear(), 3, 1, 0, 0),
	                LocalDateTime.of(now.getYear(), 6, 30, 23, 59)
	            };
	        case "Title3":
	            return new LocalDateTime[]{
	                LocalDateTime.of(now.getYear(), 7, 1, 0, 0),
	                LocalDateTime.of(now.getYear(), 9, 30, 23, 59)
	            };
	            // Add more cases as needed
	    }

	    // If the title doesn't match predefined cases, fetch the first request
	    Optional<WebhookRequest> firstRequest = webhookRequestRepository.findByUserProfileIdAndTitle(userProfileId, title);

	    // Calculate the start date based on whether the first request exists
	    LocalDateTime startDate = firstRequest.map(WebhookRequest::getCreatedAt)
	                            .orElse(now); // If not found, use the current date
	    	// System.err.println("AI 12 Month Prediction Start Date ****: "+ startDate);
	    
	    LocalDateTime endDate = startDate.plusYears(1); // End date is one year from start date
	    
	    	// System.err.println("AI 12 Month Prediction End Date ****: "+ endDate);

	    return new LocalDateTime[]{startDate, endDate};
	    
	}

	
	

	// Method to dynamically get the time range based on title
	private LocalDateTime[] getDynamicTimeRange1(String title) 
	{
	    LocalDateTime now = LocalDateTime.now();

	    switch (title)
	    {
	        case "Yearly":
	        			//System.err.println("Yearly is present: "+title);
	            return new LocalDateTime[]{
	                LocalDateTime.of(now.getYear(), 1, 1, 0, 0),
	                LocalDateTime.of(now.getYear(), 12, 31, 23, 59)
	            };
	        case "Title2":
	            return new LocalDateTime[]{
	                LocalDateTime.of(now.getYear(), 3, 1, 0, 0),
	                LocalDateTime.of(now.getYear(), 6, 30, 23, 59)
	            };
	        case "Title3":
	            return new LocalDateTime[]{
	                LocalDateTime.of(now.getYear(), 7, 1, 0, 0),
	                LocalDateTime.of(now.getYear(), 9, 30, 23, 59)
	            };
	        // Add more cases as needed
	        default:
	            return new LocalDateTime[]{LocalDateTime.MIN, LocalDateTime.MAX}; // Default range
	    }
	}




}
