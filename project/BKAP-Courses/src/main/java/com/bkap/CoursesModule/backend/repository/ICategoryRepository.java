package com.bkap.CoursesModule.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bkap.CoursesModule.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Short> {

}
