package com.techpixe.serviceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.dto.PaymentResponseDTO;
import com.techpixe.dto.UserProfileDTO;
import com.techpixe.dto.UserProfileResponseDTO;
import com.techpixe.dto.UserProfileResponseDTO2;
import com.techpixe.dto.ZodiacDTO;
import com.techpixe.entity.Location;
import com.techpixe.entity.Payments;
import com.techpixe.entity.UserProfile;
import com.techpixe.repository.PaymentsRepository;
import com.techpixe.repository.UserProfileRepository;
import com.techpixe.service.UserProfileService;

import ch.qos.logback.core.joran.conditional.ElseAction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import org.springframework.core.io.FileSystemResource;


@Service
public class UserProfileServiceImpl implements UserProfileService {
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("$(spring.mail.username)")
	private String fromMail;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserProfileResponseDTO save(String userName, String email, String password, String confirmPassword) {
		// Check if the email already exists
		Optional<UserProfile> existingUser = Optional.ofNullable(userProfileRepository.findByEmail(email));

		if (existingUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
		}

		// Check if password and confirmPassword match
		if (!password.equals(confirmPassword)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Password and Confirm Password do not match");
		}

		// Create a new user profile and save it
		UserProfile userProfile = new UserProfile();
		userProfile.setUserName(userName);
		userProfile.setEmail(email);
		userProfile.setPassword(passwordEncoder.encode(password));
		userProfile.setRole("User");

		UserProfile savedUserProfile = userProfileRepository.save(userProfile);
		
		UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
		userProfileResponseDTO.setUserProfileId(savedUserProfile.getUserProfileId());
		userProfileResponseDTO.setUserName(savedUserProfile.getUserName());
		userProfileResponseDTO.setEmail(savedUserProfile.getEmail());
		
		return userProfileResponseDTO;
		
	}


	
	
//	@Override
//	public Optional<UserProfileResponseDTO2> saveForAstrocard(Long userProfileId,String planType, String firstName, String lastName,
//			Long mobileNumber, String dateOfBirth, String timeOfBirth, Long pincode, String location, String style,
//			MultipartFile uploadImage, String language,String gender) {
//
//		return userProfileRepository.findById(userProfileId).map(existingUserProfile -> {
//
//			// Set fields as before
//			existingUserProfile.setPlanType(planType !=null ? planType : existingUserProfile.getPlanType());
//			existingUserProfile.setFirstName(firstName != null ? firstName : existingUserProfile.getFirstName());
//			existingUserProfile.setLastName(lastName != null ? lastName : existingUserProfile.getLastName());
//			existingUserProfile
//					.setMobileNumber(mobileNumber != null ? mobileNumber : existingUserProfile.getMobileNumber());
//			existingUserProfile
//					.setDateOfBirth(dateOfBirth != null ? dateOfBirth : existingUserProfile.getDateOfBirth());
//			existingUserProfile
//					.setTimeOfBirth(timeOfBirth != null ? timeOfBirth : existingUserProfile.getTimeOfBirth());
//			existingUserProfile.setPincode(pincode != null ? pincode : existingUserProfile.getPincode());
//			existingUserProfile.setLocation(location != null ? location : existingUserProfile.getLocation());
//			existingUserProfile.setStyle(style != null ? style : existingUserProfile.getStyle());
//
//			// Convert MultipartFile to byte[] if provided
//			if (uploadImage != null && !uploadImage.isEmpty()) {
//				try {
//					existingUserProfile.setUplaodImage(uploadImage.getBytes());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			existingUserProfile.setLanguage(language != null ? language : existingUserProfile.getLanguage());
//
//			// Get latitude and longitude from pincode
//			GeocodingService geocodingService = new GeocodingService();
//			Location locationObj = geocodingService.getLatLongFromPincode(pincode);
//			if (locationObj != null) {
//				existingUserProfile.setLatitude(locationObj.getLatitude());
//				existingUserProfile.setLongitude(locationObj.getLongitude());
//
//				// Fetch and set the timezone
//				TimezoneService timezoneService = new TimezoneService();
//				String timeZone = timezoneService.getTimeZoneFromLatLong(locationObj.getLatitude(),
//						locationObj.getLongitude());
//				existingUserProfile.setTimeZone(timeZone);
//			}
//
//			// Calculate and set the zodiac sign
//			AstrologyService astrologyService = new AstrologyService();
//			String zodiacSign = astrologyService.getZodiacSign(existingUserProfile.getDateOfBirth());
//			existingUserProfile.setZodiac(zodiacSign);
//			existingUserProfile.setGender(gender !=null ? gender : existingUserProfile.getGender());
//
//		    userProfileRepository.save(existingUserProfile);
//		    
//		    UserProfileResponseDTO2 response = new UserProfileResponseDTO2();
//	        response.setUserProfileId(existingUserProfile.getUserProfileId());
//	        response.setUserName(existingUserProfile.getUserName());
//	        response.setEmail(existingUserProfile.getEmail());
//	        response.setMobileNumber(existingUserProfile.getMobileNumber());
//	        response.setUplaodImage(existingUserProfile.getUplaodImage());
//	        
//	        response.setPlanType(existingUserProfile.getPlanType());
//	        
//	        return Optional.of(response);
//		    
//		}).orElse(Optional.empty()); // In case the userProfileId is not found // In case the userProfileId is not found
//	}


