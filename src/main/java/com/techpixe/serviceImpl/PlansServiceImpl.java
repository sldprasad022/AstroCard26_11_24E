package com.techpixe.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.entity.Plans;
import com.techpixe.repository.PlansRepository;
import com.techpixe.service.PlansService;

@Service
public class PlansServiceImpl implements PlansService
{
	@Autowired
	PlansRepository plansRepository;
	
	@Override
	public Plans savePlan(Long planTypeId, String planType, double actualPrice, Double discountPercentage, String validity) {
	    Plans plans = new Plans();
	    plans.setPlanTypeId(planTypeId);
	    plans.setPlanType(planType);
	    plans.setActualPrice(actualPrice);
	    plans.setValidity(validity);

	    // Validate discount percentage
	    if (discountPercentage == null || discountPercentage <= 0) {
	        plans.setOfferPrice(Math.round(actualPrice)); // No discount, save as rounded actual price
	        plans.setDiscountPercentage(0.0);
	    } else {
	        if (discountPercentage > 100) {
	            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
	        }

	        double discountAmount = (actualPrice * discountPercentage) / 100;
	        double finalPrice = actualPrice - discountAmount;
	        plans.setOfferPrice((double) Math.round(finalPrice)); // Round to nearest integer
	        plans.setDiscountPercentage(discountPercentage);
	    }

	    // Save and return the plan
	    return plansRepository.save(plans);
	}

	


	@Override
	public List<Plans> allPlans() {
		List<Plans> allPlans =  plansRepository.findAll();
		if (allPlans==null)
		{
			throw new ResponseStatusException(HttpStatus.OK,"No Plans Found");
		}
		return allPlans;
	}

	@Override
	public Plans getPlanByName(String planType) {
	    try {
	        Plans plan = plansRepository.findByPlanType(planType);
	        if (plan == null) {
	            throw new ResponseStatusException(HttpStatus.OK,"Plan with type '" + planType + "' not found.");
	        }
	        return plan;
	    } catch (Exception e) {
	        // Handle other possible exceptions, such as database issues
	        throw new RuntimeException("Error occurred while fetching plan: " + e.getMessage(), e);
	    }
	}
	
	
	public Plans fetchByPlanId(Long planId)
	{
		return plansRepository.findById(planId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Plan is not found"));
	}

	@Override
	public void deleteByPlanType(Long planId) 
	{
		
		plansRepository.deleteById(planId);
	}
	



	
	
	@Override
	public Optional<Plans> updatePlanByType(Long planTypeId, String planType, Double actualPrice, Double discountPercentage,
	                                        String validity) {
	    return plansRepository.findByPlanTypeId(planTypeId).map(existingPlan -> {
	        // Update planType if provided, otherwise keep the existing one
	        existingPlan.setPlanType(planType != null ? planType : existingPlan.getPlanType());

	        // Update actualPrice if provided and greater than 0, otherwise keep the existing one
	        if (actualPrice != null && actualPrice > 0) {
	            existingPlan.setActualPrice(actualPrice);
	        }

	        // Update validity if provided, otherwise keep the existing one
	        existingPlan.setValidity(validity != null ? validity : existingPlan.getValidity());

	        // Validate and update discount percentage
	        if (discountPercentage == null || discountPercentage <= 0) {
	            existingPlan.setOfferPrice(Math.round(existingPlan.getActualPrice())); // No discount, save as rounded actual price
	            existingPlan.setDiscountPercentage(0.0);
	        } else {
	            if (discountPercentage > 100) {
	                throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
	            }

	            double discountAmount = (existingPlan.getActualPrice() * discountPercentage) / 100;
	            double finalPrice = existingPlan.getActualPrice() - discountAmount;
	            existingPlan.setOfferPrice((double) Math.round(finalPrice)); // Round to nearest integer
	            existingPlan.setDiscountPercentage(discountPercentage);
	        }

	        // Save the updated plan
	        return plansRepository.save(existingPlan);
	    });
	}

}
