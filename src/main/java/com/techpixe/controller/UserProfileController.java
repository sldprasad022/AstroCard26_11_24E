package com.techpixe.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techpixe.dto.ErrorResponseDto;
import com.techpixe.dto.PaymentResponseDTO;
import com.techpixe.dto.UserProfileResponseDTO;
import com.techpixe.dto.UserProfileResponseDTO2;
import com.techpixe.dto.ZodiacDTO;

import com.techpixe.entity.UserProfile;
import com.techpixe.service.UserProfileService;

import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/userProfile")
public class UserProfileController 
{
	@Autowired
	private UserProfileService userProfileService;
	

	
	@PostMapping("/save")
	public ResponseEntity<UserProfileResponseDTO> save(@RequestParam  String userName,@RequestParam  String email, @RequestParam  String password, @RequestParam String confirmPassword)
	{
		UserProfileResponseDTO saved= userProfileService.save(userName, email, password, confirmPassword);
		return new ResponseEntity<UserProfileResponseDTO>(saved,HttpStatus.CREATED);
	}

	
		//After Payment 
//		@PutMapping("/update1/{userProfileId}/{planType}")
//		public ResponseEntity<UserProfileResponseDTO2> saveForAstroCard(@PathVariable Long userProfileId,@PathVariable String planType ,@RequestParam(required = false) String firstName,@RequestParam(required = false) String lastName,@RequestParam(required = false) String mobileNumber,
//				@RequestParam(required = false)  String dateOfBirth,@RequestParam(required = false) String timeOfBirth,@RequestParam(required = false) Long pincode,@RequestParam(required = false) String location, 
//				@RequestParam(required = false)  String style,@RequestParam(required = false) MultipartFile uploadImage,@RequestParam(required = false) String language, @RequestParam(required = false) String gender)
//		{
//			// Convert mobile number from String to Long
//	        Long mobileNumber1 = Long.valueOf(mobileNumber);
//			
//			Optional<UserProfileResponseDTO2> updatedUser = userProfileService.saveForAstrocard(userProfileId,planType, firstName, lastName, mobileNumber1, dateOfBirth, timeOfBirth, pincode, location, style, uploadImage, language,gender);
//			return updatedUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());	
//			
//		}
	
	@PutMapping("/update1/{userProfileId}/{planType}")
	public ResponseEntity<List<Object>> saveForAstroCard(
	    @PathVariable Long userProfileId,
	    @PathVariable String planType,
	    @RequestParam(required = false) String firstName,
	    @RequestParam(required = false) String lastName,
	    @RequestParam(required = false) Long mobileNumber,
	    @RequestParam(required = false) String dateOfBirth,
	    @RequestParam(required = false) String timeOfBirth,
	    @RequestParam(required = false) Long pincode,
	    @RequestParam(required = false) String location,
	    @RequestParam(required = false) String style,
	    @RequestParam(required = false) MultipartFile uploadImage,
	    @RequestParam(required = false) String language,
	    @RequestParam(required = false) String gender
	) {
	    try {
	        // Convert mobileNumber from String to Long
	        //Long mobileNumber1 = (mobileNumber != null) ? Long.valueOf(mobileNumber) : null;

	        // Call the service method to update the user profile and get payment details
	        Optional<List<Object>> updatedResponse = userProfileService.saveForAstrocard(
	            userProfileId,
	            planType,
	            firstName,
	            lastName,
	            mobileNumber,
	            dateOfBirth,
	            timeOfBirth,
	            pincode,
	            location,
	            style,
	            uploadImage,
	            language,
	            gender
	        );

	        // Check if the update was successful and return the response
	        return updatedResponse.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

	    } catch (NumberFormatException e) {
	        // Handle invalid mobile number format
	        return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
	    } catch (Exception e) {
	        // Handle other unforeseen errors
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 Internal Server Error
	    }
	}
	
	
	
