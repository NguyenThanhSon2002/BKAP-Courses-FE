package com.bkap.CoursesModule.backend.service;

import java.util.List;

import com.bkap.CoursesModule.dto.CourseDTO;
import com.bkap.CoursesModule.entity.Course;

public interface ICourseService {

	List<Course> getAllCourses();

	List<Course> getCoursesByCategory(String slug);

	Course getCourseById(Short id);

	Course createCourse(CourseDTO courseDTO);

	Course updateCourse(Short id, CourseDTO courseDTO);

	void deleteCourse(Short id);

	// ===== Thêm mới cho module Admin (Thymeleaf) =====
	Course save(Course course);

}
