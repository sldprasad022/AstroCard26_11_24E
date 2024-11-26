package com.techpixe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techpixe.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>
{
	UserProfile findByEmail(String email);
	
	boolean existsByFirstNameNotNullAndLastNameNotNullAndMobileNumberNotNullAndDateOfBirthNotNullAndTimeOfBirthNotNullAndPincodeNotNullAndLatitudeNotNullAndLongitudeNotNullAndTimeZoneNotNullAndLocationNotNullAndStyleNotNullAndLanguageNotNullAndGenderNotNull();
	
	// Custom query method to check if the user profile is complete based on the userProfileId
    @Query("SELECT CASE WHEN (u.userName IS NOT NULL AND u.firstName IS NOT NULL AND u.lastName IS NOT NULL " +
           "AND u.email IS NOT NULL AND u.password IS NOT NULL AND u.mobileNumber > 0 " +
           "AND u.dateOfBirth IS NOT NULL AND u.timeOfBirth IS NOT NULL AND u.pincode > 0 " +
           "AND u.latitude IS NOT NULL AND u.longitude IS NOT NULL AND u.timeZone IS NOT NULL " +
           "AND u.location IS NOT NULL AND u.style IS NOT NULL AND u.uplaodImage IS NOT NULL " +
           "AND u.language IS NOT NULL AND u.zodiac IS NOT NULL AND u.role IS NOT NULL " +
           "AND u.gender IS NOT NULL) THEN TRUE ELSE FALSE END " +
           "FROM UserProfile u WHERE u.userProfileId = :userProfileId")
    boolean isUserProfileComplete(Long userProfileId);

}
