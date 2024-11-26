package com.techpixe.controller;


import com.techpixe.dto.PaymentResponseDTO;
import com.techpixe.dto.PlanTypeCountsDTO;
import com.techpixe.dto.PlanTypeRevenueDTO;
import com.techpixe.entity.PaymentStatus;
import com.techpixe.entity.Payments;
import com.techpixe.repository.PaymentsRepository;
import com.techpixe.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;
    
    @Autowired
    private PaymentsRepository paymentsRepository;
    
    @PostMapping("/savePayment/{userProfile}")
    public ResponseEntity<Payments> savePayment(@RequestParam Double paymentAmount,@RequestParam Long planTypeId,@RequestParam String planType,@RequestParam String validity,@PathVariable Long userProfile)
    {
    	Payments savePayments = paymentsService.savePayment(paymentAmount, planTypeId, planType,validity, userProfile);
    	return new ResponseEntity<>(savePayments,HttpStatus.CREATED);
    }
    

    
 // Verify the payment
    @PostMapping("/verifyPayment")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> paymentDetails)
    {
            Payments payment = paymentsService.updatePaymentStatus(paymentDetails.get("razorpay_"
            		+ "order_id"),paymentDetails.get("razorpay_payment_id"),paymentDetails.get("razorpay_signature"));
            //System.err.println("Success");
            return ResponseEntity.ok("Payment is valid and updated successfully.");

    }
    
    
  @GetMapping("/paymentHistory/successOrFailure/{userProfileId}")
  public ResponseEntity<List<Payments>> getSuccessfulPayments(@PathVariable Long userProfileId) {
      List<Payments> successfulPayments = paymentsService.getPaymentHistory(userProfileId);
      return ResponseEntity.ok(successfulPayments);
  }

    //Admin Dashboard
    
    @GetMapping("/totalActiveUsersCount")
    public ResponseEntity<Long> totalActiveUsersCount()
    {
    	long totalActiveUsersCount = paymentsService.totalActiveUsersCount("Success");
    	return new ResponseEntity<Long>(totalActiveUsersCount,HttpStatus.OK);
    }
    
    @GetMapping("/total-Active-users/count-by-plan")
    public ResponseEntity<PlanTypeCountsDTO> totalUsersCountBasedOnPlan() {
        PlanTypeCountsDTO totalActiveUsers€BasedOnPlan= paymentsService.totalUsersCountBasedOnPlan();
        return new ResponseEntity<PlanTypeCountsDTO>(totalActiveUsers€BasedOnPlan,HttpStatus.OK);
    }
    
    @GetMapping("/total-revenue")
    public PlanTypeRevenueDTO calculateRevenue() {
        return paymentsService.calculateRevenue();
    }
    
    @GetMapping("/userActiveOrNotPlanType/{userProfile}")
    public ResponseEntity<Boolean> isPaymentSuccessAndPlanType(@PathVariable Long userProfile,String planType)
    {
    	boolean fetched = paymentsService.isPaymentSuccessAndPlanType(userProfile, planType);
    	return new ResponseEntity<Boolean>(fetched,HttpStatus.OK);
    }
    
    //-----------------------------------------------------------------------------------------------------------------
    


}

    