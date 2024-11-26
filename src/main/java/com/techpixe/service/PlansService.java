package com.techpixe.service;

import java.util.List;
import java.util.Optional;

import com.techpixe.entity.Plans;

public interface PlansService 
{
	Plans savePlan(Long planTypeId,String planType,double actualPrice,Double discountPercentage,String validity);
	
	Plans getPlanByName(String planType);
	
	List<Plans> allPlans();
	
	public Plans fetchByPlanId(Long planId);
	
	public void deleteByPlanType(Long planId);

	Optional<Plans> updatePlanByType(Long planTypeId, String planType, Double actualPrice, Double discountPercentage,
			String validity);
}
