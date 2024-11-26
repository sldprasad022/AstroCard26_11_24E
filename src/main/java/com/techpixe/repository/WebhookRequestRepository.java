package com.techpixe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.entity.WebhookRequest;

public interface WebhookRequestRepository extends JpaRepository<WebhookRequest, Long>
{
	List<WebhookRequest> findByUserProfileId(Long userProfileId);
	
	// Method to find a single record by userProfileId and title
    Optional<WebhookRequest> findByUserProfileIdAndTitle(Long userProfileId, String title);
}
