package com.manufacturing.service;

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

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean validateUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) return false;

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}