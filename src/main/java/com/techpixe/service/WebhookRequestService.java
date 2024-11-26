package com.techpixe.service;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.techpixe.entity.WebhookRequest;
import com.techpixe.entity.WebhookResponse;

public interface WebhookRequestService 
{
	//-----------Main---------------------
	
	public String sendToWebhook(Map<String, Object> payload,Long userProfileId,String title,String isLongTerm);
	
}
