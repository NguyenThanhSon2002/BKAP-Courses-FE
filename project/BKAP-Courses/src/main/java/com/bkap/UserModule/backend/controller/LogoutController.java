package com.bkap.UserModule.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkap.UserModule.backend.service.IUserService;
import com.bkap.UserModule.dto.LogoutRequest;
import com.bkap.UserModule.entity.User;

@RestController
@RequestMapping("api/v1/logout")
@CrossOrigin("*")
public class LogoutController {

	@Autowired
	private IUserService userService;

	@PostMapping()
	public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
		try {
			User user = userService.findByUsername(request.getUsername());

			if (user == null) {
				return ResponseEntity.status(404).body("Username không tồn tại!");
			}

			// ✅ Thêm dòng này: cập nhật last_logout và is_online = false vào DB
			userService.updateLastLogout(request.getUsername());

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Đăng xuất thành công!");

			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
		}
	}

}
