package com.techpixe.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpixe.dto.InvoiceDTO;
import com.techpixe.dto.PaymentOrderDetailsDTO;
import com.techpixe.entity.OrderItem;
import com.techpixe.entity.Plans;
import com.techpixe.entity.ShipmentStatusResponse;
import com.techpixe.entity.ShiprocketOrderRequest;
import com.techpixe.entity.UserProfile;
import com.techpixe.exception.CustomStatusException;
import com.techpixe.repository.OrderItemRepository;
import com.techpixe.repository.PlansRepository;
import com.techpixe.repository.ShiprocketOrderRepository;
import com.techpixe.repository.UserProfileRepository;
import com.techpixe.service.ShiprocketOrderService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class ShiprocketOrderServiceImpl implements ShiprocketOrderService {

	private static final int List = 0;

	@Autowired
	private ShiprocketOrderRepository shiprocketOrderRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ShiprocketAuthService authService;
	
	@Autowired
	private PlansRepository plansRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;


	@Autowired
	private RestTemplate restTemplate;
	
	////////////////////////////////////////////////////////////
	
	private String awbShipmentId;
	
	private Integer courierCompanyId;
	
	private String courierName1;
	
	private String awbCode;
	
	private Double frightCharges;
	
	private Integer custemorShippingPincode;
	
	// 9]] we are getting orderIdForInvoice in the shiprocket api response.Shirocket will give another order Id in the response and also we are generating one order Id and it will be converted int channel Id in the response.
	private Integer orderIdForInvoice;
	
	private String generatedLabel;
	
	private String generatedInvoice;

	public static int generateRandomSixDigitNumber() {
		Random random = new Random();
		// Generate a number between 100000 and 999999
		return 100000 + random.nextInt(900000);
	}

	// --------Correct -2 without custemor address saving-----------------
	// @Override
	public ShiprocketOrderRequest createOrder1(ShiprocketOrderRequest request, Long userProfileId) {

		UserProfile userProfile1 = userProfileRepository.findById(userProfileId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"UserProfile with this " + userProfileId + " is not Found"));

		// Card Measurements
		double cardLength = 8.56;
		double cardBreadth = 0.5;
		double CardHeight = 5.4;
		double cardWeight = 0.005;

		// here, i am giving our company pick up Location Id hyderabad : 6304761

		if (userProfile1 != null) {
			//System.err.println("Service Impl : " + request.toString());

			// Create JSON object to hold request body data
			JSONObject orderData = new JSONObject();

			int orderId1 = generateRandomSixDigitNumber();
			orderData.put("order_id", orderId1);

			// orderData.put("order_date", request.getOrderDate());

			String TOKEN = authService.getToken();

			//System.err.println("*************TOKEN*****************" + TOKEN);

			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			String formattedDate = localDateTime.format(formatter);
			//System.err.println("!!!!!!!!!!!!" + formattedDate + "!!!!!!!!!!!!!!");
			orderData.put("order_date", formattedDate);

			orderData.put("pickup_location", request.getpickup_location());

			// ********* Default value = Custom *********
			orderData.put("channel_id", "Custom");
			orderData.put("comment", request.getComment());

			// Billing details
			orderData.put("billing_customer_name", request.getBilling_customer_name());
			orderData.put("billing_last_name", request.getbilling_last_name());
			orderData.put("billing_address", request.getbilling_address());
			orderData.put("billing_address_2", request.getbilling_address_2()); // Optional
			orderData.put("billing_city", request.getbilling_city());
			orderData.put("billing_pincode", request.getBilling_pincode());
			orderData.put("billing_state", request.getbilling_state());
			orderData.put("billing_country", request.getbilling_country());
			orderData.put("billing_email", request.getbilling_email());
			orderData.put("billing_phone", request.getbilling_phone());

			// Shipping details (only if shipping is different from billing)
			orderData.put("shipping_is_billing", request.isshipping_is_billing());

			orderData.put("shipping_customer_name", request.getshipping_customer_name());
			orderData.put("shipping_last_name", request.getshipping_last_name());
			orderData.put("shipping_address", request.getshipping_address());
			orderData.put("shipping_address_2", request.getshipping_address_2());
			orderData.put("shipping_city", request.getshipping_city());
			orderData.put("shipping_pincode", request.getShipping_pincode());
			orderData.put("shipping_state", request.getshipping_state());
			orderData.put("shipping_country", request.getshipping_country());
			orderData.put("shipping_email", request.getshipping_email());
			orderData.put("shipping_phone", request.getshipping_phone());

//	                orderData.put("shipping_customer_name", cardAddressUsersData.getShipping_customer_name());
//	                orderData.put("shipping_last_name", cardAddressUsersData.getShipping_last_name());
//	                orderData.put("shipping_address", cardAddressUsersData.getShipping_address());
//	                orderData.put("shipping_address_2", cardAddressUsersData.getShipping_address_2());
//	                orderData.put("shipping_city", cardAddressUsersData.getShipping_city());
//	                orderData.put("shipping_pincode", cardAddressUsersData.getShipping_pincode());
//	                orderData.put("shipping_state", cardAddressUsersData.getShipping_state());
//	                orderData.put("shipping_country", cardAddressUsersData.getShipping_country());
//	                orderData.put("shipping_email", cardAddressUsersData.getShipping_email());
//	                orderData.put("shipping_phone", cardAddressUsersData.getShipping_phone());

			// Adding order items
			JSONArray orderItemsArray = new JSONArray();
			for (OrderItem item : request.getorder_items()) {
				JSONObject itemData = new JSONObject();
				itemData.put("name", item.getName());
				// itemData.put("name", "Astro Card");
				itemData.put("sku", item.getSku());
				itemData.put("units", item.getUnits());
				itemData.put("selling_price", item.getSelling_price());
				itemData.put("hsn", item.getHsn());
				orderItemsArray.put(itemData);
			}
			orderData.put("order_items", orderItemsArray);

			// Payment and other details
			// with out hard coding------------
//	            orderData.put("payment_method", request.getpayment_method());
//	            orderData.put("sub_total", request.getsub_total());
//	            orderData.put("length", request.getLength());
//	            orderData.put("breadth", request.getBreadth());
//	            orderData.put("height", request.getHeight());
//	            orderData.put("weight", request.getWeight());

			// here we hard coded the values.----------
			orderData.put("payment_method", request.getpayment_method());
			orderData.put("sub_total", request.getSub_total());
			orderData.put("length", cardLength);
			orderData.put("breadth", cardBreadth);
			orderData.put("height", CardHeight);
			orderData.put("weight", cardWeight);

			// orderData.put("userProfile",request.getUserProfile());

			// Send POST request to Shiprocket API
			String url = "https://apiv2.shiprocket.in/v1/external/orders/create/adhoc";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + TOKEN);

			HttpEntity<String> entity = new HttpEntity<>(orderData.toString(), headers);
			//System.err.println("json data 44" + entity);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			// Handle the API response
			if (response.getStatusCode() == HttpStatus.OK) {
//				System.err.println("Response Status: " + response.getStatusCode());
//				System.err.println("Order created successfully: " + response.getBody());

				ObjectMapper objectMapper = new ObjectMapper();

				Map<String, Object> responseMap;
				try {
					responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
					});
					request.setShipmentId(responseMap.get("shipment_id").toString());
					request.setStatus(responseMap.get("status").toString());

					// request.setStatusCode(responseMap.get("status_code")!=null ?
					// responseMap.get("status_code").toString() : null);

					if (responseMap.get("status_code") != null) {
						request.setStatusCode(Integer.parseInt(responseMap.get("status_code").toString()));
					}

//		                request.setTrackingUrl(responseMap.get("awb_code") != null ? responseMap.get("awb_code").toString() : null);
//		                request.setCourierCompany(responseMap.get("courier_company_id") != null ? responseMap.get("courier_company_id").toString() : null);
//
//					request.setAwbCode(
//							responseMap.get("awb_code") != null ? responseMap.get("awb_code").toString() : null);
//					request.setCourierCompanyId(responseMap.get("courier_company_id") != null
//							? responseMap.get("courier_company_id").toString()
//							: null);

					if (responseMap.get("onboarding_completed_now") != null) {
						request.setOnboardingCompletedNow(
								Integer.parseInt(responseMap.get("onboarding_completed_now").toString()));
					}

					if (responseMap.get("courier_name") != null) {
						request.setCourierName(responseMap.get("courier_name").toString());
					}

					// request.setDeliveryDate();
//		                request.setbilling_address(biilingAddress1);
//		                
//		                request.setbilling_city(billingCity);
//		                
//		                request.setbilling_pincode(billingPincode);
//		                
//		                request.setbilling_state(billingState);
//		                
//		                request.setbilling_country(billingCountry);
//		                
//		                request.setbilling_email(billingEmail);
//		                
//		                request.setbilling_phone(billingPhone);

					request.setOrder_id(String.valueOf(orderId1));

					request.setOrderDate(formattedDate);

					request.setUserProfile(userProfile1);

					request.setchannel_id("Custom");

					request.setLength(cardLength);

					request.setBreadth(cardBreadth);

					request.setHeight(CardHeight);

					request.setWeight(cardWeight);

					// request.setUserProfile(userProfile1.getUserProfileId());
					//System.err.println("*************Service Impl  ID : "+ userProfileId + "--------------" + request.getUserProfile());
									
					return shiprocketOrderRepository.save(request);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				// Save order request in database and return the saved entity

			} else {
				//System.err.println("Failed to create order: " + response.getBody());

				throw new RuntimeException("Failed to create order with Shiprocket. Response: " + response.getBody());
			}
		}

		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"User Profile Id " + userProfileId + " was not found");
		}
		// ************************ we have to modify the return
		// object******************************
		return request;

	}


	

	@Override
	public ShipmentStatusResponse getShipmentStatus(String shipmentId) {

		String TOKEN = authService.getToken();

		String url = "https://apiv2.shiprocket.in/v1/external/courier/track/shipment/" + shipmentId;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + TOKEN);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<ShipmentStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				ShipmentStatusResponse.class);

		ShipmentStatusResponse shipmentStatusResponse = response.getBody();

		// Find ShiprocketOrderRequest by shipmentId
		ShiprocketOrderRequest orderRequest = shiprocketOrderRepository.findByShipmentId(shipmentId);

		if (orderRequest != null && shipmentStatusResponse != null) {
			// Update the status in ShiprocketOrderRequest
			orderRequest.setStatus(shipmentStatusResponse.getStatus()); // Assuming getStatus() returns the new status
																		// from the API
//            orderRequest.setStatusCode(shipmentStatusResponse.getStatusCode());

			// Save the updated order request object
			shiprocketOrderRepository.save(orderRequest);
		}

		return shipmentStatusResponse;
	}

	// --------------------------*********Main*********----------------------------------
	@Override
	public List<ShipmentStatusResponse> getShipmentStatus1(Long userProfileId) {
		String TOKEN = authService.getToken();

		// Retrieve all ShiprocketOrderRequest objects for the given userProfileId
		UserProfile userProfile = userProfileRepository.findById(userProfileId)
				.orElseThrow(() -> new RuntimeException("User profile not found"));

		List<ShiprocketOrderRequest> orders = shiprocketOrderRepository.findByUserProfile(userProfile);

		if (orders.isEmpty()) {
			throw new RuntimeException("No orders found for the provided userProfileId");
		}

		List<ShipmentStatusResponse> statusResponses = new ArrayList<>();

		for (ShiprocketOrderRequest order : orders) {
			String shipmentId = order.getShipmentId();

			if (shipmentId != null && !shipmentId.isEmpty()) {
				// Call the Shiprocket API to get the shipment status
				String url = "https://apiv2.shiprocket.in/v1/external/courier/track/shipment/" + shipmentId;

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + TOKEN);

				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<ShipmentStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
						ShipmentStatusResponse.class);

				ShipmentStatusResponse shipmentStatusResponse = response.getBody();

				if (shipmentStatusResponse != null) {
					// Update the status in ShiprocketOrderRequest
					order.setStatus(shipmentStatusResponse.getStatus()); // Update the status from API
					// order.setStatusCode(shipmentStatusResponse.getStatusCode()); // Uncomment if
					// needed

					// Save the updated order request object
					shiprocketOrderRepository.save(order);

					// Add to the response list
					statusResponses.add(shipmentStatusResponse);
				}
			}
		}

		return statusResponses;
	}

	// -----------------------With Address
	// Also------------------------------------------
	@Override
	public List<ShipmentStatusResponse> getShipmentStatus2(Long userProfileId) {

		String TOKEN = authService.getToken();

		// Retrieve all ShiprocketOrderRequest objects for the given userProfileId
		UserProfile userProfile = userProfileRepository.findById(userProfileId)
				.orElseThrow(() -> new RuntimeException("User profile not found"));

		List<ShiprocketOrderRequest> orders = shiprocketOrderRepository.findByUserProfile(userProfile);

		if (orders.isEmpty()) {
			throw new RuntimeException("No orders found for the provided userProfileId");
		}

		List<ShipmentStatusResponse> statusResponses = new ArrayList<>();

		for (ShiprocketOrderRequest order : orders) {
			String shipmentId = order.getShipmentId();

			if (shipmentId != null && !shipmentId.isEmpty()) {
				// Call the Shiprocket API to get the shipment status
				String url = "https://apiv2.shiprocket.in/v1/external/courier/track/shipment/" + shipmentId;

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + TOKEN);

				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<ShipmentStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
						ShipmentStatusResponse.class);

				ShipmentStatusResponse shipmentStatusResponse = response.getBody();

				if (shipmentStatusResponse != null) {
					// Update the status in ShiprocketOrderRequest
					order.setStatus(shipmentStatusResponse.getStatus()); // Update the status from API
					// order.setStatusCode(shipmentStatusResponse.getStatusCode()); // Uncomment if
					// needed

					// Save the updated order request object
					shiprocketOrderRepository.save(order);

					// Add to the response list
					statusResponses.add(shipmentStatusResponse);
				}
			}
		}

		return statusResponses;
	}

	// ----------Testing-----------with Address also------------
	@Override
	public List<ShiprocketOrderRequest> getShipmentStatusTesting(Long userProfileId) {

		String TOKEN = authService.getToken();

		// Retrieve all ShiprocketOrderRequest objects for the given userProfileId
		UserProfile userProfile = userProfileRepository.findById(userProfileId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found"));

		List<ShiprocketOrderRequest> orders = shiprocketOrderRepository.findByUserProfile(userProfile);

		if (orders.isEmpty()) {
			throw new RuntimeException("No orders found for the provided userProfileId");
		}

		List<ShiprocketOrderRequest> statusResponses = new ArrayList<>();

		for (ShiprocketOrderRequest order : orders) {
			String shipmentId = order.getShipmentId();

			if (shipmentId != null && !shipmentId.isEmpty()) {
				// Call the Shiprocket API to get the shipment status
				String url = "https://apiv2.shiprocket.in/v1/external/courier/track/shipment/" + shipmentId;

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + TOKEN);

				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<ShipmentStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
						ShipmentStatusResponse.class);

				ShipmentStatusResponse shipmentStatusResponse = response.getBody();

				if (shipmentStatusResponse != null) {
					// Update the status in ShiprocketOrderRequest
					order.setStatus(shipmentStatusResponse.getStatus()); // Update the status from API
					// order.setStatusCode(shipmentStatusResponse.getStatusCode()); // Uncomment if
					// needed

					// Save the updated order request object
					shiprocketOrderRepository.save(order);

					// Add to the response list
//	                statusResponses.add(shipmentStatusResponse);
				}
			}
		}

		return orders;
	}

	// -------------------------All Shipment
	// statuses------------------------------------
	@Override
	public List<ShiprocketOrderRequest> getShipmentStatusForAllUsers() {

		String TOKEN = authService.getToken();

		// Retrieve all ShiprocketOrderRequest objects in the database
		List<ShiprocketOrderRequest> allOrders = shiprocketOrderRepository.findAll();

		if (allOrders.isEmpty()) {
			//throw new ResponseStatusException(HttpStatus.OK,"No orders found");
			return new ArrayList<>();
		}

		// Loop through each order and update shipment status from the Shiprocket API
		for (ShiprocketOrderRequest order : allOrders) {
			String shipmentId = order.getShipmentId();

			if (shipmentId != null && !shipmentId.isEmpty()) {
				try {
					// Call the Shiprocket API to get the shipment status
					String url = "https://apiv2.shiprocket.in/v1/external/courier/track/shipment/" + shipmentId;

					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + TOKEN);

					HttpEntity<String> entity = new HttpEntity<>(headers);

					ResponseEntity<ShipmentStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
							ShipmentStatusResponse.class);

					ShipmentStatusResponse shipmentStatusResponse = response.getBody();

					if (shipmentStatusResponse != null) {
						// Update the status in ShiprocketOrderRequest based on the API response
						order.setStatus(shipmentStatusResponse.getStatus());
						order.setStatusCode(shipmentStatusResponse.getStatusCode());
					}

				} catch (HttpClientErrorException e) {
					// Handle the error in case of an issue with the API call
					//System.err.println("Error fetching status for shipmentId " + shipmentId + ": " + e.getMessage());
					// Log or handle error appropriately
				}
			}
		}

		// Save the updated order statuses back into the database
		shiprocketOrderRepository.saveAll(allOrders);

		// Return the list of orders with their updated statuses
		return allOrders;
	}

	// --------------First 10 records only-----------------------------------------
	@Override
	public Page<ShiprocketOrderRequest> getShipmentStatusForAllUsers(Pageable pageable) {

		String TOKEN = authService.getToken();

		// Retrieve a page of ShiprocketOrderRequest objects from the database
		Page<ShiprocketOrderRequest> paginatedOrders = shiprocketOrderRepository.findAll(pageable);

		if (paginatedOrders.isEmpty()) {
			throw new RuntimeException("No orders found in the system.");
		}

		// Loop through each order and update shipment status from the Shiprocket API
		for (ShiprocketOrderRequest order : paginatedOrders) {
			String shipmentId = order.getShipmentId();

			if (shipmentId != null && !shipmentId.isEmpty()) {
				try {
					// Call the Shiprocket API to get the shipment status
					String url = "https://apiv2.shiprocket.in/v1/external/courier/track/shipment/" + shipmentId;

					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + TOKEN);

					HttpEntity<String> entity = new HttpEntity<>(headers);

					ResponseEntity<ShipmentStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
							ShipmentStatusResponse.class);

					ShipmentStatusResponse shipmentStatusResponse = response.getBody();

					if (shipmentStatusResponse != null) {
						// Update the status in ShiprocketOrderRequest based on the API response
						order.setStatus(shipmentStatusResponse.getStatus());
						order.setStatusCode(shipmentStatusResponse.getStatusCode());
					}

				} catch (HttpClientErrorException e) {
					// Handle the error in case of an issue with the API call
					//System.err.println("Error fetching status for shipmentId " + shipmentId + ": " + e.getMessage());
					// Log or handle error appropriately
				}
			}
		}

		// Save the updated order statuses back into the database
		shiprocketOrderRepository.saveAll(paginatedOrders);

		// Return the paginated list of orders with their updated statuses
		return paginatedOrders;
	}





	//Final
