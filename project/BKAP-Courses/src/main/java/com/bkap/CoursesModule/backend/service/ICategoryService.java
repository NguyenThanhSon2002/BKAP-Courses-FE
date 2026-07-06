package com.bkap.CoursesModule.backend.service;

import java.util.List;

import com.bkap.CoursesModule.entity.Category;

public interface ICategoryService {

	List<Category> findAll();

	Category findById(Short id);

	Category save(Category category);

	void deleteById(Short id);
}
