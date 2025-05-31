package com.manufacturing.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.manufacturing.model.Order;
import com.manufacturing.service.OrderService;
 
@RestController
@CrossOrigin(origins = "https://manufacturing-system-springboot.onrender.com")
@RequestMapping("/api/orders")
public class OrderController {
 
    @Autowired
    private OrderService orderService;
 
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
 
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            // 驗證必要欄位
            if (order.getUserId() == null || order.getProductName() == null || order.getProductName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
 
            // 計算總金額
            if (order.getQuantity() != null && order.getPrice() != null) {
                order.setTotalAmount(order.getQuantity() * order.getPrice());
            }
 
            // 確保狀態欄位有設置，若無則設為預設值
            if (order.getStatus() == null || order.getStatus().isEmpty()) {
                order.setStatus("處理中");
            }
 
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
 
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
      @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
 