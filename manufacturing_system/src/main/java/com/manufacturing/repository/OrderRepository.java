package com.manufacturing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manufacturing.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 根據 User ID 查詢訂單，依訂單號排序
    List<Order> findByUserIdOrderByOrderNumberAsc(Long userId);

    // 根據 User ID 查詢訂單
    List<Order> findByUserId(Long userId);

    // 根據部門查詢訂單 (Manager 權限)
    List<Order> findByDepartment(String department);

    // 根據部門排序查詢
    List<Order> findByDepartmentOrderByOrderNumberAsc(String department);

    // 根據訂單狀態查詢 (可做報表或流程管理)
    List<Order> findByStatus(String status);
}