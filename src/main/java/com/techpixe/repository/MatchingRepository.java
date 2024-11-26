package com.techpixe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Long>
{

	Optional<Matching> findByUserProfileUserProfileId(Long userProfileId);
	//Optional<Matching> findByUserProfile(Long userProfileId);
}
