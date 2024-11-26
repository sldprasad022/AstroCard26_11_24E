package com.techpixe.serviceImpl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.dto.AdminDTO;
import com.techpixe.entity.Admin;
import com.techpixe.repository.AdminRepository;
import com.techpixe.service.AdminService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AdminServiceImpl implements AdminService
{
	@Autowired
	private AdminRepository adminRepository;
	
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("$(spring.mail.username)")
	private String fromMail;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	@Override
	public String loginByEmail(String email, String password) {
		Admin admin1 = adminRepository.findByEmail(email);
		if (admin1!=null)
		{
			if (passwordEncoder.matches(password, admin1.getPassword()))
			{
				return "Login is Succesfull";
			} 
			else 
			{
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Password does not Match");
			}
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Admin with this Email was not Found.");
		}
	}

	@Override
	public String loginByMobileNumber(Long mobileNumber, String password) {
		Admin admin1 = adminRepository.findByMobileNumber(mobileNumber);
		if (admin1!=null)
		{
			if (passwordEncoder.matches(password, admin1.getPassword())) 
			{
				
				
				return "Login is Succesfull";
			} 
			else
			{
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Password does not Match");
			}
		} 
		else
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Admin with this Mobile Number was not Found.");
		}
	}
	
	
	public static String generateOTP() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000); // Generates a random number between 100000 and 999999
		return String.valueOf(otp);
	}

//	@Override
//	public String forgotPasswordSendOTP(String email)
//	{
//		Admin admin1 = adminRepository.findByEmail(email);
//		if (admin1!=null)
//		{
//			AdminDTO adminDTO = new AdminDTO();
//			adminDTO.setAdminId(admin1.getAdminId());
//			adminDTO.setAdminName(admin1.getAdminName());
//			adminDTO.setEmail(email);
//			adminDTO.setMobileNumber(admin1.getMobileNumber());
//			adminDTO.setPassword(admin1.getPassword());
//			adminDTO.setRole(admin1.getRole());
//			
//			
//			String generateOTP = generateOTP();
//			adminDTO.setOtp(generateOTP);
//
//			admin1.setOtp(generateOTP);
//			adminRepository.save(admin1);
//
//			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//			simpleMailMessage.setFrom(fromMail);
//			simpleMailMessage.setTo(email);
//			simpleMailMessage.setSubject("OTP for ForgotPassword in Astrology Application\n");
//			simpleMailMessage.setText("Dear " + admin1.getAdminName()
//					+ "\n\nPlease check your  email and generted OTP\n UserEmail  :" + email + "\n  OTP :"
//					+ generateOTP + "\n\n"
//					+ "you will be required to reset the New password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
//			javaMailSender.send(simpleMailMessage);
//
//			
//			return "OTP Send to Your Email";
//		}
//		else
//		{
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Email is not Found");
//		}
//	}
	
	@Override
	public String forgotPasswordSendOTP(String email) {
	    Admin admin1 = adminRepository.findByEmail(email);
	    if (admin1 != null) {
	        AdminDTO adminDTO = new AdminDTO();
	        adminDTO.setAdminId(admin1.getAdminId());
	        adminDTO.setAdminName(admin1.getAdminName());
	        adminDTO.setEmail(email);
	        adminDTO.setMobileNumber(admin1.getMobileNumber());
	        adminDTO.setPassword(admin1.getPassword());
	        adminDTO.setRole(admin1.getRole());

	        String generateOTP = generateOTP();
	        adminDTO.setOtp(generateOTP);

	        admin1.setOtp(generateOTP);
	        adminRepository.save(admin1);

	        // HTML content with multiple colors and sections
	        String htmlContent = "<html>"
	                + "<body style='font-family: Arial, sans-serif; color: #333;'>"
	                + "<div style='width: 100%; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px;'>"
	                + "<div style='background-color: #4CAF50; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px;'>"
	                + "<h2 style='color: #ffffff; margin: 0;'>Bharat Astrology</h2>"
	                + "<p style='color: #ffffff; margin: 5px 0;'>OTP for Password Reset</p>"
	                + "</div>"
	                
	                + "<div style='padding: 20px; background-color: #f9f9f9;'>"
	                + "<p style='font-size: 16px;'>Dear <strong>" + admin1.getAdminName() + "</strong>,</p>"
	                + "<p>We received a request to reset your password for your account associated with <strong>" + email + "</strong>.</p>"
	                
	                + "<div style='background-color: #ffe0b2; padding: 15px; border-radius: 5px; margin: 15px 0;'>"
	                + "<p style='margin: 0; font-size: 18px; color: #bf360c;'><strong>Your OTP Code: " + generateOTP + "</strong></p>"
	                + "</div>"

	                + "<p>Please enter this OTP code to proceed with resetting your password. Once verified, you’ll be prompted to create a new password.</p>"
	                + "</div>"

	                + "<div style='padding: 20px; background-color: #e0f7fa;'>"
	                + "<p style='margin: 0; font-size: 14px;'>For assistance, feel free to reach us at <a href='mailto:info@techpixe.com' style='color: #00796b;'>info@techpixe.com</a>.</p>"
	                + "</div>"

	                + "<div style='padding: 15px; text-align: center; background-color: #4CAF50; color: #ffffff; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;'>"
	                + "<p style='margin: 0; font-size: 13px;'>Thank you,<br>The TechPixe Team</p>"
	                + "</div>"
	                
	                + "</div>"
	                + "</body>"
	                + "</html>";

	        try {
	            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	            helper.setFrom(fromMail);
	            helper.setTo(email);
	            helper.setSubject("OTP for Forgot Password in Astrology Application");
	            helper.setText(htmlContent, true); // Enable HTML

	            javaMailSender.send(mimeMessage);
	            return "OTP sent to your email";
	        } catch (MessagingException e) {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send OTP email", e);
	        }
	    } else {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email not found");
	    }
	}

	@Override
	public String forgotPassword(String email,String otp, String newPassword)
	{
		Admin admin1= adminRepository.findByEmail(email);
		if (admin1!=null) 
		{
			if (admin1.getOtp().equals(otp))
			{
				AdminDTO adminDTO = new AdminDTO();
				adminDTO.setAdminId(admin1.getAdminId());
				adminDTO.setAdminName(admin1.getAdminName());
				adminDTO.setEmail(email);
				adminDTO.setMobileNumber(admin1.getMobileNumber());
				
				adminDTO.setPassword(newPassword);
				
				admin1.setPassword(newPassword);
				adminRepository.save(admin1);
				
				return "Password Changed Succesfully";
			} 
			else
			{
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"OTP does not Match");
			}
		} 
		else
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Email was not Found");
		}
	}

	@Override
	public Admin registerAdmin(String adminName, String email, Long mobileNumber, String password)
	{
		Admin admin = new Admin();
		admin.setAdminName(adminName);
		admin.setEmail(email);
		admin.setMobileNumber(mobileNumber);
		admin.setPassword(passwordEncoder.encode(password));
		admin.setRole("Admin");
		
		return adminRepository.save(admin);
	}

	
}
