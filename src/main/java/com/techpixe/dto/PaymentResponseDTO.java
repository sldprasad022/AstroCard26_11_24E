package com.techpixe.dto;


public class PaymentResponseDTO {
    private String planType;
    private String status;
    private Long userProfileId;
    private String userName;
    private byte[] uplaodImage;
    
    private String firstName;
    
   // private String billing_customer_name;
    private String shipping_customer_name;
    
    

	public PaymentResponseDTO(String planType, String status, Long userProfileId, String userName, byte[] uplaodImage,
			String firstName) {
		super();
		this.planType = planType;
		this.status = status;
		this.userProfileId = userProfileId;
		this.userName = userName;
		this.uplaodImage = uplaodImage;
		this.firstName = firstName;
	}

	public PaymentResponseDTO(String planType, String status, Long userProfileId, String userName, byte[] uplaodImage,
		String firstName, String shipping_customer_name) {
	super();
	this.planType = planType;
	this.status = status;
	this.userProfileId = userProfileId;
	this.userName = userName;
	this.uplaodImage = uplaodImage;
	this.firstName = firstName;
	this.shipping_customer_name = shipping_customer_name;
}

	public PaymentResponseDTO(String planType, String status, Long userProfileId, String userName, byte[] uplaodImage) {
		super();
		this.planType = planType;
		this.status = status;
		this.userProfileId = userProfileId;
		this.userName = userName;
		this.uplaodImage = uplaodImage;
	}
    
    public PaymentResponseDTO(String planType, String status, Long userProfileId, String userName) {
		super();
		this.planType = planType;
		this.status = status;
		this.userProfileId = userProfileId;
		this.userName = userName;
	}
    
    

	public PaymentResponseDTO() {
	}

	

	// Getters and Setters
    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	public byte[] getUplaodImage() {
		return uplaodImage;
	}

	public void setUplaodImage(byte[] uplaodImage) {
		this.uplaodImage = uplaodImage;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getShipping_customer_name() {
		return shipping_customer_name;
	}

	public void setShipping_customer_name(String shipping_customer_name) {
		this.shipping_customer_name = shipping_customer_name;
	}

	
    
    
}
