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

@CrossOrigin(origins = "frontendUrl")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /** 取得訂單（依角色過濾） */
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

    /** 建立訂單 */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Order order, HttpServletRequest request) {
        try {
            User user = getAuthenticatedUser(request);
            if (user == null) return ResponseEntity.status(401).body("未授權使用者");

            // ✅ 共用驗證方法
            ResponseEntity<?> validationError = validateOrder(order, true);
            if (validationError != null) return validationError;

            // 一般使用者只能建立自己的訂單
            order.setUserId(user.getId());
            order.setDepartment(user.getDepartment());

            if (order.getTotalAmount() == null && order.getQuantity() != null && order.getPrice() != null) {
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

    /** 更新訂單 */
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

            // ✅ 共用驗證方法
            ResponseEntity<?> validationError = validateOrder(order, false);
            if (validationError != null) return validationError;

            // 保留不可修改欄位
            order.setId(id);
            order.setUserId(existingOrder.getUserId());
            order.setDepartment(existingOrder.getDepartment());

            if (order.getTotalAmount() == null && order.getQuantity() != null && order.getPrice() != null) {
                order.setTotalAmount(order.getQuantity() * order.getPrice());
            }

            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("更新失敗：" + e.getMessage());
        }
    }

    /** 刪除訂單 */
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

    /** 共用驗證方法 */
    private ResponseEntity<?> validateOrder(Order order, boolean isCreate) {
        if (isCreate && (order.getOrderNumber() == null || order.getOrderNumber().isEmpty())) {
            return ResponseEntity.badRequest().body("訂單編號為必填");
        }
        if (order.getProductName() == null || order.getProductName().isEmpty()) {
            return ResponseEntity.badRequest().body("產品名稱為必填");
        }
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("數量必須大於 0");
        }
        if (order.getPrice() == null || order.getPrice() < 0) {
            return ResponseEntity.badRequest().body("價格不可為負數");
        }
        return null;
    }

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
