package com.techpixe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserPurchasedPlans
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userPurchasedPlansId;
	
	private Long planTypeId;
	
	private String planType;
	
	private Double paymentAmount;
	
	private String validity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userProfileId")
	@JsonIgnore
	private UserProfile userProfile;

	public Long getUserPurchasedPlansId() {
		return userPurchasedPlansId;
	}

	public void setUserPurchasedPlansId(Long userPurchasedPlansId) {
		this.userPurchasedPlansId = userPurchasedPlansId;
	}

	public Long getPlanTypeId() {
		return planTypeId;
	}

	public void setPlanTypeId(Long planTypeId) {
		this.planTypeId = planTypeId;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	
}
