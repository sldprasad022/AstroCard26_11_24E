package com.techpixe.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Entity
public class LifeTimeHoroscope 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lifeTimeId;
	
	@Lob
    private String jsonResponse; // Changed from Map to String
	
	@Transient
    private Map<String, Object> jsonResponseMap;
	
	private String jsonRequest;
	
	
	private LocalDateTime createdAt;
	
	private String title;   // here we are doing Hard code
	
	private String isLongTerm;  // here we are doing Hard code
	
	private Long userProfileId;
	
	
	@Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_data", columnDefinition = "LONGBLOB")
    private byte[] pdfData;  // New field to store PDF data
	
	public byte[] getPdfData() {
        return pdfData;
    }

    public void setPdfData(byte[] pdfData) {
        this.pdfData = pdfData;
    }
	
	
	public Long getLifeTimeId() {
		return lifeTimeId;
	}

	public void setLifeTimeId(Long lifeTimeId) {
		this.lifeTimeId = lifeTimeId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsLongTerm() {
		return isLongTerm;
	}

	public void setIsLongTerm(String isLongTerm) {
		this.isLongTerm = isLongTerm;
	}

	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public static ObjectMapper getObjectmapper() {
		return objectMapper;
	}

	public String getJsonRequest() {
		return jsonRequest;
	}

	public void setJsonRequest(String jsonRequest) {
		this.jsonRequest = jsonRequest;
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
}
