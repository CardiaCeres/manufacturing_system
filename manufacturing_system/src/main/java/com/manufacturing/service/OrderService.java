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
        if ("MANAGER".equals(user.getRole())) {
            // 經理 → 只看到自己部門的訂單
            return orderRepository.findByDepartmentOrderByOrderNumberAsc(user.getDepartment());
        } else {
            // 一般使用者 → 只看到自己的訂單
            return orderRepository.findByUserIdOrderByOrderNumberAsc(user.getId());
        }
    }

    public Order createOrder(Order order) {
        order.setTotalAmount(order.getQuantity() * order.getPrice());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

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
