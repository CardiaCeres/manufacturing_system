package com.manufacturing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manufacturing.model.Order;
import com.manufacturing.model.User;
import com.manufacturing.security.JwtUtil;
import com.manufacturing.service.OrderService;
import com.manufacturing.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "frontendUrl") // 替換成實際前端 URL
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ---------- 取得訂單 ----------
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            User user = getAuthenticatedUser(request);
            if (user == null) return ResponseEntity.status(401).body("未授權使用者");

            List<Order> orders;
            if ("MANAGER".equalsIgnoreCase(user.getRole())) {
                orders = orderService.getOrdersByDepartment(user.getDepartment());
            } else {
                orders = orderService.getOrdersByUserId(user.getId());
            }

            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("伺服器錯誤：" + e.getMessage());
        }
    }

    // ---------- 建立訂單 ----------
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Order order, HttpServletRequest request) {
        try {
            User user = getAuthenticatedUser(request);
            if (user == null) return ResponseEntity.status(401).body("未授權使用者");

            // 權限控制
            if ("MANAGER".equalsIgnoreCase(user.getRole())) {
                // MANAGER 若未指定 userId，預設為自己
                if (order.getUserId() == null) order.setUserId(user.getId());

                // 從 userId 取得對應的部門，確保在同部門
                User targetUser = userService.getUserById(order.getUserId());
                if (targetUser == null || !user.getDepartment().equals(targetUser.getDepartment())) {
                    return ResponseEntity.status(403).body("只能為同部門成員建立訂單");
                }
                order.setDepartment(targetUser.getDepartment());

            } else {
                // 一般使用者只能建立自己的訂單
                order.setUserId(user.getId());
                order.setDepartment(user.getDepartment());
            }

            if (order.getProductName() == null || order.getProductName().isEmpty()) {
                return ResponseEntity.badRequest().body("產品名稱為必填");
            }

            if (order.getQuantity() != null && order.getPrice() != null) {
                order.setTotalAmount(order.getQuantity() * order.getPrice());
            }

            if (order.getStatus() == null || order.getStatus().isEmpty()) {
                order.setStatus("處理中");
            }

            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("建立訂單失敗：" + e.getMessage());
        }
    }

    // ---------- 更新訂單 ----------
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order, HttpServletRequest request) {
        try {
            User user = getAuthenticatedUser(request);
            if (user == null) return ResponseEntity.status(401).body("未授權使用者");

            Order existingOrder = orderService.getOrderById(id);
            if (existingOrder == null) return ResponseEntity.notFound().build();

            if (!canAccessOrder(user, existingOrder)) {
                return ResponseEntity.status(403).body("無權限修改此訂單");
            }

            // 確保 MANAGER 不會更新到其他部門訂單
            if ("MANAGER".equalsIgnoreCase(user.getRole())) {
                order.setDepartment(existingOrder.getDepartment());
                order.setUserId(existingOrder.getUserId());
            } else {
                // 一般使用者只能更新自己的訂單
                order.setUserId(user.getId());
                order.setDepartment(user.getDepartment());
            }

            // 自動計算 TotalAmount
            if (order.getQuantity() != null && order.getPrice() != null) {
                order.setTotalAmount(order.getQuantity() * order.getPrice());
            }

            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("更新失敗：" + e.getMessage());
        }
    }

    // ---------- 刪除訂單 ----------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, HttpServletRequest request) {
        try {
            User user = getAuthenticatedUser(request);
            if (user == null) return ResponseEntity.status(401).body("未授權使用者");

            Order existingOrder = orderService.getOrderById(id);
            if (existingOrder == null) return ResponseEntity.notFound().build();

            if (!canAccessOrder(user, existingOrder)) {
                return ResponseEntity.status(403).body("無權限刪除此訂單");
            }

            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("刪除失敗：" + e.getMessage());
        }
    }

    // ---------- Helper Methods ----------
    private User getAuthenticatedUser(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) return null;

        String username = JwtUtil.extractUsername(token);
        String role = JwtUtil.extractRole(token);
        String department = JwtUtil.extractDepartment(token);

        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setRole(role);
            user.setDepartment(department);
        }
        return user;
    }

    private boolean canAccessOrder(User user, Order order) {
        if ("MANAGER".equalsIgnoreCase(user.getRole())) {
            return order.getDepartment() != null && order.getDepartment().equals(user.getDepartment());
        } else {
            return order.getUserId() != null && order.getUserId().equals(user.getId());
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
