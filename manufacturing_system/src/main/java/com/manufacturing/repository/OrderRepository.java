package com.manufacturing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manufacturing.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByOrderNumberAsc(Long userId);// 根據 User ID 查詢訂單
}