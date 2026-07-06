package com.bkap.CoursesModule.backend.controller;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bkap.CoursesModule.backend.service.ICategoryService;
import com.bkap.CoursesModule.backend.service.ICourseService;
import com.bkap.CoursesModule.entity.Category;
import com.bkap.CoursesModule.entity.Course;
import com.bkap.UserModule.backend.service.IUserService;
import com.bkap.UserModule.entity.User;

@Controller
@RequestMapping("/admin/course")
public class AdminCourseController {

	@Autowired
	private ICourseService courseService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IUserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Category.class, "category", new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				if (text == null || text.isEmpty()) {
					setValue(null);
					return;
				}
				Category category = new Category();
				category.setId(Short.parseShort(text));
				setValue(category);
			}
		});

		binder.registerCustomEditor(User.class, "instructor", new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				if (text == null || text.isEmpty()) {
					setValue(null);
					return;
				}
				User user = new User();
				user.setId(Short.parseShort(text));
				setValue(user);
			}
		});
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("courses", courseService.getAllCourses());
		return "admin/course/list";
	}

	// ✅ THÊM MỚI — bị thiếu trước đó
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("course", new Course());
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("instructors", userService.findAll());
		return "admin/course/form";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute Course course) {
		courseService.save(course);
		return "redirect:/admin/course";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Short id, Model model) {
		model.addAttribute("course", courseService.getCourseById(id));
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("instructors", userService.findAll());
		return "admin/course/form";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Short id) {
		courseService.deleteCourse(id);
		return "redirect:/admin/course";
	}
}