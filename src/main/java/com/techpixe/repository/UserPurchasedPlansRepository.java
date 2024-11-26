package com.techpixe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.entity.UserProfile;
import com.techpixe.entity.UserPurchasedPlans;

public interface UserPurchasedPlansRepository extends JpaRepository<UserPurchasedPlans, Long>
{

}
