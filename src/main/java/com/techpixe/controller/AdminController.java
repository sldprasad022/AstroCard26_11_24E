package com.techpixe.controller;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.dto.ErrorResponseDto;
import com.techpixe.entity.Admin;
import com.techpixe.service.AdminService;

import jakarta.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/api/admin")
public class AdminController 
{
	@Autowired
	private AdminService adminService;
	
	private boolean isEmail(String email)
	{
		return email.contains("@");
	}
	
	private boolean isMobileNumber(String emailOrMobileNumber) {
		return emailOrMobileNumber.matches("\\d+");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String emailOrMobileNumber,@RequestParam String password)
	{
		if (emailOrMobileNumber!=null)
		{
			if (isEmail(emailOrMobileNumber))
			{
				String loginEmail= adminService.loginByEmail(emailOrMobileNumber, password);
				return new ResponseEntity<String>(loginEmail,HttpStatus.OK);
			}
			else if(isMobileNumber(emailOrMobileNumber))
			{
				String loginMobile= adminService.loginByMobileNumber(Long.parseLong(emailOrMobileNumber), password);
				return new ResponseEntity<String>(loginMobile,HttpStatus.OK);
			}
			else
			{
				ErrorResponseDto errorResponseDto = new ErrorResponseDto();
				errorResponseDto.setError("Invalid Email Format or Invalid Mobile Number Format.Please Enter a Valid Input");
				return ResponseEntity.internalServerError().body(errorResponseDto);
			}
		}
		else
		{
			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Invalid input. Email or mobile number must be provided.");
			return ResponseEntity.internalServerError().body(errorResponseDto);
		}
	}
	
	
	@PostMapping("/forgotPasswordSendOTP")
	public ResponseEntity<String> forgotPasswordSendOTP(@RequestParam String email)
	{
		String adminDTO = adminService.forgotPasswordSendOTP(email);
		return new ResponseEntity<String>(adminDTO,HttpStatus.OK);
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestParam String email,@RequestParam String otp,@RequestParam String newPassword)
	{
		String adminDTO = adminService.forgotPassword(email,otp,newPassword);
		return new ResponseEntity<String>(adminDTO,HttpStatus.OK);
	}
	
	@PostMapping("/registerAdmin")
	public ResponseEntity<Admin> registerAdmin(@RequestParam String adminName,@RequestParam String email,@RequestParam Long mobileNumber,@RequestParam String password)
	{
		Admin saved = adminService.registerAdmin(adminName, email, mobileNumber, password);
		return new ResponseEntity<Admin>(saved,HttpStatus.OK);
	}
	

	

}
