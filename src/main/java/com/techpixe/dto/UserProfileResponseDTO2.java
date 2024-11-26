package com.techpixe.dto;

public class UserProfileResponseDTO2 
{
	private Long userProfileId;
    private String userName;
    private String email;
    private Long mobileNumber;
    private byte[] uplaodImage;
    
    private String planType;
    
    
    
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public Long getUserProfileId() {
		return userProfileId;
	}
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public byte[] getUplaodImage() {
		return uplaodImage;
	}
	public void setUplaodImage(byte[] uplaodImage) {
		this.uplaodImage = uplaodImage;
	}
	
	
	
    
}
