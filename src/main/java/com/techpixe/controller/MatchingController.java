package com.techpixe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.service.MatchingService;

@RestController
@RequestMapping("/api/matching")
public class MatchingController 
{
	@Autowired
	private MatchingService matchingService;
	
	
	@PostMapping("/save/{userProfileId}")
	public ResponseEntity<?> sendToWebhookForMatching(@RequestBody Map<String, Object> requestData,@PathVariable Long userProfileId)
	{
//		System.err.println("*******requestData******"+requestData);
//		System.err.println("*******userProfileId******"+userProfileId);
		
		String jsonResponse = matchingService.sendToWebhookForMatching(requestData, userProfileId);
		return ResponseEntity.ok(jsonResponse);
	}
	
	
	@GetMapping("/fetchedMatchingLastData/{userProfileId}")
	public ResponseEntity<?> fetchedUserLast(@PathVariable Long userProfileId)
	{
		String matchingLastResponse = matchingService.fetchedUserLast(userProfileId);
		return new ResponseEntity<String>(matchingLastResponse,HttpStatus.OK);
	}
}
