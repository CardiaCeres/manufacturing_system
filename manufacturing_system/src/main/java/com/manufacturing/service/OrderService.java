package com.manufacturing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manufacturing.model.Order;
import com.manufacturing.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderNumberAsc(userId);
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
            existingOrder.setTotalAmount(order.getTotalAmount());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setOrderDate(order.getOrderDate());
            existingOrder.setTotalAmount(existingOrder.getQuantity() * existingOrder.getPrice());

            return orderRepository.save(existingOrder);

            
        } else {
            throw new RuntimeException("訂單未找到，無法更新");
        }
    }
}