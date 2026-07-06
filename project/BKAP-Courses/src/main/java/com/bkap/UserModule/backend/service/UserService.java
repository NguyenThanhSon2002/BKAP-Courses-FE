package com.bkap.UserModule.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.UserModule.backend.repository.IUserRepository;
import com.bkap.UserModule.dto.UserResponseDTO;
import com.bkap.UserModule.entity.User;
import com.bkap.UserModule.entity.UserRole;
import com.bkap.UserModule.form.UserFormForRegister;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Override
	public User login(String username, String password) {
		// 1. Tìm customer theo username
		Optional<User> optional = userRepository.findByUsername(username);

		// 2. Kiểm tra mật khẩu
		if (optional.isPresent() && optional.get().getPassword().equals(password)) {
			return optional.get();
		}
		return null; // Trả về null nếu sai thông tin
	}

	@Override
	public boolean existsByPhone(String phone) {
		return userRepository.existsByPhone(phone);
	}

	@Override
	public User createCustomer(UserFormForRegister form) {
		User user = new User();
		user.setFullname(form.getFullname());
		user.setUsername(form.getUsername());
		user.setPhone(form.getPhone());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setRole(UserRole.STUDENT);
		user.setBirthday(form.getBirthday());

		return userRepository.save(user);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public void updateLastLogin(String username) {
		Optional<User> optional = userRepository.findByUsername(username);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setLastLogin(new java.util.Date());
			user.setOnline(true); // ← Đánh dấu online
			userRepository.save(user);
		}
	}

	@Override
	public void updateLastLogout(String username) {
		Optional<User> optional = userRepository.findByUsername(username);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setLastLogout(new java.util.Date());
			user.setOnline(false); // ← Đánh dấu offline
			userRepository.save(user);
		}
	}

	@Override
	public UserResponseDTO getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy: " + username));

		UserResponseDTO dto = new UserResponseDTO();
		dto.setFullname(user.getFullname());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getPhone());
		// dto.setAddress(user.getAddress());
		// dto.setBio(user.getBio());
		dto.setRole(user.getRole() != null ? user.getRole().name() : null);

		// ✅ birthday là String → gán thẳng, không cần format
		dto.setBirthday(user.getBirthday());

		return dto;
	}

	@Override
	public UserResponseDTO updateUserProfile(String username, UserResponseDTO request) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy: " + username));

		// Chỉ cập nhật các field được phép thay đổi
		if (request.getFullname() != null) {
			user.setFullname(request.getFullname());
		}
		if (request.getPhone() != null) {
			user.setPhone(request.getPhone());
		}
		if (request.getBirthday() != null) {
			user.setBirthday(request.getBirthday());
		}
		// if (request.getAddress() != null) {
		// user.setAddress(request.getAddress());
		// }
		// if (request.getBio() != null) {
		// user.setBio(request.getBio());
		// }

		userRepository.save(user);

		// Trả về data mới nhất
		return getUserByUsername(username);

	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
}
