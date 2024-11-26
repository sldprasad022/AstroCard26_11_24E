package com.techpixe.service;

import java.util.Map;

public interface MatchingService 
{
	public String sendToWebhookForMatching(Map<String, Object> payload,Long userProfileId);
	
	public String fetchedUserLast(Long userProfileId);
	
}