	private boolean isEmail(String email)
	{
		return email.contains("@");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email,@RequestParam String password)
	{
		if (email!=null)
		{
			if (isEmail(email)) 
			{
				List<PaymentResponseDTO> loginUser = userProfileService.login(email, password);
				return new ResponseEntity<List<PaymentResponseDTO>>(loginUser,HttpStatus.OK);
			}
			else
			{
				ErrorResponseDto errorResponseDto = new ErrorResponseDto();
				errorResponseDto.setError("Email is not Found");
				return ResponseEntity.internalServerError().body(errorResponseDto);
			}
		}
		else
		{
			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Invalid Email Pattern");
			return ResponseEntity.internalServerError().body(errorResponseDto);
		}
	}
	
	
	
	@PostMapping("/forgotPasswordSendOTP/{email}")
	public ResponseEntity<?> forgotPasswordSendOTP(@PathVariable String email)
	{
		String userProfileDTO = userProfileService.forgotPasswordSendOTP(email);
		return new ResponseEntity<String>(userProfileDTO,HttpStatus.OK);
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestParam String email,@RequestParam String newPassword,@RequestParam String otp)
	{
		if (email!=null)
		{
			if (isEmail(email))
			{
				String userProfileDTO = userProfileService.forgotPassword(email, newPassword, otp);
				return new ResponseEntity<String>(userProfileDTO,HttpStatus.OK);
			} 
			else
			{
				ErrorResponseDto errorResponseDto = new ErrorResponseDto();
				errorResponseDto.setError("Invalid Email Pattern");
				return ResponseEntity.internalServerError().body(errorResponseDto);
			}
		} 
		else
		{
			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Email is not Found");
			return ResponseEntity.internalServerError().body(errorResponseDto);
		}
	}
	
	
	@PutMapping("/update2/{userProfileId}")
	public ResponseEntity<UserProfileResponseDTO2> saveForAstroCard1(@PathVariable Long userProfileId,@RequestParam(required = false) String userName,
			@RequestParam(required = false) Long mobileNumber,@RequestParam(required = false)  MultipartFile uploadImage,@RequestParam(required = false) String language)
	{
		Optional<UserProfileResponseDTO2> updateUser = userProfileService.saveForAstroCard1(userProfileId, userName, mobileNumber, uploadImage, language);
		return updateUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		
		
		
	}
	
	@GetMapping("/fetchById/{userProfileId}")
	public ResponseEntity<UserProfile> fetechById(@PathVariable Long userProfileId)
	{
		UserProfile fetchById = userProfileService.fetchById(userProfileId);
		return new ResponseEntity<UserProfile>(fetchById,HttpStatus.OK);
	}
	
	@GetMapping("/webhook/{userProfileId}")
	public ResponseEntity<ZodiacDTO> webhook(@PathVariable Long userProfileId)
	{
		ZodiacDTO webhookDTO = userProfileService.webhook(userProfileId);
		return new ResponseEntity<ZodiacDTO>(webhookDTO,HttpStatus.OK);
	}
	
	
//***********Total Active Users List******************
    
    @GetMapping("/TotalActiveUsersList")
    public ResponseEntity<Set<UserProfile> > getActiveUserProfiles() {
         Set<UserProfile> allActiveUsersList = userProfileService.getActiveUserProfiles();
         return new ResponseEntity<Set<UserProfile>>(allActiveUsersList,HttpStatus.OK);
    }
    
    
    //----------------User Prediction-------------------------------------
	
    @PostMapping("/changePassword/{userProfileId}")
	public ResponseEntity<String> changePassord(@RequestParam String oldPassword ,@RequestParam String password,@RequestParam String confirmPassword,@PathVariable Long userProfileId)
	{
    	String changedPassword= userProfileService.changePassword(oldPassword,password, confirmPassword, userProfileId);
    	return new ResponseEntity<String>(changedPassword,HttpStatus.OK);
	}
	
}