//	@Override
//	public ShiprocketOrderRequest createOrder3(ShiprocketOrderRequest request, Long userProfileId) throws JsonProcessingException {
//		//System.err.println("Condition -1" + " " + request.toString());
//
//		UserProfile userProfile1 = userProfileRepository.findById(userProfileId)
//				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//						"UserProfile with this " + userProfileId + " is not Found"));
//		
//		 Plans fetchedCardPrice = plansRepository.findByPlanType("Card");
//		 
//		 System.err.println("fetched Card price and Selling Price *********** "+fetchedCardPrice.getOfferPrice());
//
//		// Card Measurements
//		double cardLength = 8.56;
//		double cardBreadth = 0.5;
//		double CardHeight = 5.4;
//		double cardWeight = 0.005;
//		
//		String pickupLocation = "Office";
//		
//		String  comment="Techpixe";
//		
//		
//		//Shipping
//		String shipping_customer_name="Techpixe";
//		String shipping_last_name="Techpixe Bharatshop";
//		String shipping_address="#8-3-231/F/33,Sri Krishna Nagar,Yousufguda,Hyderabad - 500045";
//		String shipping_address_2="#8-3-231/F/33,Sri Krishna Nagar,Yousufguda,Hyderabad - 500045";
//		String shipping_city="Hyderabad";
//		int shipping_pincode=500045;
//		String shipping_state="Telangana";
//		String shipping_country="India";
//		String shipping_email="info@techpixe.com";
//		String shipping_phone="7989119126";
//		
//		
//		String payment_method="Prepaid";
//		
//		int sub_total=(int) fetchedCardPrice.getOfferPrice();
//		
//		
//		//Order Items
//		String name="Astro Card";
//		String sku=" Bharatastrocard";//Stock Keeping Unit
//		int units=1000; //we put 1000 number as a example
//		
//		//int selling_price=(int) fetchedCardPrice.getOfferPrice();
//		
//		//int selling_price=(int) (fetchedCardPrice.getOfferPrice()+frightCharges);
//		
//		//************************************************************************************************************************************
//		
//		 CustemorbillingPincode =  request.getBilling_pincode();
//		 
//		 
//	    checkingCourierServiceAvailability();
//	    
//	    
//	    
//	    System.err.println("*****frightCharges****************frightCharges******frightCharges 880 ****"+frightCharges);
//		
//		int selling_price = (int) Math.round(fetchedCardPrice.getOfferPrice() + frightCharges);
//		System.err.println("Selling Price (Rounded): " + selling_price);
//
//	    
//	    
//	    //************************************************************************************************************************************
//	    
//		
//		
//		
//		
//		
//		
//
//		// here, i am giving our company pick up Location Id hyderabad : 6304761
//
//		if (userProfile1 != null) {
//
//			Map<String, Object> orderData = new HashMap<>();
//
//			// JSONObject orderData = new JSONObject();
//
//			int orderId1 = generateRandomSixDigitNumber();
//			orderData.put("order_id", orderId1);
//
//			//System.err.println(orderData.put("order_id", orderId1));
//
//			// orderData.put("order_date", request.getOrderDate());
//
//			String TOKEN = authService.getToken();
//
//			System.err.println("*************TOKEN*****************" + TOKEN);
//
//			LocalDateTime localDateTime = LocalDateTime.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//			String formattedDate = localDateTime.format(formatter);
//			
//			//System.err.println("!!!!!!!!!!!!" + formattedDate + "!!!!!!!!!!!!!!");
//			
//			orderData.put("order_date", formattedDate);
//
//			orderData.put("pickup_location", pickupLocation);
//
//			// ********* Default value = Custom *********
//			orderData.put("channel_id", "Custom");
//			orderData.put("comment", comment);
//
//
//			// Billing details
//			orderData.put("billing_customer_name", request.getBilling_customer_name());
//			
//			//System.err.println(orderData.put("billing_customer_name", request.getBilling_customer_name()));
//			
//			orderData.put("billing_last_name", request.getBilling_customer_name());
//			orderData.put("billing_address", request.getbilling_address());
//			orderData.put("billing_address_2", request.getbilling_address_2()); // Optional
//			orderData.put("billing_city", request.getbilling_city());
//			orderData.put("billing_pincode", request.getBilling_pincode());
//			orderData.put("billing_state", request.getbilling_state());
//			orderData.put("billing_country", request.getbilling_country());
//			orderData.put("billing_email", request.getbilling_email());
//			
//			//*********************************************************************************************************
//			
//	
//			
//			//*********************************************************************************************************
//			
//
//			//System.err.println(orderData.put("billing_email", request.getbilling_email()));
//
//			orderData.put("billing_phone", request.getbilling_phone());
//
//			// Shipping details (only if shipping is different from billing)
//			orderData.put("shipping_is_billing", false);
//
//			orderData.put("shipping_customer_name", shipping_customer_name);
//			orderData.put("shipping_last_name", shipping_last_name);
//			orderData.put("shipping_address", shipping_address);
//			orderData.put("shipping_address_2", shipping_address_2);
//			orderData.put("shipping_city", shipping_city);
//			orderData.put("shipping_pincode", shipping_pincode);
//			orderData.put("shipping_state", shipping_state);
//			orderData.put("shipping_country", shipping_country);
//			orderData.put("shipping_email", shipping_email);
//			orderData.put("shipping_phone", shipping_phone);
//
//			// System.err.println("955" + " " +request.getorder_items());
//			// Adding order items
//			
//			
//			List<Map<String, Object>> orderItems = new ArrayList<>();
//			Map<String, Object> itemData = new HashMap<>();
//			//JSONObject itemData = new JSONObject();
//			itemData.put("name", name);
//			itemData.put("sku", sku);
//			itemData.put("units", units);
//			itemData.put("selling_price", selling_price);
//			
//			orderItems.add(itemData);
//			orderData.put("order_items", orderItems);
//
//			// itemData.put("hsn", "441122");
//			//System.err.println("Item Data: " + itemData);
//
//			// here we hard coded the values.----------
//			orderData.put("payment_method", payment_method);
//			orderData.put("sub_total", sub_total); // this subtotal price will be reflect in ship rocket (Ready to ship :: Payment)
//			//orderData.put("sub_total", selling_price);
//			orderData.put("length", cardLength);
//			orderData.put("breadth", cardBreadth);
//			orderData.put("height", CardHeight);
//			orderData.put("weight", cardWeight);
//
//			// orderData.put("userProfile",request.getUserProfile());
//
//			// Send POST request to Shiprocket API
//			String url = "https://apiv2.shiprocket.in/v1/external/orders/create/adhoc";
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			headers.set("Authorization", "Bearer " + TOKEN);
//			//System.err.println("response 555555555555555555555555 " + orderData);
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderData, headers);
//		    String jsonString = objectMapper.writeValueAsString(entity.getBody());
//
//			
//			objectMapper.readTree(jsonString);
//			//System.err.println("json data " +jsonString);
//
//			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//			//System.err.println("Shiprocket Api response : " + response);
//
//			// Handle the API response
//			if (response.getStatusCode() == HttpStatus.OK) {
//				System.err.println("Response Status: " + response.getStatusCode());
//				System.err.println("Order created successfully: " + response.getBody());
//
//
//				Map<String, Object> responseMap;
//
//				try {
//				    //ShiprocketOrderRequest request = new ShiprocketOrderRequest();
//
//				    responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
//
//				    // Safely set values with null checks
//				    request.setShipmentId(
//				        responseMap.get("shipment_id") != null ? responseMap.get("shipment_id").toString() : null
//				    );
//				    
//				    System.err.println("888");
//				    
//				    //*****************************************************************************************************
//				    
//				    //Please recharge your ShipRocket wallet
//				    
//				    if (responseMap.get("status_code") != null && (int) responseMap.get("status_code") == 350) {
//				        
//				        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Order Creation Failed "); // or throw an exception
//				    }
//				    
//				    
//				    awbShipmentId = responseMap.get("shipment_id").toString();
//				    
//				    //generateAwbNumber(awbShipmentId);
//				    
//				     generateAwbNumber(courierCompanyId);
//				    
//				    
//				 
//				    
//				    //*****************************************************************************************************
//				    
//				    System.err.println("444");
//				    
//				    request.setStatus(
//				        responseMap.get("status") != null ? responseMap.get("status").toString() : null
//				    );
//
//				    // Safely parse the status code if it exists
//				    if (responseMap.get("status_code") != null) {
//				        request.setStatusCode(Integer.parseInt(responseMap.get("status_code").toString()));
//				    }
//
////				    request.setAwbCode(
////				        responseMap.get("awb_code") != null ? responseMap.get("awb_code").toString() : null
////				    );
////				    request.setCourierCompanyId(
////				        responseMap.get("courier_company_id") != null ? responseMap.get("courier_company_id").toString() : null
////				    );
//
//				    if (responseMap.get("onboarding_completed_now") != null) {
//				        request.setOnboardingCompletedNow(
//				            Integer.parseInt(responseMap.get("onboarding_completed_now").toString())
//				        );
//				    }
//
//				    request.setCourierName(
//				        responseMap.get("courier_name") != null ? responseMap.get("courier_name").toString() : null
//				    );
//
//				    request.setUserProfile(userProfile1);
//
//				    //System.err.println("Service Impl ID : " + userProfileId + "--------------" + request.getUserProfile());
//				    
//				 request.setAwbCode(awbCode);  //here we are saving the awbcode from the generateAwbNumber() method.
//				 
//				 request.setCourierCompanyId(courierCompanyId); // here we are saving the courierCompany Id from checkingCourierServiceAvailability() method
//				
//				    
//				request.setOrder_id(String.valueOf(orderId1));
//				    
//				request.setOrderDate(formattedDate);
//				    
//				request.setpickup_location(pickupLocation); 
//				
//				request.setComment(comment);
//                
//                request.setUserProfile(userProfile1);
//                
//                request.setchannel_id("Custom");
//                
//                request.setBilling_customer_name(request.getBilling_customer_name());
//                
//                request.setbilling_last_name(request.getbilling_last_name());
//                
//                request.setbilling_address(request.getbilling_address());
//                
//                request.setbilling_address_2(request.getbilling_address_2());
//                
//                request.setbilling_city(request.getbilling_city());
//                
//                request.setBilling_pincode(request.getBilling_pincode());
//                
//                request.setbilling_state(request.getbilling_state());
//                
//                request.setbilling_country(request.getbilling_country());
//                
//                request.setbilling_email(request.getbilling_email());
//                
//                request.setbilling_phone(request.getbilling_phone());
//                
//                //Shipping
//                request.setshipping_customer_name(shipping_customer_name);
//                
//                request.setshipping_last_name(shipping_last_name);
//                
//                request.setshipping_address(shipping_address);
//                
//                request.setshipping_address_2(shipping_address_2);
//                
//                request.setshipping_city(shipping_city);
//                
//                request.setShipping_pincode(shipping_pincode);
//                
//                request.setshipping_country(shipping_country);
//                
//                request.setshipping_state(shipping_state);
//                
//                request.setshipping_email(shipping_email);
//                
//                request.setshipping_phone(shipping_phone);
//                
//                
//                
//                //request.setorder_items(orderItems);
//                
//                request.setpayment_method(payment_method);
//                
//                request.setSub_total(sub_total);
//                
//                request.setLength(cardLength);
//                
//                request.setBreadth(cardBreadth);
//                
//                request.setHeight(CardHeight);
//                
//                request.setWeight(cardWeight);
//                
//                
//                
//                //Order Item Details
//                OrderItem orderItem = new OrderItem();
//                orderItem.setName(name);
//                orderItem.setSku(sku);
//                orderItem.setUnits(units);
//               // orderItem.setSelling_price((Integer) Math.round(fetchedCardPrice.getOfferPrice()));
//                
//                orderItem.setSelling_price(selling_price);
//                
//                System.err.println("Fetched Card Price and Selling Price :::: "+(int) fetchedCardPrice.getOfferPrice());
//                
//               /// (double) Math.round(fetchedCardPrice.getOfferPrice());
//                
//                orderItemRepository.save(orderItem);
//                
//                
//                
//				    
//				    
//				return shiprocketOrderRepository.save(request);
//
//				} catch (JsonProcessingException e) {
//				    e.printStackTrace();
//				}
//
//			} else {
//				//System.err.println("444444444444444444444444444444444444444444444444");
//				//System.err.println("Failed to create order: " + response.getBody());
//
//				throw new RuntimeException("Failed to create order with Shiprocket. Response: " + response.getBody());
//			}
//		}
//
//		else {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//					"User Profile Id " + userProfileId + " was not found");
//		}
//		
//		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Order Creation Failed");
//
//	}
	
	
	 public String generateUUID()
	 {
		 String PREFIX = "bsastro";
		 
		 AtomicInteger counter = new AtomicInteger(0); 
		 
	        // Get the current date in yyyyMMdd format
	        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

	        // Increment the counter (thread-safe)
	        int incrementNumber = counter.incrementAndGet();

	        // Generate the UUID
	        return PREFIX + currentDate + incrementNumber;
	    }
	
	
	
	//Final
	@Override
	public ShiprocketOrderRequest createOrder3(ShiprocketOrderRequest request, Long userProfileId,Integer planAmount) throws JsonProcessingException {
		//System.err.println("Condition -1" + " " + request.toString());

		UserProfile userProfile1 = userProfileRepository.findById(userProfileId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"UserProfile with this " + userProfileId + " is not Found"));
		 
		// System.err.println("fetched Card price and Selling Price *********** "+fetchedCardPrice.getOfferPrice());

		// Card Measurements
		double cardLength = 8.56;
		double cardBreadth = 0.5;
		double CardHeight = 5.4;
		double cardWeight = 0.005;
		
		String pickupLocation = "Office";
		
		String  comment="Techpixe";
		
		// here, i am giving our company pick up Location Id hyderabad : 6304761
		//Shipping
		String billing_customer_name="Techpixe";
		String billing_last_name="Techpixe Bharatshop";
		String billing_address="#8-3-231/F/33,Sri Krishna Nagar,Yousufguda,Hyderabad - 500045";
		String billing_address_2="#8-3-231/F/33,Sri Krishna Nagar,Yousufguda,Hyderabad - 500045";
		String billing_city="Hyderabad";
		int billing_pincode=500045;
		String billing_state="Telangana";
		String billing_country="India";
		String billing_email="info@techpixe.com";
		String billing_phone="7989119126";
		
		String payment_method="Prepaid";
		int sub_total= planAmount;
		
		
		//Order Items
		String name="Astro Card";
		String sku=" Bharatastrocard";//Stock Keeping Unit
		int units=1; //we put 1000 number as a example
		
		//************************************************************************************************************************************
		
		custemorShippingPincode =  request.getShipping_pincode();
		
		//1]] here, //here we are calling checkingCourierServiceAvailability(); fro getting the lowest fright charge with call_before_delivery =="Available" only.
	    checkingCourierServiceAvailability();
	     
	    		// System.err.println("*****frightCharges****************frightCharges******frightCharges 880 ****"+frightCharges);
		
		int selling_price = (int) Math.round(planAmount + frightCharges);
				//System.err.println("Selling Price (Rounded): " + selling_price);

	    //************************************************************************************************************************************

		if (userProfile1 != null) {

			Map<String, Object> orderData = new HashMap<>();

				// JSONObject orderData = new JSONObject();

			Integer orderId1 = generateRandomSixDigitNumber();
			orderData.put("order_id", orderId1);

			String TOKEN = authService.getToken();

				//System.err.println("*************TOKEN*****************" + TOKEN);

			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			String formattedDate = localDateTime.format(formatter);
			
				//System.err.println("!!!!!!!!!!!!" + formattedDate + "!!!!!!!!!!!!!!");
			
			orderData.put("order_date", formattedDate);

			orderData.put("pickup_location", pickupLocation);

				// ********* Default value = Custom *********
				//orderData.put("channel_id", "Custom");
			
			orderData.put("comment", comment);


				// Billing details
				//System.err.println(orderData.put("billing_customer_name", request.getBilling_customer_name()));
			
			orderData.put("billing_customer_name", billing_customer_name);
			orderData.put("billing_last_name", billing_last_name);
			orderData.put("billing_address", billing_address);
			orderData.put("billing_address_2", billing_address_2); // Optional
			orderData.put("billing_city", billing_city);
			orderData.put("billing_pincode", billing_pincode);
			orderData.put("billing_state", billing_state);
			orderData.put("billing_country",billing_country);
			orderData.put("billing_email", billing_email);
			orderData.put("billing_phone", billing_phone);
			
	
					// Shipping details (only if shipping is different from billing)
			orderData.put("shipping_is_billing", false);

			orderData.put("shipping_customer_name", request.getshipping_customer_name());
			orderData.put("shipping_last_name", request.getshipping_last_name());
			orderData.put("shipping_address", request.getshipping_address());
			orderData.put("shipping_address_2", request.getshipping_address_2());
			orderData.put("shipping_city", request.getshipping_city());
			orderData.put("shipping_pincode", request.getShipping_pincode());
			orderData.put("shipping_state", request.getshipping_state());
			orderData.put("shipping_country", request.getshipping_country());
			orderData.put("shipping_email", request.getshipping_email());
			orderData.put("shipping_phone", request.getshipping_phone());

					// System.err.println("955" + " " +request.getorder_items());
					// Adding order items
			
			
			List<Map<String, Object>> orderItems = new ArrayList<>();
			Map<String, Object> itemData = new HashMap<>();
					//JSONObject itemData = new JSONObject();
			itemData.put("name", name);
			itemData.put("sku", sku);
			itemData.put("units", units);
			itemData.put("selling_price", selling_price);
			
			orderItems.add(itemData);
			orderData.put("order_items", orderItems);

				// itemData.put("hsn", "441122");
				//System.err.println("Item Data: " + itemData);

				// here we hard coded the values.----------
			orderData.put("payment_method", payment_method);
			//orderData.put("sub_total", sub_total); // this subtotal price will be reflect in ship rocket (Ready to ship :: Payment)
			orderData.put("sub_total", selling_price);
			orderData.put("length", cardLength);
			orderData.put("breadth", cardBreadth);
			orderData.put("height", CardHeight);
			orderData.put("weight", cardWeight);

				// orderData.put("userProfile",request.getUserProfile());

				// Send POST request to Shiprocket API
			String url = "https://apiv2.shiprocket.in/v1/external/orders/create/adhoc";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + TOKEN);
				//System.err.println("response 555555555555555555555555 " + orderData);
			ObjectMapper objectMapper = new ObjectMapper();

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderData, headers);
		    String jsonString = objectMapper.writeValueAsString(entity.getBody());

			
			objectMapper.readTree(jsonString);
			
				//System.err.println("Token 22 " +TOKEN);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

				//System.err.println("Shiprocket Api response : " + response);

				// Handle the API response
			if (response.getStatusCode() == HttpStatus.OK) {
					//System.err.println("Response Status: " + response.getStatusCode());
					//here , we are getting shiprtocket Order Id in this response.we are generating order Id and it will be converted in to channel id in the response. shiprocket will give another order Id and we can see that order id in the response.
					//System.err.println("Order created successfully: " + response.getBody());


				Map<String, Object> responseMap;

				try {

				    responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

				    // Safely set values with null checks
				    request.setShipmentId(
				        responseMap.get("shipment_id") != null ? responseMap.get("shipment_id").toString() : null
				    );
				    
				    
				  //here , we are getting shiprtocket Order Id in this response.we are generating order Id and it will be converted in to channel id in the response. shiprocket will give another order Id and we can see that order id in the response.
				    request.setShiprocketOrderId(
				    		responseMap.get("order_id")!=null ? (Integer) responseMap.get("order_id"):null);
				    
				    //*****************************************************************************************************
				    
				    //Please recharge your ShipRocket wallet
				    
				    if (responseMap.get("status_code") != null && (int) responseMap.get("status_code") == 350) {
				        
				        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Order Creation Failed "); // or throw an exception
				    }
				    
				    // 3]] here, we need awbShipmentId for hitting shiprocket api for getting AWB number.that's why we are intializing into global variable.
				    awbShipmentId = responseMap.get("shipment_id").toString();
				    
				    
				     //orderIdForInvoice = (Integer) responseMap.get("order_id");
				    //*********** Shirocket will give another order Id in the response.***************************
				    // 9]] we are getting orderIdForInvoice in the shiprocket api response.Shirocket will give another order Id in the response and also we are generating one order Id and it will be converted int channel Id in the response.
				    orderIdForInvoice = responseMap.get("order_id") != null ? (Integer) responseMap.get("order_id") : 0;
				    
				    
				    // 4]]here, we are passing courier company Id from the Global variable  [(checkingCourierServiceAvailability())].
				    // 5]]here , we are calling the  generateAwbNumber(courierCompanyId); fro getting AWB number.
				     generateAwbNumber(courierCompanyId);
				    
				    
				    //*****************************************************************************************************
				    
				    request.setStatus(
				        responseMap.get("status") != null ? responseMap.get("status").toString() : null
				    );

				    // Safely parse the status code if it exists
				    if (responseMap.get("status_code") != null) {
				        request.setStatusCode(Integer.parseInt(responseMap.get("status_code").toString()));
				    }

				    if (responseMap.get("onboarding_completed_now") != null) {
				        request.setOnboardingCompletedNow(
				            Integer.parseInt(responseMap.get("onboarding_completed_now").toString())
				        );
				    }

				    request.setCourierName(
				        responseMap.get("courier_name") != null ? responseMap.get("courier_name").toString() : null
				    );

				    request.setUserProfile(userProfile1);
				    
				    String uuId = generateUUID();
				    
				    request.setUuId(uuId);
				    
				 request.setAwbCode(awbCode);  //here we are saving the awbcode from the generateAwbNumber() method.
				 
				 request.setCourierCompanyId(courierCompanyId); // here we are saving the courierCompany Id from checkingCourierServiceAvailability() method
				 
				 request.setCourierName(courierName1);// here we are saving the courierCompany name from checkingCourierServiceAvailability() method
				    
				request.setOrder_id(String.valueOf(orderId1));
				    
				request.setOrderDate(formattedDate);
				    
				request.setpickup_location(pickupLocation); 
				
				request.setComment(comment);
                
                request.setUserProfile(userProfile1);
                
                request.setchannel_id("Custom");
                
                request.setBilling_customer_name(billing_customer_name);
                
                request.setbilling_last_name(billing_last_name);
                
                request.setbilling_address(billing_address);
                
                request.setbilling_address_2(billing_address_2);
                
                request.setbilling_city(billing_city);
                
                request.setBilling_pincode(billing_pincode);
                
                request.setbilling_state(billing_state);
                
                request.setbilling_country(billing_country);
                
                request.setbilling_email(billing_email);
                
                request.setbilling_phone(billing_phone);
                
                //Shipping
                request.setshipping_customer_name(request.getshipping_customer_name());
                
                request.setshipping_last_name(request.getshipping_last_name());
                
                request.setshipping_address(request.getshipping_address());
                
                request.setshipping_address_2(request.getshipping_address_2());
                
                request.setshipping_city(request.getshipping_city());
                
                request.setShipping_pincode(request.getShipping_pincode());
                
                request.setshipping_state(request.getshipping_state());
                
                request.setshipping_country(request.getshipping_country());
                
                request.setshipping_email(request.getshipping_email());
                
                request.setshipping_phone(request.getshipping_phone());
                
                request.setFrightCharges(frightCharges); //// here we are saving the fright Charges from checkingCourierServiceAvailability() method
                
                request.setPlanAmount(planAmount);
                
             
                //request.setorder_items(orderItems);
                
                request.setpayment_method(payment_method);
                
                request.setSub_total(sub_total);
                
                request.setLength(cardLength);
                
                request.setBreadth(cardBreadth);
                
                request.setHeight(CardHeight);
                
                request.setWeight(cardWeight);
                
               
                //Order Item Details
                OrderItem orderItem = new OrderItem();
                orderItem.setName(name);
                orderItem.setSku(sku);
                orderItem.setUnits(units);
                
                orderItem.setSelling_price(selling_price);
               
                orderItemRepository.save(orderItem);
                
                //****************************************************************************************************************************
                
                String  shipmentId =awbShipmentId;
                
                List<String> shipmentIds = Arrays.asList(shipmentId);
                
                
                // 7]] here, we are calling getLabel() for getting label of the order but that method needs shipmentId as alist
                getLabelUrl(shipmentIds);

              
                request.setLabel(generatedLabel);
   
               // System.err.println("orderIdForInvoice************"+orderIdForInvoice);
                
                if (orderIdForInvoice == null)
                {
                	//System.err.println("orderIdForInvoice************"+orderIdForInvoice);
                    throw new IllegalArgumentException("Order ID for invoice cannot be null. ****");
                }
                
                
                List<Integer> orderIds = Arrays.asList(orderIdForInvoice);
                
                // 11]] here, we are calling  downloadInvoice(orderIds); for getting INVOICE and we need to pass orderIdForInvoice as list.
                // 12]] we are getting orderIdForInvoice in the shiprocket api response.Shirocket will give another order Id in the response and also we are generating one order Id and it will be converted int channel Id in the response.
                
                downloadInvoice(orderIds);
                
                request.setInvoice(generatedInvoice);
                
              //****************************************************************************************************************************
                   
				return shiprocketOrderRepository.save(request);

				} catch (JsonProcessingException e) {
				    e.printStackTrace();
				}

			} else {
				//System.err.println("444444444444444444444444444444444444444444444444");
				//System.err.println("Failed to create order: " + response.getBody());

				throw new RuntimeException("Failed to create order with Shiprocket. Response: " + response.getBody());
			}
		}

		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Profile Id " + userProfileId + " was not found");
					
		}
		
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Order Creation Failed");

	}


	
	////here we choosing the lowest fright charge with call_before_delivery =="Available" only. 
	public void checkingCourierServiceAvailability() throws JsonMappingException, JsonProcessingException 
	{
		
		System.err.println("****************************CheckingCourierServiceAvailability() Called**********************************");
		
	    Integer pickup_postcode = 500045; // Replace with your pickup postal code
	    Integer delivery_postcode = custemorShippingPincode; // Ensure this is valid and initialized
	    String cod = "0"; // 0 for no cash on delivery
	    String weight = "0.005"; // Weight as a string

	    // Construct the API URL
	    String url = String.format(
	        "https://apiv2.shiprocket.in/v1/external/courier/serviceability/?pickup_postcode=%s&delivery_postcode=%s&cod=%s&weight=%s",
	        pickup_postcode, delivery_postcode, cod, weight
	    );

	    String TOKEN = authService.getToken(); // Fetch the authentication token

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + TOKEN);

	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    try {
	        // Make the API call
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

	            if (responseMap != null && !responseMap.isEmpty()) {
	               // System.err.println("Response Map: :::: " + responseMap);

	                // Extract available courier companies
	                List<Map<String, Object>> courierCompanies = 
	                    (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("data")).get("available_courier_companies");
	                
	                
	                // *****************we have to print list here company with the lowest freight charge and "call_before_delivery" available

	                // Find the courier company with the lowest freight charge and "call_before_delivery" available
	                Optional<Map<String, Object>> lowestFreightCourier = courierCompanies.stream()
	                    .filter(courier -> "Available".equals(courier.get("call_before_delivery")) && courier.get("freight_charge") != null)
	                    .min(Comparator.comparingDouble(courier -> Double.parseDouble(courier.get("freight_charge").toString())));

	                // Handle the result
	                if (lowestFreightCourier.isPresent())
	                {
	                    Map<String, Object> courier = lowestFreightCourier.get();
					                   // System.err.println("Courier with lowest freight charge:");
					                    //System.err.println("Courier Compny name "+courier.get("courier_name"));
				                    	//System.err.println("Courier Company ID *************: " + courier.get("courier_company_id"));
				                    	//System.err.println("Freight Charge ******************: " + courier.get("freight_charge"));
	                    
	                    //*************************************************************************************
	                    				//System.err.print(courier.get("freight_charge"));
	                    
	                    // 2]] here, we are getting fright charges of particular Company and stored it in Global variable
	                    frightCharges =  (Double) courier.get("freight_charge");
	                    				//frightCharges = Double.valueOf(courier.get("freight_charge").toString());
	                    
	                    courierName1 = (String) courier.get("courier_name");
	                    
	                    //2]] here we getting or choosing the lowest fright charge with call_before_delivery =="Available" only and intializing stored it in global variable.
	                    courierCompanyId = (Integer) courier.get("courier_company_id");
	                    
	                    				// System.err.println("*************courierCompanyId*****************"+courierCompanyId);
	                    
	                    ShiprocketOrderRequest shiprocketOrderRequest = new ShiprocketOrderRequest();
	                    
	                    shiprocketOrderRequest.setCourierCompanyId((Integer) courier.get("courier_company_id"));
	                    
	                    
//	                    generateAwbNumber(courierCompanyId);
	                   
	                    //*************************************************************************************
	                    
	                }
	                else
	                {
	                    System.err.println("No courier service matches the criteria.");
	                }
	            } 
	            else
	            {
	                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No Courier Services Available for delivery");
	            }
	        }
	    } catch (HttpClientErrorException e) {
	        //System.err.println("API Error: " + e.getResponseBodyAsString());
	        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Shiprocket API Error: " + e.getMessage());
	    }
	}
	
	
	//here, we need awbShipmentId for hitting shiprocket api for getting AWB number
	public void generateAwbNumber(Integer courierCompanyId) throws JsonProcessingException
	{
					// System.err.println("**********************generateAwbNumber method Called*************************");

	    Map<String, Object> shipmentCourierRequset = new HashMap<>();
	    shipmentCourierRequset.put("shipment_id", awbShipmentId);
	    shipmentCourierRequset.put("courier_id", courierCompanyId);

	    			// System.err.println("**********:::: " + courierCompanyId);

	    String TOKEN = authService.getToken();
	    String url = "https://apiv2.shiprocket.in/v1/external/courier/assign/awb";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + TOKEN);

	    ObjectMapper objectMapper = new ObjectMapper();
	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(shipmentCourierRequset, headers);

	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

	    			//System.err.println("Shiprocket API Response 1234 ****: " + response.getBody());

	    // Check if the response is successful and contains a body
	    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) 
	    {
	        Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

	        		// System.err.println("444444444444" + responseMap);

	        // Check if the response contains a status_code
	        if (responseMap.get("status_code") != null)
	        {
	            Integer statusCode = (Integer) responseMap.get("status_code");
	            String message = (String) responseMap.get("message");

	            if (statusCode == 350)
	            {
	                // Trigger email notification
	                sendRechargeWalletEmail(message);

	                throw new CustomStatusException(350, "Insufficient Shiprocket wallet balance: " + message);
	            }
	        }

	        // Process the response if no error (status_code != 350)
	        if (responseMap.get("response") != null)
	        {
	            Map<String, Object> responseData = (Map<String, Object>) responseMap.get("response");

	            if (responseData != null && responseData.containsKey("data"))
	            {
	                Map<String, Object> dataMap = (Map<String, Object>) responseData.get("data");

	                if (dataMap != null && dataMap.containsKey("awb_code"))
	                {
	                	//*****************************************************************************************
	                	
	                    String awbCode1 = (String) dataMap.get("awb_code"); // Ensure awb_code exists and is a String
	                    
	                    // 6]] here , we are getting AWB number and intializing in to Global Variable.
	                    awbCode = awbCode1;
	                    		//System.err.println("******************AWB Code: " + awbCode1 + "*********************");
	                    
	                    //******************************************************************************************
	                } 
	                else
	                {
	                    throw new RuntimeException("AWB code not found in Shiprocket response data");
	                }
	            }
	            else 
	            {
	                throw new RuntimeException("'data' key is missing in Shiprocket response");
	            }
	        }
	        else
	        {
	            throw new RuntimeException("'response' key is missing in Shiprocket response");
	        }
	    } 
	    else 
	    {
	        throw new RuntimeException("Failed to generate AWB. Response: " + response.getBody());
	    }
	}



	
	

	private void sendRechargeWalletEmail(String message)
	{
	    String toEmail = "bharatastrosage@gmail.com"; // Replace with actual recipient email
	    String subject = "Shiprocket Wallet Recharge Needed for Astrocard";
	    String body = "Dear Admin,\n\n" + message + "\n\nPlease recharge your wallet to proceed with your shipments.\n\nThank you.";

	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setTo(toEmail);
	    email.setSubject(subject);
	    email.setText(body);
	    

	    try
	    {
	        javaMailSender.send(email); // Ensure `javaMailSender` is correctly configured
	        //System.err.println("Recharge wallet email sent successfully.");
	    } 
	    catch (Exception e) 
	    {
	        //System.err.println("Failed to send email: " + e.getMessage());
	    }
	}

	
	// here, we are getting LABEL  of the order. we need to pass list of shipmentId as a input.
	private void getLabelUrl(List<String> shipmentIds) {
	    if (shipmentIds == null || shipmentIds.isEmpty()) {
	        throw new IllegalArgumentException("Shipment ID list cannot be null or empty.");
	    }

	    // Convert shipment IDs to an array of integers
	    List<Integer> shipmentIdList = shipmentIds.stream()
	                                              .map(Integer::valueOf)
	                                              .toList();

	    // Prepare the request payload
	    Map<String, Object> shipmentCourierRequest = new HashMap<>();
	    shipmentCourierRequest.put("shipment_id", shipmentIdList);

	    // Authentication token
	    String TOKEN = authService.getToken();
	    if (TOKEN == null || TOKEN.isEmpty()) {
	        throw new RuntimeException("Failed to retrieve authentication token.");
	    }

	    String url = "https://apiv2.shiprocket.in/v1/external/courier/generate/label";

	    // Set headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + TOKEN);

	    // Create HTTP entity
	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(shipmentCourierRequest, headers);

	    // Call Shiprocket API
	    ResponseEntity<String> response;
	    try {
	        response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	    } catch (Exception e) {
	        throw new RuntimeException("Error while calling Shiprocket API: " + e.getMessage(), e);
	    }

	    		// Log full response body for debugging
	    		//System.err.println("Shiprocket API Response: " + response.getBody());

	    // Check response status and body
	    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
	        try {
	            // Parse the response body to extract `label_url`
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode responseJson = objectMapper.readTree(response.getBody());

	            // Log status code and message
	            if (responseJson.has("status_code")) {
	                int statusCode = responseJson.get("status_code").asInt();
	                String message = responseJson.get("message").asText();
	                		//System.err.println("Status Code: " + statusCode + ", Message: " + message);
	            }

	            // Check if the label was created successfully
	            if (responseJson.has("label_created") && responseJson.get("label_created").asInt() == 1) {
	                // Extract label_url if it exists
	                if (responseJson.has("label_url")) {
	                    String labelUrl = responseJson.get("label_url").asText();
	                    		//System.out.println("Label URL **** : " + labelUrl);
	                    //**************************************************************************
	                    
	                    // 8]] we are getting lable url and stored in to global variable.
	                    generatedLabel = labelUrl;
	                    
	                    //**************************************************************************
	                    // Store the label URL or perform necessary actions
	                    // e.g., save to database or log it
	                   // System.err.println("************* Label URL: " + labelUrl);
	                } else {
	                    throw new RuntimeException("Label URL not found in the response, but label creation was successful.");
	                }
	            } else {
	                // If label creation failed
	                String errorMessage = responseJson.has("message")
	                        ? responseJson.get("message").asText()
	                        : "Unknown error";
	                throw new RuntimeException("Failed to create label: " + errorMessage);
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Error parsing Shiprocket API response: " + e.getMessage(), e);
	        }
	    } else {
	        throw new RuntimeException("Failed to generate label. HTTP Status: " + response.getStatusCode());
	    }
	}
	
	
	private void downloadInvoice(List<Integer> orderIds) {
	    // Validate the orderIds list
	    if (orderIds == null || orderIds.isEmpty()) {
	        throw new IllegalArgumentException("Order ID list cannot be null or empty.");
	    }

	    // Prepare the request payload
	    Map<String, Object> shipmentCourierRequest = new HashMap<>();
	    shipmentCourierRequest.put("ids", orderIds);

	    // Authentication token
	    String TOKEN = authService.getToken();
	    if (TOKEN == null || TOKEN.isEmpty()) {
	        throw new RuntimeException("Failed to retrieve authentication token.");
	    }

	    // Shiprocket API URL for generating invoice
	    String url = "https://apiv2.shiprocket.in/v1/external/orders/print/invoice";

	    // Set headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + TOKEN);

	    // Create the HttpEntity for the request
	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(shipmentCourierRequest, headers);

	    // Make the POST request
	    ResponseEntity<String> response;
	    try {
	        response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	    } catch (Exception e) {
	        throw new RuntimeException("Error while calling Shiprocket API: " + e.getMessage(), e);
	    }

	    	// Log the response
	    	// System.out.println("Shiprocket API Response: " + response.getBody());

	    // Handle the response
	    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
	        try {
	            // Parse the response
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode responseJson = objectMapper.readTree(response.getBody());

	            // Check if the invoice was created
	            if (responseJson.has("is_invoice_created") && responseJson.get("is_invoice_created").asBoolean()) {
	                // Extract the invoice URL
	                if (responseJson.has("invoice_url")) {
	                    String invoiceUrl = responseJson.get("invoice_url").asText();
	                    
	                    	// System.err.println("Invoice URL: ****** " + invoiceUrl);
	                    //***************************************************************************************************************
	                    	
	                    	// here we are getting invoice and stored it in to global variable
	                    	generatedInvoice = invoiceUrl;
	                    
	                    //***************************************************************************************************************

	                    // Handle the invoice URL (e.g., store it or process it)
	                } else {
	                    throw new RuntimeException("Invoice URL not found in the response.");
	                }
	            } else {
	                throw new RuntimeException("Invoice creation failed. Response: " + response.getBody());
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Error parsing Shiprocket API response: " + e.getMessage(), e);
	        }
	    } else {
	        throw new RuntimeException("Failed to generate invoice. HTTP Status: " + response.getStatusCode());
	    }
	}

	
	
	

	
	
	@Override
	public ShiprocketOrderRequest getBasedOnOrderId(String order_id)
	{
		
		ShiprocketOrderRequest shiprocketOrderRequest = shiprocketOrderRepository.findByOrderId(order_id);
		if (shiprocketOrderRequest!=null)
		{
			return shiprocketOrderRequest;
		}
		else 
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"order_id is not Found");
		}
	}

	//Invoiceee
	@Override
	public InvoiceDTO fetchInvoice(Long userProfileId) 
	{
		ShiprocketOrderRequest shiprocketOrderRequest = shiprocketOrderRepository.findByUserProfileUserProfileId(userProfileId);
		if (shiprocketOrderRequest!=null)
		{
			InvoiceDTO invoiceDTO = new InvoiceDTO();

			invoiceDTO.setShipping_customer_name(shiprocketOrderRequest.getshipping_customer_name());
			invoiceDTO.setShipping_last_name(shiprocketOrderRequest.getshipping_last_name());
			invoiceDTO.setShipping_address(shiprocketOrderRequest.getshipping_address());
			invoiceDTO.setShipping_city(shiprocketOrderRequest.getshipping_city());
			invoiceDTO.setShipping_pincode(shiprocketOrderRequest.getShipping_pincode());
			invoiceDTO.setShipping_state(shiprocketOrderRequest.getshipping_state());
			invoiceDTO.setShipping_city(shiprocketOrderRequest.getshipping_city());
			invoiceDTO.setShipping_state(shiprocketOrderRequest.getshipping_state());
			invoiceDTO.setShipping_country(shiprocketOrderRequest.getshipping_country());
			invoiceDTO.setShipping_phone(shiprocketOrderRequest.getshipping_phone());
			invoiceDTO.setShipping_email(shiprocketOrderRequest.getshipping_email());
			
			invoiceDTO.setOrder_id(shiprocketOrderRequest.getOrder_id());
			invoiceDTO.setOrderDate(shiprocketOrderRequest.getOrderDate());
			invoiceDTO.setPlanAmount(shiprocketOrderRequest.getPlanAmount());
			
			invoiceDTO.setFrightCharges(shiprocketOrderRequest.getFrightCharges());
			
			invoiceDTO.setUuId(shiprocketOrderRequest.getUuId());
			
			return invoiceDTO;
			
			
		} 
		else
		{
			//throw new ResponseStatusException(HttpStatus.NOT_FOUND,"userProfileId is not Found");
			return new InvoiceDTO();
		}
	}

	@Override
	public ShiprocketOrderRequest fetchByDataBasedOnShiprocketOrderId(Integer shiprocketOrderId) 
	{
		ShiprocketOrderRequest fetchShiprocketOrderRequest = shiprocketOrderRepository.findByShiprocketOrderId(shiprocketOrderId);
		if (fetchShiprocketOrderRequest!=null)
		{
			return fetchShiprocketOrderRequest;
		}
		else 
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"shiprocketOrderId is not Found"+shiprocketOrderId);
		}
		
	}

	
	@Transactional
	@Override
	public void deleteShipment(Integer shiprocketOrderId)
	{
        String TOKEN = authService.getToken();
        String url = "https://apiv2.shiprocket.in/v1/external/orders/cancel";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        // Create the payload
        Map<String, Object> payload = Collections.singletonMap("ids", Collections.singletonList(shiprocketOrderId));

        // Prepare the HTTP entity with headers and payload
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try 
        {
            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Handle the response
            if (response.getStatusCode() == HttpStatus.OK) 
            {
            	shiprocketOrderRepository.deleteByShiprocketOrderId(shiprocketOrderId);
            	
                		//System.err.println("Order canceled successfully: " + response.getBody());
            } 
            else 
            {
                		//System.err.println("Failed to cancel order. Status: " + response.getStatusCode());
                throw new ResponseStatusException(response.getStatusCode(),"Failed to cancel order");
            }
        }
        catch (Exception e) 
        {
            			//System.err.println("Error while canceling order: " + e.getMessage());
        	String errorMessage = "Unexpected error while canceling the order: " + e.getMessage();
        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }
	
	

	@Override
	public List<PaymentOrderDetailsDTO> findSuccessfulPaymentOrderDetails()
	{
		List<PaymentOrderDetailsDTO> allPaymentSuccessfullOrderDetails= shiprocketOrderRepository.findSuccessfulPaymentOrderDetails();
		if (!allPaymentSuccessfullOrderDetails.isEmpty()) 
		{
						//System.err.println("**allPaymentSuccessfullOrderDetails**"+allPaymentSuccessfullOrderDetails);
			
			return allPaymentSuccessfullOrderDetails;
		}
		else
		{
			return new ArrayList<>();
			//throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No Order Details Found");
		}
	}
	
	

	

}
