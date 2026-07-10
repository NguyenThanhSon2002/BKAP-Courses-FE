package com.bkap.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);

			System.out.println("Token (10 ký tự đầu): " + token.substring(0, 10));
			System.out.println("Token valid: " + jwtUtil.validateToken(token));

			if (jwtUtil.validateToken(token)) {
				String username = jwtUtil.getUsernameFromToken(token);
				String role = jwtUtil.getRoleFromToken(token);
				System.out.println("Username: " + username + " | Role: " + role);

				SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, List.of(authority));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				System.out.println(">>> TOKEN INVALID!");
			}
		} else {
			System.out.println(">>> KHÔNG CÓ AUTH HEADER!");
		}

		filterChain.doFilter(request, response);
	}
}