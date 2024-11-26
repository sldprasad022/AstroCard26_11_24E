package com.techpixe.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.techpixe.dto.PaymentResponseDTO;
import com.techpixe.dto.UserProfileDTO;
import com.techpixe.dto.UserProfileResponseDTO;
import com.techpixe.dto.UserProfileResponseDTO2;
import com.techpixe.dto.ZodiacDTO;
import com.techpixe.entity.UserProfile;

public interface UserProfileService 
{
	UserProfileResponseDTO save(String userName,String email,String password,String confirmPassword);

	
//	Optional<UserProfileResponseDTO2> saveForAstrocard(Long userProfileId,String planType, String firstName, String lastName, Long mobileNumber,
//			String dateOfBirth, String timeOfBirth, Long pincode, String location, String style,
//			MultipartFile uploadImage, String language,String gender);
	
	
	Optional<List<Object>> saveForAstrocard(Long userProfileId, String planType, String firstName, String lastName,
			Long mobileNumber, String dateOfBirth, String timeOfBirth, Long pincode, String location, String style,
			MultipartFile uploadImage, String language, String gender);

	
	public List<PaymentResponseDTO> login(String email, String password);
	
	String forgotPasswordSendOTP(String email);
	
	String forgotPassword(String email,String newPassword, String otp);
	
	String changePassword(String oldPassword,String password,String confirmPassword,Long userProfileId);
	
	Optional<UserProfileResponseDTO2> saveForAstroCard1(Long userProfileId,String userName,Long mobileNumber,MultipartFile uploadImage,String language);
	
	
	
	//
	UserProfile fetchById(Long userProfileId);
	
	ZodiacDTO webhook(Long userProfileId);
	
	
	//Total Active Users List 
	Set<UserProfile> getActiveUserProfiles();
	
	 //----------------User Prediction-------------------------------------
	
	 
	
}
