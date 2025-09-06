package com.manufacturing.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.manufacturing.model.User;
import com.manufacturing.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 儲存新使用者或更新密碼
    public User saveUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // 驗證使用者登入
    public boolean validateUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) return false;
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // 透過 username 取得使用者
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 透過 email 取得使用者
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 透過 resetToken 取得使用者
    public Optional<User> getUserByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    // 產生重設密碼 Token
    public String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(1)); // Token 有效 1 小時
        userRepository.save(user);
        return token;
    }

    // 驗證 Token 是否有效
    public boolean isResetTokenValid(User user, String token) {
        return token.equals(user.getResetToken()) &&
               user.getTokenExpiry() != null &&
               user.getTokenExpiry().isAfter(LocalDateTime.now());
    }

    // 重設密碼
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }
}