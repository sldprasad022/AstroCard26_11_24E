
package com.techpixe.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;
import com.techpixe.dto.InvoiceDTO;
import com.techpixe.dto.PaymentOrderDetailsDTO;
import com.techpixe.entity.ShipmentStatusResponse;
import com.techpixe.entity.ShiprocketOrderRequest;
import com.techpixe.service.ShiprocketOrderService;

@RestController
@RequestMapping("/api/shiprocket")
public class ShiprocketOrderController {

    @Autowired
    private ShiprocketOrderService shiprocketOrderService;
    
    
    
    @Autowired
    private RestTemplate restTemplate;

    
    @GetMapping("/track/{shipmentId}")
    public ResponseEntity<ShipmentStatusResponse> getShipmentStatus(@PathVariable String shipmentId)
    {
        ShipmentStatusResponse tracked =  shiprocketOrderService.getShipmentStatus(shipmentId);
        return new ResponseEntity<ShipmentStatusResponse>(tracked,HttpStatus.OK);
    }
    
 // Method to get shipment status by userProfileId -----*********------------
    @GetMapping("/status/tracking/{userProfileId}")
    public ResponseEntity<List<ShipmentStatusResponse>> getShipmentStatusByUserProfileId(@PathVariable Long userProfileId) {
        try {
            // Call service method to get the shipment status based on userProfileId
            List<ShipmentStatusResponse> shipmentStatusResponses = shiprocketOrderService.getShipmentStatus1(userProfileId);

            // Return the shipment status responses
            return ResponseEntity.ok(shipmentStatusResponses);

        } catch (RuntimeException ex) {
            // Handle any errors such as "User profile not found" or "No orders found"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    
    @GetMapping("/tracking1/status1/{userProfileId}")
    public ResponseEntity<List<ShipmentStatusResponse>> getShipmentStatusByUserProfileId1(@PathVariable Long userProfileId) {
        try {
            // Call service method to get the shipment status based on userProfileId
            List<ShipmentStatusResponse> shipmentStatusResponses = shiprocketOrderService.getShipmentStatus2(userProfileId);

            // Return the shipment status responses
            return ResponseEntity.ok(shipmentStatusResponses);

        } catch (RuntimeException ex) {
            // Handle any errors such as "User profile not found" or "No orders found"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    
  //----------Testing-----------with Address also------------based on userId
    @GetMapping("/tracking1/status1/withAddress/basedOnUserId/{userProfileId}")
    public ResponseEntity<List<ShiprocketOrderRequest>> getShipmentStatusByUserProfileIdTesting(@PathVariable Long userProfileId) {
        try {
            // Call service method to get the shipment status based on userProfileId
            List<ShiprocketOrderRequest> shiprocketOrderRequest = shiprocketOrderService.getShipmentStatusTesting(userProfileId);

            // Return the shipment status responses
            return ResponseEntity.ok(shiprocketOrderRequest);

        } catch (RuntimeException ex) {
            // Handle any errors such as "User profile not found" or "No orders found"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    //----------------------All Shipment Statuses ---------------------------------------
    @GetMapping("/getShipmentStatusForAllUsers")
    public ResponseEntity<List<ShiprocketOrderRequest>> getShipmentStatusForAllUsers()
    {
    	List<ShiprocketOrderRequest> getAll = shiprocketOrderService.getShipmentStatusForAllUsers();
    	return new ResponseEntity<List<ShiprocketOrderRequest>>(getAll,HttpStatus.OK);
    }
    
    //-------------------First 10 records only-------------------------------------------
    @GetMapping("/getShipmentStatusForAllUsers/10users")
    public ResponseEntity<Page<ShiprocketOrderRequest>> getShipmentStatusForAllUsers(
            @RequestParam(defaultValue = "0") int page, // Page number
            @RequestParam(defaultValue = "10") int size // Page size (10 records per page)
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ShiprocketOrderRequest> orders = shiprocketOrderService.getShipmentStatusForAllUsers(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    
   
    
    //Final
    @PostMapping("/createCardOrder/{userProfileId}/{planAmount}")
    public ResponseEntity<ShiprocketOrderRequest> createOrder3(@RequestBody ShiprocketOrderRequest request,@PathVariable Long userProfileId,@PathVariable Integer planAmount ) throws JacksonException{
//    	 System.err.println("Controller : "+request.toString());
//    	 System.err.println("Controller ID : "+userProfileId);
    	System.err.println("*****planAmount**********planAmount***********planAmount*******planAmount controller method **"+planAmount);
        ShiprocketOrderRequest response = shiprocketOrderService.createOrder3(request,userProfileId,planAmount);
        return new ResponseEntity<ShiprocketOrderRequest>(response,HttpStatus.CREATED);
    }
    
    
    
    @GetMapping("/fetchBasedOn/shiprocketOrderId/{shiprocketOrderId}")
    public ResponseEntity<ShiprocketOrderRequest> fetchByShiprocketOrderId(@PathVariable Integer shiprocketOrderId)
    {
    	ShiprocketOrderRequest found = shiprocketOrderService.fetchByDataBasedOnShiprocketOrderId(shiprocketOrderId);
    	return new ResponseEntity<ShiprocketOrderRequest>(found,HttpStatus.OK);
    }

    
    
    @DeleteMapping("/deleteShipmentOrder")
    public ResponseEntity<Void> deleteShiprocketOrder(@RequestParam Integer shiprocketOrderId)
    {
    	ShiprocketOrderRequest shiprocketOrderRequest = shiprocketOrderService.fetchByDataBasedOnShiprocketOrderId(shiprocketOrderId);
    	if (shiprocketOrderRequest!=null)
    	{
    		shiprocketOrderService.deleteShipment(shiprocketOrderId);
    		return new ResponseEntity<>(HttpStatus.OK);
		}
    	else 
    	{
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
    
    
    
    @GetMapping("/allSuccessfullOrderDetails")
    public ResponseEntity<List<PaymentOrderDetailsDTO>> findSuccessfullOrderDetails()
    {
    	List<PaymentOrderDetailsDTO> allData = shiprocketOrderService.findSuccessfulPaymentOrderDetails();
    	return new ResponseEntity<List<PaymentOrderDetailsDTO>>(allData,HttpStatus.OK);
    }
    
    @GetMapping("/getInvoice/{userProfileId}")
    public ResponseEntity<InvoiceDTO> fetchInvoice(@PathVariable Long userProfileId)
    {
    	InvoiceDTO fetcheInvoiceData = shiprocketOrderService.fetchInvoice(userProfileId);
    	return new ResponseEntity<InvoiceDTO>(fetcheInvoiceData,HttpStatus.OK);
    }
    

    
    
}

