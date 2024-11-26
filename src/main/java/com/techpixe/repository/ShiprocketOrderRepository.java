package com.techpixe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.techpixe.dto.PaymentOrderDetailsDTO;
import com.techpixe.entity.ShiprocketOrderRequest;
import com.techpixe.entity.UserProfile;

@Repository
public interface ShiprocketOrderRepository extends JpaRepository<ShiprocketOrderRequest, Long>
{
	ShiprocketOrderRequest findByShipmentId(String shipmentId);

	List<ShiprocketOrderRequest> findByUserProfile(UserProfile userProfile);
	
	Page<ShiprocketOrderRequest> findAll(Pageable pageable);
	
	ShiprocketOrderRequest findByUserProfileUserProfileId(Long userProfileId);
	
	@Query("SELECT s FROM ShiprocketOrderRequest s WHERE s.order_id = :order_id")
    ShiprocketOrderRequest findByOrderId(@Param("order_id") String order_id);
	
	
	ShiprocketOrderRequest findByShiprocketOrderId(Integer shiprocketOrderId);
	
	
	@Modifying
    @Transactional
    @Query("DELETE FROM ShiprocketOrderRequest s WHERE s.shiprocketOrderId = :shiprocketOrderId")
    void deleteByShiprocketOrderId(@Param("shiprocketOrderId") Integer shiprocketOrderId);

	
	
	//
	@Query("SELECT new com.techpixe.dto.PaymentOrderDetailsDTO(s.invoice, s.label, s.orderDate, s.shiprocketOrderId, s.shipping_customer_name, s.order_id) " +
	           "FROM Payments p " +
	           "JOIN ShiprocketOrderRequest s ON p.userProfile.userProfileId = s.userProfile.userProfileId " +
	           "WHERE p.status = 'Success'")
	    List<PaymentOrderDetailsDTO> findSuccessfulPaymentOrderDetails();
	
}
