package com.techpixe.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>
{


}
