package com.techpixe.service;

import com.techpixe.entity.UserPurchasedPlans;

public interface UserPurchasedPlansService 
{
	public UserPurchasedPlans saveHistory(Long planId,String planType,Double paymentAmount,String validity,Long userProfile);
}
