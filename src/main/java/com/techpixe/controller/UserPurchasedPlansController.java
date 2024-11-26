package com.techpixe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.entity.UserPurchasedPlans;
import com.techpixe.service.UserPurchasedPlansService;

@RestController
@RequestMapping("/api/userPurchasedPlans")
public class UserPurchasedPlansController 
{
	@Autowired
	private UserPurchasedPlansService userPurchasedPlansService;
	
	@PostMapping("/saveHistory/{userProfile}")
	public ResponseEntity<UserPurchasedPlans> savePlanHistory(@PathVariable Long userProfile,@RequestParam Long planTypeId,@RequestParam String planType,@RequestParam Double paymentAmount,@RequestParam String validity)
	{
		UserPurchasedPlans userPurchasedPlans = userPurchasedPlansService.saveHistory(planTypeId, planType, paymentAmount,validity, userProfile);
		return new ResponseEntity<UserPurchasedPlans>(userPurchasedPlans,HttpStatus.CREATED);
	}
}
