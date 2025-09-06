package com.manufacturing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manufacturing.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根據 username 查找用戶
    User findByUsername(String username);

    // 根據 email 查找用戶
    Optional<User> findByEmail(String email);

    // 根據 resetToken 查找用戶
    Optional<User> findByResetToken(String resetToken);
}