package com.manufacturing.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manufacturing.model.User;
import com.manufacturing.service.UserService;
 
@RestController
@CrossOrigin(origins = "http://localhost:8082")
@RequestMapping("/api")
public class UserController {
 
    @Autowired
    private UserService userService;
 
    // 登入
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean valid = userService.validateUser(user.getUsername(), user.getPassword());
 
        if (valid) {
            return ResponseEntity.ok(userService.getUserByUsername(user.getUsername()));
        } else {
            return ResponseEntity.status(401).body("憑證錯誤，請確認帳號密碼");
        }
    }
 
    // 註冊
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // 檢查使用者是否已存在
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("使用者名稱已存在");
        }
 
        // 儲存新使用者
        User newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }
}