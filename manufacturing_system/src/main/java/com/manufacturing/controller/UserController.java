package com.manufacturing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

import com.manufacturing.model.User;
import com.manufacturing.security.JwtUtil;
import com.manufacturing.service.EmailService;
import com.manufacturing.service.UserService;

@RestController
@CrossOrigin(origins = "frontendUrl")
@RequestMapping("/api")
public class UserController {

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /* =========================
       登入
       ========================= */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean valid = userService.validateUser(user.getUsername(), user.getPassword());

        if (!valid) {
            return ResponseEntity.status(401).body("憑證錯誤，請確認帳號密碼");
        }

        User dbUser = userService.getUserByUsername(user.getUsername());
        if (dbUser == null) {
            return ResponseEntity.status(401).body("使用者不存在");
        }

        // 檢查帳號是否完成 Email 驗證
        if (!Boolean.TRUE.equals(dbUser.getEnabled())) {
            return ResponseEntity.status(403).body("帳號尚未完成 Email 驗證");
        }

        // 生成 JWT，包含 username、role、department
        String token = JwtUtil.generateToken(
            dbUser.getUsername(),
            Map.of(
                "role", dbUser.getRole(),
                "department", dbUser.getDepartment()
            )
        );

        return ResponseEntity.ok().body(new AuthResponse(token, dbUser));
    }

    /* =========================
       註冊（寄送 Email 驗證信）
       ========================= */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("使用者名稱已存在");
        }

        // 儲存新使用者，預設 enabled=false
        User newUser = userService.saveUser(user);

        try {
            // 產生驗證 Token
            String verifyToken = userService.generateVerifyToken(newUser);
            String verifyUrl = frontendUrl + "/verify-email?token=" + verifyToken;

            // 寄送驗證信
            emailService.sendVerifyEmail(newUser.getEmail(), verifyUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "寄送驗證信失敗"));
        }

        return ResponseEntity.ok(Map.of("message", "註冊成功，請前往信箱完成驗證"));
    }

    /* =========================
       Email 驗證 API
       ========================= */
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {

          String token = request.get("token");

          if (token == null || token.isBlank()) {
               return ResponseEntity.badRequest().body("Token 不存在");
         }

         Optional<User> optionalUser = userService.getUserByVerifyToken(token);
         if (optionalUser.isEmpty()) {
              return ResponseEntity.status(400).body("驗證 Token 無效或已過期");
        }

        User user = optionalUser.get();
        userService.enableUser(user);

        return ResponseEntity.ok("Email 驗證成功，帳號已啟用，請重新登入");
   }


    /* =========================
       忘記密碼（寄信）
       ========================= */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> optionalUser = userService.getUserByEmail(email);

        if (optionalUser.isPresent()) {
            try {
                User user = optionalUser.get();
                String token = userService.generateResetToken(user);
                String resetUrl = frontendUrl + "/reset-password?token=" + token;

                emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);
                return ResponseEntity.ok(Map.of("message", "寄送成功"));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(Map.of("message", "寄信失敗"));
            }
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "寄送失敗：無法寄信"));
        }
    }

    /* =========================
       重設密碼
       ========================= */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body("缺少必要參數");
        }

        Optional<User> optionalUser = userService.getUserByResetToken(token);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Token 無效或已過期");
        }

        User user = optionalUser.get();
        userService.resetPassword(user, newPassword);

        return ResponseEntity.ok("密碼已成功重設，請重新登入");
    }

    /* =========================
       Token + User 封裝
       ========================= */
    static class AuthResponse {
        private final String token;
        private final User user;

        public AuthResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }
}
