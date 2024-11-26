package com.techpixe.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.techpixe.dto.InvoiceDTO;
import com.techpixe.dto.PaymentOrderDetailsDTO;
import com.techpixe.entity.ShipmentStatusResponse;
import com.techpixe.entity.ShiprocketOrderRequest;

public interface ShiprocketOrderService
{

	 //Final
	 ShiprocketOrderRequest createOrder3(ShiprocketOrderRequest request,Long userProfileId,Integer planAmount)throws JsonProcessingException;
	
	ShipmentStatusResponse getShipmentStatus(String shipmentId);
	
	
	//--------------------********Main*******---------------------------------
	public List<ShipmentStatusResponse> getShipmentStatus1(Long userProfileId);
	
	//--------------------With Address also------------------------------------
	public List<ShipmentStatusResponse> getShipmentStatus2(Long userProfileId);
	
	//----------Testing-----------with Address also------------based on userProfileId
	public List<ShiprocketOrderRequest> getShipmentStatusTesting(Long userProfileId);
	
	//----------------All shipment Statuses-----------------------------------
	public List<ShiprocketOrderRequest> getShipmentStatusForAllUsers();

	//---------------First 10 records Only------------------------------------
	Page<ShiprocketOrderRequest> getShipmentStatusForAllUsers(Pageable pageable);
	
	 
	 
	
	 
	 //getOrderId
	 ShiprocketOrderRequest getBasedOnOrderId(String order_id);
	 
	 //Delete Shipment
	// void deleteShipment(String order_id);
	 
	 
	  //Invoice
	  InvoiceDTO fetchInvoice(Long userProfileId);
	  
	  ShiprocketOrderRequest fetchByDataBasedOnShiprocketOrderId(Integer shiprocketOrderId);
	  
	  
	  void deleteShipment(Integer shiprocketOrderId);
	  
	  
	  List<PaymentOrderDetailsDTO> findSuccessfulPaymentOrderDetails();
	  
	  
	
}
