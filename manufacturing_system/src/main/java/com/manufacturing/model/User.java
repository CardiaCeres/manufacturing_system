package com.manufacturing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    // 權限角色: ADMIN / MANAGER/ USER
    private String role = "USER";  
    private String department;      // 所屬部門

    // 重設密碼用
    private String resetToken;
    private LocalDateTime tokenExpiry;

    // Email 驗證用
    private Boolean enabled = false;          // 帳號是否已完成 Email 驗證
    private String verifyToken;               // 驗證用 Token
    private LocalDateTime verifyTokenExpiry;  // 驗證 Token 有效期限

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) { 
        this.department = department; 
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public LocalDateTime getVerifyTokenExpiry() {
        return verifyTokenExpiry;
    }

    public void setVerifyTokenExpiry(LocalDateTime verifyTokenExpiry) {
        this.verifyTokenExpiry = verifyTokenExpiry;
    }
}
