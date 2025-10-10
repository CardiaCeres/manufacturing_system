package com.manufacturing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manufacturing.model.Order;
import com.manufacturing.model.User;
import com.manufacturing.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // 根據使用者角色與部門回傳訂單
    public List<Order> getOrdersForUser(User user) {
        if ("MANAGER".equalsIgnoreCase(user.getRole()) || "ADMIN".equalsIgnoreCase(user.getRole())) {
            // 管理者或經理 → 看到自己部門的訂單
            return orderRepository.findByDepartmentOrderByOrderNumberAsc(user.getDepartment());
        } else {
            // 一般使用者 → 只看到自己的訂單
            return orderRepository.findByUserIdOrderByOrderNumberAsc(user.getId());
        }
    }

    // ✅ 取得指定使用者的訂單
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderNumberAsc(userId);
    }

    // ✅ 取得指定部門的訂單
    public List<Order> getOrdersByDepartment(String department) {
        return orderRepository.findByDepartmentOrderByOrderNumberAsc(department);
    }

    // ✅ 依 ID 查詢訂單（供 Controller 驗證權限時使用）
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // 建立訂單
    public Order createOrder(Order order) {
        if (order.getQuantity() != null && order.getPrice() != null) {
            order.setTotalAmount(order.getQuantity() * order.getPrice());
        }
        return orderRepository.save(order);
    }

    // 刪除訂單
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // 更新訂單
    public Order updateOrder(Long id, Order order) {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isPresent()) {
            Order existingOrder = existingOrderOpt.get();
            existingOrder.setOrderNumber(order.getOrderNumber());
            existingOrder.setCustomerName(order.getCustomerName());
            existingOrder.setMaterialName(order.getMaterialName());
            existingOrder.setProductName(order.getProductName());
            existingOrder.setQuantity(order.getQuantity());
            existingOrder.setPrice(order.getPrice());
            existingOrder.setTotalAmount(existingOrder.getQuantity() * existingOrder.getPrice());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setOrderDate(order.getOrderDate());
            existingOrder.setDepartment(order.getDepartment());
            return orderRepository.save(existingOrder);
        } else {
            throw new RuntimeException("訂單未找到，無法更新");
        }
    }
}
