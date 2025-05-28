package com.manufacturing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manufacturing.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // 根據 username 查找用戶
    User findByUsername(String username);
}