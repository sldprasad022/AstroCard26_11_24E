package com.techpixe.entity;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

@Entity
public class Matching 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long matchingId;
	
	private String partnerName;
	
	private String partnerGender;
	
	private String partnerPincode;
	
	private String partnerLatitude;
	
	private String partnerLongitude;
	
	private String partnerDateOfBirth;
	
	private String partnerTimeOfBirth;
	
	private String partnerLanguage;
	
	private String title;
	
	private boolean userGender;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userProfileId")
	private UserProfile userProfile;
	
	@Lob
    private String jsonResponse; // Changed from Map to String
	
	@Transient
    private Map<String, Object> jsonResponseMap;
	
	private String jsonRequest;

	public Long getMatchingId() {
		return matchingId;
	}

	public void setMatchingId(Long matchingId) {
		this.matchingId = matchingId;
	}

	

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPartnerGender() {
		return partnerGender;
	}

	public void setPartnerGender(String partnerGender) {
		this.partnerGender = partnerGender;
	}

	public String getPartnerPincode() {
		return partnerPincode;
	}

	public void setPartnerPincode(String partnerPincode) {
		this.partnerPincode = partnerPincode;
	}

	public String getPartnerLatitude() {
		return partnerLatitude;
	}

	public void setPartnerLatitude(String partnerLatitude) {
		this.partnerLatitude = partnerLatitude;
	}

	public String getPartnerLongitude() {
		return partnerLongitude;
	}

	public void setPartnerLongitude(String partnerLongitude) {
		this.partnerLongitude = partnerLongitude;
	}

	public String getPartnerDateOfBirth() {
		return partnerDateOfBirth;
	}

	public void setPartnerDateOfBirth(String partnerDateOfBirth) {
		this.partnerDateOfBirth = partnerDateOfBirth;
	}

	public String getPartnerTimeOfBirth() {
		return partnerTimeOfBirth;
	}

	public void setPartnerTimeOfBirth(String partnerTimeOfBirth) {
		this.partnerTimeOfBirth = partnerTimeOfBirth;
	}

	public String getPartnerLanguage() {
		return partnerLanguage;
	}

	public void setPartnerLanguage(String partnerLanguage) {
		this.partnerLanguage = partnerLanguage;
	}

	public String getJsonRequest() {
		return jsonRequest;
	}

	public void setJsonRequest(String jsonRequest) {
		this.jsonRequest = jsonRequest;
	}

	public static ObjectMapper getObjectmapper() {
		return objectMapper;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	// ObjectMapper for JSON conversion
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Getter and Setter for jsonResponse
    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
        try {
            this.jsonResponseMap = objectMapper.readValue(jsonResponse, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Getter for jsonResponseMap
    public Map<String, Object> getJsonResponseMap() {
        return jsonResponseMap;
    }

    // Override setter for jsonResponseMap to convert back to JSON
    public void setJsonResponseMap(Map<String, Object> jsonResponseMap) {
        this.jsonResponseMap = jsonResponseMap;
        try {
            this.jsonResponse = objectMapper.writeValueAsString(jsonResponseMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isUserGender() {
		return userGender;
	}

	public void setUserGender(boolean userGender) {
		this.userGender = userGender;
	}

	
    
    
	
	
	
}
