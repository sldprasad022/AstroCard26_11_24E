package com.techpixe.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.techpixe.entity.Payments;
import com.techpixe.entity.UserProfile;
import com.techpixe.dto.PaymentResponseDTO;
import com.techpixe.dto.PlanTypeCountsDTO;
import com.techpixe.dto.PlanTypeRevenueDTO;
import com.techpixe.entity.PaymentStatus; // Import the PaymentStatus enum
import com.techpixe.repository.PaymentsRepository;
import com.techpixe.repository.UserProfileRepository;
import com.techpixe.service.PaymentsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private static final String RAZORPAY_KEY_ID = "rzp_test_P7eTEWTbR1y2Sm"; // Replace with your actual key ID
    private static final String RAZORPAY_SECRET_KEY = "gFMj8IVEIJuIKBOEeqRzslPt"; // Replace with your actual secret key

    @Override
    public Payments savePayment(Double paymentAmount, Long planTypeId, String planType,String validity, Long userProfile) {
        UserProfile userProfile2 = userProfileRepository.findById(userProfile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this Id " + userProfile + " is not Found"));
        
        boolean userDataExists =  userProfileRepository.isUserProfileComplete(userProfile);

        try {
            RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_SECRET_KEY);

            // Create RazorPayObject
            JSONObject orderRequest = new JSONObject();
            //orderRequest.put("amount", paymentAmount * 100); // Amount in paise
            orderRequest.put("amount", (int) (paymentAmount * 100)); // Cast to integer

            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + userProfile + "_" + System.currentTimeMillis());

            Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            String razorpayOrderId = razorpayOrder.get("id");

            // Save payment details
            Payments payment = new Payments();
            payment.setPaymentAmount(paymentAmount);
            
            payment.setRazorPayOrderId(razorpayOrderId);
            payment.setUserProfile(userProfile2);
            payment.setStatus("Pending"); // Set status to PENDING initially
            //System.err.println("*****************************"+PaymentStatus.PENDING+"*******************");
            payment.setPlanTypeId(planTypeId);
            payment.setPlanType(planType);
            payment.setValidity(validity);
            payment.setCreatedAt(LocalDateTime.now());
            
            payment.setUserDataExists(userDataExists);
            
            //System.err.println("********First time Data present or Not or second time******"+userDataExists);


            return paymentsRepository.save(payment);

        } catch (RazorpayException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating Razorpay order: " + e.getMessage());
        }
    }


    

    

	@Override
    public Payments updatePaymentStatus(String razorPayOrderId,String paymentId,String signature) {
        Payments payment = paymentsRepository.findByRazorPayOrderId(razorPayOrderId);
        if (payment == null) {
        	Payments payments1 = new Payments();
        	payments1.setStatus("Failure");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "razorPayOrder with this ID " + razorPayOrderId + " not found");
        }
        payment.setStatus("Success");
        //System.err.println("******************Impl "+status+"**************************");
        payment.setPaymentId(paymentId);
        payment.setSignature(signature);
        return paymentsRepository.save(payment);
    }

    public List<Payments> getPaymentHistory(Long userProfileId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this Id " + userProfileId + " is not Found"));
        return paymentsRepository.findByUserProfile(userProfile);
    }




    // Admin Dashboard

	@Override
	public Long totalActiveUsersCount(String status) 
	{
		try
		{
			return  paymentsRepository.countByStatus(status);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0L;
		}
	}


	public PlanTypeCountsDTO totalUsersCountBasedOnPlan() {
        PlanTypeCountsDTO planTypeCountsDTO = new PlanTypeCountsDTO();

        try {
        	//planTypeCountsDTO.setTotalSales(paymentsRepository.countActiveUsersBySumOfPlanTypes());
        	//planTypeCountsDTO.setTotalActiveUsers(paymentsRepository.countByStatus("Success"));
            planTypeCountsDTO.setTotalCardUsersCount(paymentsRepository.countActiveUsersByPlanTypeAndStatus("Card", "Success"));
            planTypeCountsDTO.setTotalWebUsersCount(paymentsRepository.countActiveUsersByPlanTypeAndStatus("Web", "Success"));
            planTypeCountsDTO.setTotalCardAndWebUsersCount(paymentsRepository.countActiveUsersByPlanTypeAndStatus("Card and Web", "Success"));
            
        } catch (Exception e) {
            planTypeCountsDTO.setTotalCardUsersCount(0L);
            planTypeCountsDTO.setTotalWebUsersCount(0L);
            planTypeCountsDTO.setTotalCardAndWebUsersCount(0L);
        }

        return planTypeCountsDTO;
    }
	
	
	public PlanTypeRevenueDTO calculateRevenue() {
        PlanTypeRevenueDTO revenueDTO = new PlanTypeRevenueDTO();

        // Calculate total payment amount for all payments
        revenueDTO.setTotalPaymentsAmount(paymentsRepository.sumTotalPayments());

        // Calculate total payment amount by plan type
        revenueDTO.setTotalCardRevenue(paymentsRepository.sumRevenueByPlanType("Card"));
        revenueDTO.setTotalWebRevenue(paymentsRepository.sumRevenueByPlanType("Web"));
        revenueDTO.setTotalCardAndWebRevenue(paymentsRepository.sumRevenueByPlanType("Card and Web"));

        return revenueDTO;
    }






	@Override
	public boolean isPaymentSuccessAndPlanType(Long userProfileId,String planType)
	{
		UserProfile userProfile = userProfileRepository.findById(userProfileId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this "+userProfileId+" is not found"));
		
		// Fetch payments associated with the userProfile (Assuming a user can have multiple payments)
		List<Payments> paymentsList = paymentsRepository.findByUserProfile(userProfile);
		
		for (Payments payments : paymentsList)
		{
			if ("Success".equals(payments.getStatus())&& planType.equals(payments.getPlanType()) )
			{
				//System.err.println(planType+"=="+payments.getPlanType());
				//System.err.println("Success == "+payments.getStatus());
				return true;
			}
		}
		return false;
	}

//----------------------------------------------------------------------------------------------------
	
	

	
	
	
//	 // Scheduled to run daily
//	// Scheduled to run daily
//    @Scheduled(cron = "0 0 0 * * ?") // Adjust the cron expression as needed
//    public void updateExpiredPayments() {
//        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
//        List<Payments> expiredPayments = paymentsRepository.findByCreatedAtBeforeAndStatus(oneYearAgo, "Success");
//
//        for (Payments payment : expiredPayments) 
//        {
//            payment.setStatus("Expired");
//            paymentsRepository.save(payment);
//        }
//
//     // Get the current timestamp
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        //System.out.println("[" + timestamp + "] Expired payments updated: " + expiredPayments.size());
//    }
	
	
	
	 // Scheduled to run daily
		// Scheduled to run daily
	    @Scheduled(cron = "0 0 0 * * ?") // Adjust the cron expression as needed
	    public void updateExpiredPayments() {
	        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
	        List<Payments> expiredPayments = paymentsRepository.findByCreatedAtBeforeAndStatus(oneYearAgo, "Success");

	        for (Payments payment : expiredPayments) 
	        {
//	            payment.setStatus("Expired");
//	            paymentsRepository.save(payment);
	        	if (payment.getValidity().equals("lifetime"))
	        	{
	        		//payment.setStatus("Success");
	        		payment.setStatus(payment.getStatus());
				} 
	        	else 
	        	{
	        		payment.setStatus("Expired");
		            paymentsRepository.save(payment);
				}
	        }

	     // Get the current timestamp
	        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        //System.out.println("[" + timestamp + "] Expired payments updated: " + expiredPayments.size());
	    }
}

