package com.techpixe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.techpixe.entity.WebhookResponse;
import com.techpixe.service.WebhookRequestService;

@RestController
@RequestMapping("/api/webhook/prediction")
public class WebhookController 
{
	@Autowired
	private WebhookRequestService webhookRequestService;
	

	@PostMapping("/trigger")
    public ResponseEntity<?> triggerWebhook(@RequestBody Map<String, Object> payload,@RequestParam Long userProfileId, @RequestParam String title, @RequestParam String isLongTerm)
	{
        // Call the service to hit the webhook and return the response
//		System.err.println("******Controller Called **********");
//		System.err.println("userProfileId ::: "+userProfileId);
//		System.err.println("title : "+title);
//		System.err.println("isLongTerm : "+ isLongTerm);
//		System.err.println(payload.toString());
		
		
		 String webhookResponse = webhookRequestService.sendToWebhook(payload,userProfileId,title,isLongTerm);
		 return ResponseEntity.ok(webhookResponse);
    }
	
}
