package com.techpixe.service;

import com.techpixe.dto.AdminDTO;
import com.techpixe.entity.Admin;

public interface AdminService
{
	Admin registerAdmin(String adminName,String email,Long mobileNumber,String password);
	
	String loginByEmail(String email, String password);
	
	String loginByMobileNumber(Long mobileNumber, String password);
	
	String forgotPasswordSendOTP(String email);
	
	String forgotPassword(String email,String otp,String newPassword);
}
