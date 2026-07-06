package com.bkap.CoursesModule.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bkap.CoursesModule.backend.service.ICategoryService;
import com.bkap.CoursesModule.entity.Category;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

	@Autowired
	private ICategoryService categoryService;

	// Hiển thị danh sách
	@GetMapping
	public String list(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		return "admin/category/list";
	}

	// Hiển thị form thêm mới
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("category", new Category());
		return "admin/category/form";
	}

	// Lưu category (thêm mới)
	@PostMapping("/save")
	public String save(@ModelAttribute Category category) {
		categoryService.save(category);
		return "redirect:/admin/category";
	}

	// Hiển thị form sửa
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Short id, Model model) {
		Category category = categoryService.findById(id);
		model.addAttribute("category", category);
		return "admin/category/form";
	}

	// Xóa category
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Short id) {
		categoryService.deleteById(id);
		return "redirect:/admin/category";
	}
}