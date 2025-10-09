package com.manufacturing.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET = System.getenv("JWT_SECRET"); // 建議長度 >= 256 bits
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private static final long EXPIRATION_TIME = 86400000L; // 1 天

    // 生成 Token，支援額外 claims，例如 role 與 department
    public static String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 從 token 解析 username
    public static String extractUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    // 從 token 取得角色
    public static String extractRole(String token) {
        try {
            return (String) getClaims(token).get("role");
        } catch (Exception e) {
            return null;
        }
    }

    // 從 token 取得部門
    public static String extractDepartment(String token) {
        try {
            return (String) getClaims(token).get("department");
        } catch (Exception e) {
            return null;
        }
    }

    // 驗證 token 是否有效
    public static boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername != null 
                && extractedUsername.equals(username)
                && !isTokenExpired(token);
    }

    public static boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