	@Override
	public Optional<List<Object>> saveForAstrocard(Long userProfileId, String planType, String firstName, String lastName,
	                                               Long mobileNumber, String dateOfBirth, String timeOfBirth, Long pincode, 
	                                               String location, String style, MultipartFile uploadImage, 
	                                               String language, String gender) {

	    return userProfileRepository.findById(userProfileId).map(existingUserProfile -> {

	        // Set fields as before (same logic as you provided)
	        existingUserProfile.setPlanType(planType != null ? planType : existingUserProfile.getPlanType());
	        existingUserProfile.setFirstName(firstName != null ? firstName : existingUserProfile.getFirstName());
	        existingUserProfile.setLastName(lastName != null ? lastName : existingUserProfile.getLastName());
	        existingUserProfile.setMobileNumber(mobileNumber != null ? mobileNumber : existingUserProfile.getMobileNumber());
	        existingUserProfile.setDateOfBirth(dateOfBirth != null ? dateOfBirth : existingUserProfile.getDateOfBirth());
	        existingUserProfile.setTimeOfBirth(timeOfBirth != null ? timeOfBirth : existingUserProfile.getTimeOfBirth());
	        existingUserProfile.setPincode(pincode != null ? pincode : existingUserProfile.getPincode());
	        existingUserProfile.setLocation(location != null ? location : existingUserProfile.getLocation());
	        existingUserProfile.setStyle(style != null ? style : existingUserProfile.getStyle());

	        // Convert MultipartFile to byte[] if provided
	        if (uploadImage != null && !uploadImage.isEmpty()) {
	            try {
	                existingUserProfile.setUplaodImage(uploadImage.getBytes());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        existingUserProfile.setLanguage(language != null ? language : existingUserProfile.getLanguage());

	        // Get latitude and longitude from pincode
	        GeocodingService geocodingService = new GeocodingService();
	        Location locationObj = geocodingService.getLatLongFromPincode(pincode);
	        if (locationObj != null) {
	            existingUserProfile.setLatitude(locationObj.getLatitude());
	            existingUserProfile.setLongitude(locationObj.getLongitude());

	            // Fetch and set the timezone
	            TimezoneService timezoneService = new TimezoneService();
	            String timeZone = timezoneService.getTimeZoneFromLatLong(locationObj.getLatitude(), locationObj.getLongitude());
	            existingUserProfile.setTimeZone(timeZone);
	        }

	        // Calculate and set the zodiac sign
	        AstrologyService astrologyService = new AstrologyService();
	        String zodiacSign = astrologyService.getZodiacSign(existingUserProfile.getDateOfBirth());
	        existingUserProfile.setZodiac(zodiacSign);
	        existingUserProfile.setGender(gender != null ? gender : existingUserProfile.getGender());

	        // Save the user profile
	        userProfileRepository.save(existingUserProfile);

	        // Create UserProfileResponseDTO2
	        UserProfileResponseDTO2 response = new UserProfileResponseDTO2();
	        response.setUserProfileId(existingUserProfile.getUserProfileId());
	        response.setUserName(existingUserProfile.getUserName());
	        response.setEmail(existingUserProfile.getEmail());
	        response.setMobileNumber(existingUserProfile.getMobileNumber());
	        response.setUplaodImage(existingUserProfile.getUplaodImage());
	        response.setPlanType(existingUserProfile.getPlanType());

	        // Now, fetch payment details based on userProfileId
	        List<PaymentResponseDTO> paymentResponses = getPaymentsForUser(userProfileId);

	        // Combine both the user profile and payment response into a list
	        List<Object> responseList = new ArrayList<>();
	        responseList.add(response); // Add the user profile response as the first item
	        responseList.addAll(paymentResponses); // Add payment response objects

	        return Optional.of(responseList);

	    }).orElse(Optional.empty());
	}

	// Helper method to fetch payments by userProfileId
	private List<PaymentResponseDTO> getPaymentsForUser(Long userProfileId) {
	    // Fetch payment details based on the userProfileId
	    List<Object[]> results = paymentsRepository.findSuccessfulPaymentsByUserId(userProfileId);

	    // If results are not empty, map them to PaymentResponseDTO
	    if (!results.isEmpty()) {
	        return results.stream().map(result -> 
	            new PaymentResponseDTO(
	                (String) result[0], // planType
	                (String) result[1], // status
	                (Long) result[2],   // userProfileId
	                (String) result[3],  // userName
	                (byte[]) result[4],   // Image
	                (String) result[5]    // First Name
	            )
	        ).collect(Collectors.toList());
	    } else {
	        // If no payment details are found, return a default payment response with empty details
	        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
	        paymentResponseDTO.setUserProfileId(userProfileId);
	        paymentResponseDTO.setUserName("N/A");
	        paymentResponseDTO.setUplaodImage(null);
	        return List.of(paymentResponseDTO);
	    }
	}



	
	
	
	
	@Override
	public List<PaymentResponseDTO> login(String email, String password) 
	{
		// Retrieve the user by email
		UserProfile userProfile = userProfileRepository.findByEmail(email);

		if (userProfile != null)
		{
			// Compare the provided password with the stored hashed password
			if (passwordEncoder.matches(password, userProfile.getPassword())) 
			{
				
						//System.err.println("Login1 method");
				
				
				List<Object[]> results = paymentsRepository.findSuccessfulPaymentsByUserId2(userProfile.getUserProfileId());
				
				System.err.println("Login1 method");
		        
		        // Convert the Object[] into PaymentResponseDTO
		        if (!results.isEmpty()) 
		        {
			        	return results.stream().map(result -> 
			            new PaymentResponseDTO(
			                (String) result[0], // planType
			                (String) result[1], // status
			                (Long) result[2],   // userProfileId
			                (String) result[3],  // userName
			                (byte[]) result[4],   //Image
			                (String) result[5],	  //First Name
			                (String) result[6]    //shipping_customer_name....
			            )
			          ).collect(Collectors.toList());
		        	
				}
		        else
		        {
		        	PaymentResponseDTO paymentResponseDTO= new PaymentResponseDTO();
					paymentResponseDTO.setUserProfileId(userProfile.getUserProfileId());
					paymentResponseDTO.setUserName(userProfile.getUserName());
					paymentResponseDTO.setUplaodImage(userProfile.getUplaodImage());
					
							//System.err.println("Login Method");
					
					return List.of(paymentResponseDTO);
		        }
		    }
			else 
			{
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is not matching");
			}
		}
		else 
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email is not found");
		}
	}
	
	
//	@Override
//	public List<PaymentResponseDTO> login(String email, String password) 
//	{
//		// Retrieve the user by email
//		UserProfile userProfile = userProfileRepository.findByEmail(email);
//
//		if (userProfile != null)
//		{
//			// Compare the provided password with the stored hashed password
//			if (passwordEncoder.matches(password, userProfile.getPassword())) 
//			{
//				
//				////System.err.println("Login1 method");
//				
//				List<Object[]> results = paymentsRepository.findSuccessfulPaymentsByUserId(userProfile.getUserProfileId());
//				
//				//System.err.println("Login1 method");
//		        
//		        // Convert the Object[] into PaymentResponseDTO
//		        if (!results.isEmpty()) 
//		        {
//			        	return results.stream().map(result -> 
//			            new PaymentResponseDTO(
//			                (String) result[0], // planType
//			                (String) result[1], // status
//			                (Long) result[2],   // userProfileId
//			                (String) result[3],  // userName
//			                (byte[]) result[4],   //Image
//			                (String) result[5]    //First Name
//			            )
//			          ).collect(Collectors.toList());
//		        	
//				}
//		        else
//		        {
//		        	PaymentResponseDTO paymentResponseDTO= new PaymentResponseDTO();
//					paymentResponseDTO.setUserProfileId(userProfile.getUserProfileId());
//					paymentResponseDTO.setUserName(userProfile.getUserName());
//					paymentResponseDTO.setUplaodImage(userProfile.getUplaodImage());
//					
//					//System.err.println("Login Method");
//					
//					return List.of(paymentResponseDTO);
//		        }
//		    }
//			else 
//			{
//				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is not matching");
//			}
//		}
//		else 
//		{
//			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email  not found");
//		}
//	}


	
	


	public static String generateOTP() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000); // Generates a random number between 100000 and 999999
		return String.valueOf(otp);
	}


	
	@Override
	public String forgotPasswordSendOTP(String email) {
	    UserProfile userProfile1 = userProfileRepository.findByEmail(email);
	    if (userProfile1 != null) {

	        // Generate OTP and save it to the user profile
	        String generateOTP = generateOTP();
	        userProfile1.setOtp(generateOTP);
	        userProfileRepository.save(userProfile1);

	        try {
	            // Create MIME message for HTML content
	            MimeMessage message = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            
	            helper.setFrom(fromMail);
	            helper.setTo(email);
	            helper.setSubject("OTP for Password Reset - Astrology Application");

	            // Compose the colorful HTML email body
	            String emailBody = String.format(
	                "<!DOCTYPE html>" +
	                "<html lang=\"en\">" +
	                "<head>" +
	                "   <meta charset=\"UTF-8\">" +
	                "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
	                "   <style>" +
	                "       body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
	                "       .container { padding: 20px; background-color: #ffffff; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; }" +
	                "       .header { background-color: #4CAF50; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; color: #ffffff; text-align: center; }" +
	                "       .header h1 { margin: 0; font-size: 24px; }" +
	                "       .content { padding: 20px; color: #333333; }" +
	                "       .content p { line-height: 1.6; font-size: 16px; }" +
	                "       .otp { font-size: 24px; font-weight: bold; color: #ff5722; background-color: #fff3e0; padding: 10px; text-align: center; border-radius: 5px; margin: 15px 0; }" +
	                "       .footer { background-color: #4CAF50; padding: 15px; text-align: center; color: #ffffff; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px; font-size: 14px; }" +
	                "       .support { background-color: #e0f7fa; padding: 10px; margin: 20px 0; border-radius: 5px; text-align: center; }" +
	                "       a { color: #00796b; text-decoration: none; }" +
	                "   </style>" +
	                "</head>" +
	                "<body>" +
	                "   <div class=\"container\">" +
	                "       <div class=\"header\">" +
	                "           <h1>Astrology App - Password Reset</h1>" +
	                "       </div>" +
	                "       <div class=\"content\">" +
	                "           <p>Dear %s,</p>" + 
	                "           <p>We received a request to reset the password for your account associated with this email address (<strong>%s</strong>).</p>" +
	                "           <p>To proceed with the password reset, please use the following One-Time Password (OTP):</p>" +
	                "           <div class=\"otp\">%s</div>" +
	                "           <p>Enter this code to reset your password. Once verified, you’ll be prompted to create a new password.</p>" +
	                "           <p>If you did not request this, please ignore this email or contact our support team.</p>" +
	                "           <div class=\"support\">" +
	                "               <p>For assistance, contact us at <a href=\"mailto:support@techpixe.com\">info@techpixe.com</a>.</p>" +
	                "           </div>" +
	                "       </div>" +
	                "       <div class=\"footer\">" +
	                "           <p>Thank you, <br>The TechPixe Team</p>" +
	                "           <p>&copy; 2024 TechPixe. All rights reserved.</p>" +
	                "       </div>" +
	                "   </div>" +
	                "</body>" +
	                "</html>", userProfile1.getUserName(), email, generateOTP);

	            // Set the HTML content in the email
	            helper.setText(emailBody, true);

	            // Send the email
	            javaMailSender.send(message);
	        } catch (MessagingException e) {
	            throw new RuntimeException("Failed to send email", e);
	        }

	        return "OTP Sent Successfully to your Email";
	    } else {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email not found");
	    }
	}

	
	



	@Override
	public String forgotPassword(String email, String newPassword, String otp) {
		UserProfile userProfile1 = userProfileRepository.findByEmail(email);
		if (userProfile1 != null) {
			if (userProfile1.getOtp().equals(otp)) {

				userProfile1.setPassword(passwordEncoder.encode(newPassword));
				userProfileRepository.save(userProfile1);

				return "Password Changed Successfully";
			} else {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OTP does not Macth");
			}

		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email with this user is not Found");
		}
	}

	@Override
	public Optional<UserProfileResponseDTO2> saveForAstroCard1(Long userProfileId, String userName, Long mobileNumber,
			MultipartFile uploadImage, String language) {
		return userProfileRepository.findById(userProfileId).map(existingUserProfile -> {
			existingUserProfile.setUserName(userName!=null ? userName : existingUserProfile.getUserName());
			existingUserProfile
					.setMobileNumber(mobileNumber != null ? mobileNumber : existingUserProfile.getMobileNumber());
			

			// Convert MultipartFile to byte[] if provided
			if (uploadImage != null && !uploadImage.isEmpty()) {
				try {
					existingUserProfile.setUplaodImage(uploadImage.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			existingUserProfile.setLanguage(language != null ? language : existingUserProfile.getLanguage());

			 userProfileRepository.save(existingUserProfile);
			 
			 	UserProfileResponseDTO2 response = new UserProfileResponseDTO2();
		        response.setUserProfileId(existingUserProfile.getUserProfileId());
		        response.setUserName(existingUserProfile.getUserName());
		        response.setEmail(existingUserProfile.getEmail());
		        response.setMobileNumber(existingUserProfile.getMobileNumber());
		        response.setUplaodImage(existingUserProfile.getUplaodImage());
		        
		        return Optional.of(response);
			    
		}).orElse(Optional.empty()); // In case the userProfileId is not found // In case the userProfileId is not found
	}

	@Override
	public ZodiacDTO webhook(Long userProfileId) {
		UserProfile userProfile1 = fetchById(userProfileId);
		if (userProfile1 != null) {
			ZodiacDTO zodiacDTO = new ZodiacDTO();
			zodiacDTO.setUserId(userProfileId);
			zodiacDTO.setLocalDate(LocalDate.now());
			zodiacDTO.setZodiac(userProfile1.getZodiac());
			zodiacDTO.setLanguage(userProfile1.getLanguage());
			zodiacDTO.setDateOfBirth(userProfile1.getDateOfBirth());
			
			zodiacDTO.setTimeOfBirth(userProfile1.getTimeOfBirth());
			zodiacDTO.setLatitude(userProfile1.getLatitude());
			zodiacDTO.setLongitude(userProfile1.getLongitude());
			zodiacDTO.setLocation(userProfile1.getLocation());
			zodiacDTO.setTimeZone(userProfile1.getTimeZone());
			zodiacDTO.setStyle(userProfile1.getStyle());
			zodiacDTO.setFirstName(userProfile1.getFirstName());
			zodiacDTO.setLastName(userProfile1.getLastName());
			zodiacDTO.setGender(userProfile1.getGender());

			return zodiacDTO;
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User Profile Id is not Found");
		}
	}

	@Override
	public UserProfile fetchById(Long userProfileId) {
		return userProfileRepository.findById(userProfileId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile is not found"));
	}

	// **********Total Active Users List*****************
	@Override
	public Set<UserProfile> getActiveUserProfiles() {
		List<Payments> successfulPayments = paymentsRepository.findByStatus("Success");
		Set<UserProfile> activeUserProfiles = new HashSet<>();

		for (Payments payment : successfulPayments) {
			if (payment.getUserProfile() != null) {
				activeUserProfiles.add(payment.getUserProfile());
			}
		}

		return activeUserProfiles; // Returns unique active user profiles
	}
	
	
	

	@Override
	public String changePassword(String oldPassword,String password, String confirmPassword, Long userProfileId)
	{
		UserProfile userProfile1 = userProfileRepository.findById(userProfileId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"UserProfile Id was not not found"));
		if (passwordEncoder.matches(oldPassword, userProfile1.getPassword())) 
		{
			if (userProfile1!=null && password.equals(confirmPassword))
			{
				userProfile1.setPassword(passwordEncoder.encode(password));
				userProfileRepository.save(userProfile1);
				
				return "Password Changed Successfully";
			} 
			else 
			{
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Password and Confirm Password Does not match");
			}
		} 
		else 
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Old Password Does not match");
		}
		
	}

	
	

}
