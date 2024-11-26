package com.techpixe.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.entity.Plans;

public interface PlansRepository extends JpaRepository<Plans, Long>
{
	Plans findByPlanType(String planType);
	
	Optional<Plans> findByPlanTypeId(Long planTypeId);
	
	void deleteByPlanType(String planType);
	
	Optional<Plans> findByValidityAndPlanType(String validity, String planType);
	
}
