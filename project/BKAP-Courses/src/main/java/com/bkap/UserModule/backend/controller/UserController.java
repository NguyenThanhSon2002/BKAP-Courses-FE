package com.bkap.UserModule.backend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkap.UserModule.backend.service.IUserService;
import com.bkap.UserModule.dto.UserResponseDTO;

@RestController
@RequestMapping(value = "api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private IUserService userService;

	/**
	 * API lấy thông tin profile của user đang đăng nhập URL: GET
	 * http://localhost:8080/api/v1/user/profile
	 */
	@GetMapping("/profile")
	public ResponseEntity<UserResponseDTO> getUserProfile(Principal principal) {
		// Principal chứa thông tin username của người dùng hiện tại đang đăng nhập hệ
		// thống
		if (principal == null) {
			return ResponseEntity.status(401).build(); // Chưa đăng nhập/không có token hợp lệ
		}

		String username = principal.getName();
		UserResponseDTO userProfile = userService.getUserByUsername(username);

		return ResponseEntity.ok(userProfile);
	}

	@PutMapping("/profile")
	public ResponseEntity<UserResponseDTO> updateUserProfile(Principal principal,
			@RequestBody UserResponseDTO updateRequest) {

		if (principal == null) {
			return ResponseEntity.status(401).build();
		}

		String username = principal.getName();
		UserResponseDTO updated = userService.updateUserProfile(username, updateRequest);
		return ResponseEntity.ok(updated);
	}

}
