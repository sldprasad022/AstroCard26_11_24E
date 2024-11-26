package com.techpixe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.entity.Plans;
import com.techpixe.service.PlansService;

@RestController
@RequestMapping("/api/plans")
public class PlansController 
{
	@Autowired
	PlansService plansService;
	
	@PostMapping("/savePlan")
	public ResponseEntity<Plans> savePlan(@RequestParam Long planTypeId,@RequestParam String planType,@RequestParam double actualPrice,@RequestParam(required = false) Double discountPercentage,@RequestParam String validity)
	{
		Plans savedPlan = plansService.savePlan(planTypeId,planType, actualPrice, discountPercentage,validity);
		return new ResponseEntity<Plans>(savedPlan,HttpStatus.CREATED);
	}
	
	@GetMapping("/fetchAllPlans")
	public ResponseEntity<List<Plans>> allPlans()
	{
		List<Plans> all = plansService.allPlans();
		return new ResponseEntity<List<Plans>>(all,HttpStatus.OK);
	}
	
	@GetMapping("/fetchByCardPrice")
	public ResponseEntity<Plans> fetchByPlanType()
	{
		Plans fetchAll = plansService.getPlanByName("Card");
		return new ResponseEntity<Plans>(fetchAll,HttpStatus.OK);
	}
	
	@GetMapping("/fetchByWebPrice")
	public ResponseEntity<Plans> fetchByPlanType1()
	{
		Plans fetchAll = plansService.getPlanByName("Web");
		return new ResponseEntity<Plans>(fetchAll,HttpStatus.OK); 
	}
	
	@GetMapping("/fetchByWebCardPrice")
	public ResponseEntity<Plans> fetchByPlanType2()
	{
		Plans fetchAll = plansService.getPlanByName("Card and Web");
		return new ResponseEntity<Plans>(fetchAll,HttpStatus.OK); 
	}
	
	 @PutMapping("/updatePlansPrice/{planTypeId}")
	    public ResponseEntity<Optional<Plans>> updatePlan(
	    	@PathVariable Long planTypeId,
	        @RequestParam String planType,
	        @RequestParam(required = false) Double actualPrice,
	        @RequestParam(required = false) Double discountPercentage,
	        @RequestParam(required = false) String validity
	    ) 
	 {
	
	        
		 Optional<Plans> updatedPlanType = plansService.updatePlanByType(planTypeId,planType, actualPrice, discountPercentage,validity);
		 return new ResponseEntity<Optional<Plans>>(updatedPlanType,HttpStatus.OK);
	 }
	 
	 
	 //Based on String we performed delete Operation
	 @DeleteMapping("/delete/{planId}")
	    public ResponseEntity<Void> deletePlansByPlanType(@PathVariable Long planId) 
	 	{
			 Plans fetchedPlan = plansService.fetchByPlanId(planId);
			 if (fetchedPlan==null) 
			 {
				  return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
				 //throw new ResponseStatusException(HttpStatus.OK,"Plan with type '" + planType + "' not found.");
			 }
			 else 
			 {
				 plansService.deleteByPlanType(planId);
				 return new ResponseEntity<Void>(HttpStatus.OK);
			 }
	    }
}
