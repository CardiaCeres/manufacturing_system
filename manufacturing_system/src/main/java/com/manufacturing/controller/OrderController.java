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
import com.manufacturing.model.User;
import com.manufacturing.security.JwtUtil;
import com.manufacturing.service.OrderService;
import com.manufacturing.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin(origins = "https://manufacturing-system-latest.onrender.com")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // 從 JWT 抽出使用者並取得訂單
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            String username = JwtUtil.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(401).body("無效使用者");
            }

            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("伺服器錯誤：" + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Order order, HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            String username = JwtUtil.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(401).body("無效使用者");
            }

            // 設定使用者 ID
            order.setUserId(user.getId());

            // 驗證必要欄位
            if (order.getProductName() == null || order.getProductName().isEmpty()) {
                return ResponseEntity.badRequest().body("產品名稱為必填");
            }

            // 計算總金額
            if (order.getQuantity() != null && order.getPrice() != null) {
                order.setTotalAmount(order.getQuantity() * order.getPrice());
            }

            // 預設狀態
            if (order.getStatus() == null || order.getStatus().isEmpty()) {
                order.setStatus("處理中");
            }

            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("建立訂單失敗：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("刪除失敗：" + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("更新失敗：" + e.getMessage());
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}