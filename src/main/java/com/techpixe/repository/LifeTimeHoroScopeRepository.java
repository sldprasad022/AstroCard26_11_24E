package com.techpixe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.entity.LifeTimeHoroscope;

public interface LifeTimeHoroScopeRepository extends JpaRepository<LifeTimeHoroscope, Long>
{
	Optional<LifeTimeHoroscope> findByUserProfileId(Long userProfileId);
}
