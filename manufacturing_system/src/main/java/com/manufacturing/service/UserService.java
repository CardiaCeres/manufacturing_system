package com.manufacturing.service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.manufacturing.model.User;
import com.manufacturing.repository.UserRepository;
 
@Service
public class UserService {
 
    @Autowired
    private UserRepository userRepository;
 
    // 驗證使用者登入
    public boolean validateUser(String username, String password) {
        User existingUser = userRepository.findByUsername(username);
        return existingUser != null && existingUser.getPassword().equals(password);
    }
 
    // 根據使用者名稱取得使用者
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
 
    // 註冊新使用者
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}