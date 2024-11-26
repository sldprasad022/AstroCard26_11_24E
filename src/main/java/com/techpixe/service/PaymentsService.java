package com.techpixe.service;

import java.util.List;

import com.techpixe.dto.PaymentResponseDTO;
import com.techpixe.dto.PlanTypeCountsDTO;
import com.techpixe.dto.PlanTypeRevenueDTO;
import com.techpixe.entity.PaymentStatus;
import com.techpixe.entity.Payments;
import com.techpixe.entity.UserProfile;

public interface PaymentsService 
{
	public Payments savePayment(Double paymentAmount,Long planTypeId,String planType,String validity,Long userProfile);

	Payments updatePaymentStatus(String razorPayOrderId, String paymentId, String signature);
	
	//Admin Dashboard
	Long totalActiveUsersCount(String status);
	
	public PlanTypeCountsDTO totalUsersCountBasedOnPlan();
	
	public PlanTypeRevenueDTO calculateRevenue();
	
	public boolean isPaymentSuccessAndPlanType(Long  userProfileId,String planType);
	
	public List<Payments> getPaymentHistory(Long userProfileId);
	
	//---------------------------------------------------------------------------------------------

}
