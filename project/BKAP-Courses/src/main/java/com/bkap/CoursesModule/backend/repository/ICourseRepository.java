package com.bkap.CoursesModule.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bkap.CoursesModule.entity.Course;

public interface ICourseRepository extends JpaRepository<Course, Short> {

	List<Course> findByCategory_Slug(String slug);

}
