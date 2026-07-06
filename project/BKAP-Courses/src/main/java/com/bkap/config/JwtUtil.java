package com.bkap.config;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	// Secret key - giữ bí mật, không share
	private static final String SECRET = "bkap-courses-secret-key-2026-very-long-string";
	private static final long EXPIRATION = 24 * 60 * 60 * 1000; // 24 giờ

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	// Tạo token từ username + role
	public String generateToken(String username, String role) {
		return Jwts.builder().setSubject(username).claim("role", role) // ← THÊM MỚI: nhúng role vào token
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	// Lấy username từ token
	public String getUsernameFromToken(String token) {
		return getClaims(token).getSubject();
	}

	// Lấy role từ token
	public String getRoleFromToken(String token) {
		return getClaims(token).get("role", String.class); // ← THÊM MỚI
	}

	// Kiểm tra token còn hạn không
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Helper dùng nội bộ
	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

}
