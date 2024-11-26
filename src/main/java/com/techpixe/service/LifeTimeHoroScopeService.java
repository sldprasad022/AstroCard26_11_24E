package com.techpixe.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface LifeTimeHoroScopeService 
{
	public String lifeTimeHoroscope(Map<String, Object> payload,Long userProfileId);
}
