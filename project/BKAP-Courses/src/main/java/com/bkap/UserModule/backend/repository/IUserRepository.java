package com.bkap.UserModule.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bkap.UserModule.entity.User;

public interface IUserRepository extends JpaRepository<User, Short> {

	boolean existsByPhone(String phone);

	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);

	Optional<User> findById(Integer instructorId);

}
