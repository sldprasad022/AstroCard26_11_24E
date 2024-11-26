package com.techpixe.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpixe.entity.Payments;
import com.techpixe.entity.UserProfile;

public interface PaymentsRepository extends JpaRepository<Payments, Long>
{

	//List<Payments> findByUserProfile(UserProfile userProfile);

	Payments findByRazorPayOrderId(String razorPayOrderId);

	Optional<Payments> findByPaymentId(String paymentId);
	
	
	//Admin Dashboard
	
	Long countByStatus(String status);
	
	@Query("SELECT COUNT(p) FROM Payments p WHERE p.planType = :planType AND p.status = :status")
    Long countActiveUsersByPlanTypeAndStatus(String planType, String status);
	
	@Query("SELECT SUM(p.planType) FROM Payments p WHERE  p.status ='success'")
	Long countActiveUsersBySumOfPlanTypes();
	
	
	// Total payment amount for all payments
    @Query("SELECT SUM(p.paymentAmount) FROM Payments p WHERE p.status = 'Success'")
    Double sumTotalPayments();

    // Total payment amount by plan type
    @Query("SELECT SUM(p.paymentAmount) FROM Payments p WHERE p.planType = :planType AND p.status = 'success'")
    Double sumRevenueByPlanType(String planType);
    
    //Based on Success and Plan Type wheater the user having the permission to access the resource
    List<Payments> findByUserProfile(UserProfile userProfile);
    
    
    //Total Active Users List
	List<Payments> findByStatus(String status);
	

	//--------------
	@Query("SELECT p.planType, p.status, u.userProfileId, u.userName, u.uplaodImage, u.firstName FROM Payments p JOIN p.userProfile u WHERE u.userProfileId = :userProfileId AND p.status = 'Success'")
    List<Object[]> findSuccessfulPaymentsByUserId(@Param("userProfileId") Long userProfileId);
    
    	
    	
    	@Query("SELECT p.planType, p.status, u.userProfileId, u.userName, u.uplaodImage, u.firstName, s.shipping_customer_name " +
    		       "FROM Payments p " +
    		       "JOIN p.userProfile u " +  // Join Payments with UserProfile
    		       "JOIN ShiprocketOrderRequest s ON s.userProfile.userProfileId = u.userProfileId " +  // Join ShiprocketOrderRequest based on userProfileId
    		       "WHERE u.userProfileId = :userProfileId AND p.status = 'Success'")
    		List<Object[]> findSuccessfulPaymentsByUserId1(@Param("userProfileId") Long userProfileId);
    		
    		
    		//Final

    		@Query("SELECT p.planType, p.status, u.userProfileId, u.userName, u.uplaodImage, u.firstName, s.shipping_customer_name " +
    			       "FROM Payments p " +
    			       "JOIN p.userProfile u " +  // Join Payments with UserProfile
    			       "LEFT JOIN ShiprocketOrderRequest s ON s.userProfile.userProfileId = u.userProfileId " +  // Left Join ShiprocketOrderRequest based on userProfileId
    			       "WHERE u.userProfileId = :userProfileId AND p.status = 'Success'")
    			List<Object[]> findSuccessfulPaymentsByUserId2(@Param("userProfileId") Long userProfileId);

    
    //--------------------------For hitting webhook if the user payment status is Success------------
    @Query("SELECT p FROM Payments p WHERE p.status = :status AND p.userProfile.id = :userProfileId")
    List<Payments> findByStatusAndUserProfile(String status, Long userProfileId);
    
    
    List<Payments> findByCreatedAtBeforeAndStatus(LocalDateTime dateTime, String status);



}
