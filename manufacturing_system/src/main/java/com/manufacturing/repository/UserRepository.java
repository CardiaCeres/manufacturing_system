package com.manufacturing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manufacturing.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根據 username 查找用戶
    User findByUsername(String username);

    // 根據 email 查找用戶
    Optional<User> findByEmail(String email);

    // 根據 resetToken 查找用戶
    Optional<User> findByResetToken(String resetToken);

    // 根據 verifyToken 查找用戶（Email 驗證）
    Optional<User> findByVerifyToken(String verifyToken);

    // 依角色查找所有使用者
    List<User> findByRole(String role);

    // 依部門查找所有使用者
    List<User> findByDepartment(String department);

    // 依角色和部門查找使用者 (可給 Manager 或 Admin 管理)
    List<User> findByRoleAndDepartment(String role, String department);
}
