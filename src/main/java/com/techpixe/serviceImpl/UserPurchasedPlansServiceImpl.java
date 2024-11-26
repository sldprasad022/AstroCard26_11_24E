package com.techpixe.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.entity.UserProfile;
import com.techpixe.entity.UserPurchasedPlans;
import com.techpixe.repository.UserProfileRepository;
import com.techpixe.repository.UserPurchasedPlansRepository;
import com.techpixe.service.UserPurchasedPlansService;

@Service 
public class UserPurchasedPlansServiceImpl implements UserPurchasedPlansService
{
	@Autowired
	private UserPurchasedPlansRepository userPurchasedPlansRepository;
	
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Override
	public UserPurchasedPlans saveHistory(Long planTypeId, String planType, Double paymentAmount, String validity,Long userProfile) 
	{
		UserProfile userProfile2 = userProfileRepository.findById(userProfile).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this ID "+userProfile+ " is not found"));
		if (userProfile2!=null) 
		{
			UserPurchasedPlans userPurchasedPlans = new UserPurchasedPlans();
			userPurchasedPlans.setPlanTypeId(planTypeId);
			userPurchasedPlans.setPlanType(planType);
			userPurchasedPlans.setPaymentAmount(paymentAmount);
			userPurchasedPlans.setValidity(validity);
			userPurchasedPlans.setUserProfile(userProfile2);
			
			return userPurchasedPlansRepository.save(userPurchasedPlans);
			
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this ID  "+userProfile+ " is not found");
		}
	}

}
