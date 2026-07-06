package com.bkap.UserModule.backend.service;

import java.util.List;

import com.bkap.UserModule.dto.UserResponseDTO;
import com.bkap.UserModule.entity.User;
import com.bkap.UserModule.form.UserFormForRegister;

public interface IUserService {

	User login(String username, String password);

	boolean existsByPhone(String phone);

	User createCustomer(UserFormForRegister form);

	boolean existsByUsername(String username);

	User findByUsername(String username);

	void updateLastLogin(String username);

	void updateLastLogout(String username);

	// Lấy thông tin user dựa vào username (hoặc lấy từ JWT token/Principal)
	UserResponseDTO getUserByUsername(String username);

	UserResponseDTO updateUserProfile(String username, UserResponseDTO request);

	// ===== Thêm mới cho module Admin (Thymeleaf) =====
	List<User> findAll();

}
