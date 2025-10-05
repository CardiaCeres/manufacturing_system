package com.manufacturing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    // 登入
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean valid = userService.validateUser(user.getUsername(), user.getPassword());

        if (valid) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok().body(
                    new AuthResponse(token, userService.getUserByUsername(user.getUsername()))
            );
        } else {
            return ResponseEntity.status(401).body("憑證錯誤，請確認帳號密碼");
        }
    }

    // 註冊
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("使用者名稱已存在");
        }

        User newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    // 忘記密碼（寄信）
@PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
    String email = request.get("email");

    Optional<User> optionalUser = userService.getUserByEmail(email);

    if (optionalUser.isPresent()) {
        try {
            User user = optionalUser.get();

            // 產生重設密碼連結
            String token = userService.generateResetToken(user);
            String resetUrl = frontendUrl + "/reset-password?token=" + token;

            // 寄信給使用者註冊信箱
            emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);

            // 信箱存在且寄信成功
            return ResponseEntity.ok(Map.of("message", "寄送成功"));
        } catch (Exception e) {
            // 信箱存在但寄信失敗
            return ResponseEntity.status(500).body(Map.of(
                    "message", "寄信失敗"
              ));
        }
    } else {
        // 信箱不存在，無法寄信
        return ResponseEntity.status(400).body(Map.of("message", "寄送失敗：無法寄信"));
    }
}

    // Token + User 封裝
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
