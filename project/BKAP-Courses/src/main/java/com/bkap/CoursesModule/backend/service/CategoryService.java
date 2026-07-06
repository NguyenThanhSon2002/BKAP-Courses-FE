package com.bkap.CoursesModule.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.CoursesModule.backend.repository.ICategoryRepository;
import com.bkap.CoursesModule.entity.Category;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category findById(Short id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void deleteById(Short id) {
		categoryRepository.deleteById(id);
	}

}
