package com.techpixe.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.entity.LifeTimeHoroscope;
import com.techpixe.repository.LifeTimeHoroScopeRepository;
import com.techpixe.service.LifeTimeHoroScopeService;



@RestController
@RequestMapping("/api/lifeTimeHoroscope")
public class LifeTimeHoroScopeController 
{
	@Autowired
	private LifeTimeHoroScopeService lifeTimeHoroScopeService;
	
	@Autowired
	private LifeTimeHoroScopeRepository lifeTimeHoroScopeRepository;
	

	
	
	@PostMapping("/Save")
	public ResponseEntity<?> lifeTimeHoroScope(@RequestBody Map<String, Object> payload,@RequestParam Long userProfileId)
	{
//		System.err.println("LifetTime Controller -1**");
//		System.err.println("LifetTime Controller -1 Payload **"+payload);
//		System.err.println("LifetTime Controller -1  userProfileId **"+userProfileId);
		
		
		String lifeTimeHoroscopeResponse = lifeTimeHoroScopeService.lifeTimeHoroscope(payload, userProfileId);
		return ResponseEntity.ok(lifeTimeHoroscopeResponse);
	}


}
