package com.bkap.CoursesModule.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.CoursesModule.backend.repository.ICategoryRepository;
import com.bkap.CoursesModule.backend.repository.ICourseRepository;
import com.bkap.CoursesModule.dto.CourseDTO;
import com.bkap.CoursesModule.entity.Category;
import com.bkap.CoursesModule.entity.Course;
import com.bkap.UserModule.backend.repository.IUserRepository;
import com.bkap.UserModule.entity.User;

@Service
public class CourseService implements ICourseService {

	@Autowired
	private ICourseRepository courseRepository;

	@Autowired
	private IUserRepository userRepository; // Inject để tìm thông tin giảng viên liên kết

	@Autowired
	private ICategoryRepository categoryRepository; // Inject để tìm thông tin danh mục liên kết

	@Override
	public List<Course> getAllCourses() {
		// Trả về toàn bộ danh sách khóa học trong database
		return courseRepository.findAll();
	}

	@Override
	public List<Course> getCoursesByCategory(String slug) {
		// Trả về danh sách khóa học được lọc theo slug của danh mục
		return courseRepository.findByCategory_Slug(slug);
	}

	@Override
	public Course getCourseById(Short id) {
		// Tìm khóa học theo ID sử dụng Optional giống như cách bạn tìm User
		Optional<Course> optional = courseRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null; // Trả về null nếu không tìm thấy giống login/findByUsername bên UserService
	}

	@Override
	public Course createCourse(CourseDTO courseDTO) {
		// Khởi tạo thực thể mới bằng từ khóa new giống createCustomer
		Course course = new Course();

		// Set từng trường dữ liệu trực tiếp từ DTO gửi lên
		course.setTitle(courseDTO.getTitle());
		course.setSlug(courseDTO.getSlug());
		course.setSubtitle(courseDTO.getSubtitle());
		course.setDescription(courseDTO.getDescription());
		course.setThumbnailUrl(courseDTO.getThumbnailUrl());
		course.setPreviewVideoUrl(courseDTO.getPreviewVideoUrl());
		course.setLevel(courseDTO.getLevel());
		course.setIsPro(courseDTO.getIsPro());
		course.setPrice(courseDTO.getPrice());
		course.setOriginalPrice(courseDTO.getOriginalPrice());
		course.setPriceType(courseDTO.getPriceType());
		course.setRating(courseDTO.getRating());
		course.setRatingCount(courseDTO.getRatingCount());
		course.setStudentCount(courseDTO.getStudentCount());
		course.setLessonCount(courseDTO.getLessonCount());
		course.setDurationText(courseDTO.getDurationText());

		// 1. Tìm và gán Danh mục (Category) liên kết thông qua ID ngắn (Short)
		if (courseDTO.getCategoryId() != null) {
			Optional<Category> catOpt = categoryRepository.findById(courseDTO.getCategoryId());
			if (catOpt.isPresent()) {
				course.setCategory(catOpt.get());
			}
		}

		// 2. Tìm và gán Giảng viên (User) liên kết thông qua ID
		if (courseDTO.getInstructorId() != null) {
			// Ép kiểu ID của User nếu cần, ở đây sử dụng phương thức findById chuẩn của JPA
			Optional<User> userOpt = userRepository.findById(courseDTO.getInstructorId());
			if (userOpt.isPresent()) {
				course.setInstructor(userOpt.get());
			}
		}

		// Lưu xuống database và trả về thực thể vừa tạo giống UserRepository.save(user)
		return courseRepository.save(course);
	}

	@Override
	public Course updateCourse(Short id, CourseDTO courseDTO) {
		// 1. Tìm khóa học cũ đang có trong Database
		Optional<Course> optional = courseRepository.findById(id);

		if (optional.isPresent()) {
			Course existingCourse = optional.get();

			// 2. Cập nhật các thông tin mới từ DTO đè lên các trường cũ
			existingCourse.setTitle(courseDTO.getTitle());
			existingCourse.setSlug(courseDTO.getSlug());
			existingCourse.setSubtitle(courseDTO.getSubtitle());
			existingCourse.setDescription(courseDTO.getDescription());
			existingCourse.setThumbnailUrl(courseDTO.getThumbnailUrl());
			existingCourse.setPreviewVideoUrl(courseDTO.getPreviewVideoUrl());
			existingCourse.setLevel(courseDTO.getLevel());
			existingCourse.setIsPro(courseDTO.getIsPro());
			existingCourse.setPrice(courseDTO.getPrice());
			existingCourse.setOriginalPrice(courseDTO.getOriginalPrice());
			existingCourse.setPriceType(courseDTO.getPriceType());
			existingCourse.setRating(courseDTO.getRating());
			existingCourse.setRatingCount(courseDTO.getRatingCount());
			existingCourse.setStudentCount(courseDTO.getStudentCount());
			existingCourse.setLessonCount(courseDTO.getLessonCount());
			existingCourse.setDurationText(courseDTO.getDurationText());

			// Cập nhật lại danh mục liên kết nếu có thay đổi
			if (courseDTO.getCategoryId() != null) {
				Optional<Category> catOpt = categoryRepository.findById(courseDTO.getCategoryId());
				if (catOpt.isPresent()) {
					existingCourse.setCategory(catOpt.get());
				}
			}

			// Cập nhật lại giảng viên phụ trách nếu có thay đổi
			if (courseDTO.getInstructorId() != null) {
				Optional<User> userOpt = userRepository.findById(courseDTO.getInstructorId());
				if (userOpt.isPresent()) {
					existingCourse.setInstructor(userOpt.get());
				}
			}

			// Lưu lại thực thể đã chỉnh sửa
			return courseRepository.save(existingCourse);
		}

		return null; // Trả về null nếu không tìm thấy khóa học để sửa
	}

	@Override
	public void deleteCourse(Short id) {
		// Tìm xem khóa học có tồn tại trước khi xóa không bằng Optional
		Optional<Course> optional = courseRepository.findById(id);
		if (optional.isPresent()) {
			// Thực hiện xóa bản ghi khỏi database
			courseRepository.delete(optional.get());
		}
	}

	@Override
	public Course save(Course course) {
		return courseRepository.save(course);
	}
}
