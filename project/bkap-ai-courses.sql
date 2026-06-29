DROP DATABASE IF EXISTS Bkap_Courses;
CREATE DATABASE Bkap_Courses;
USE Bkap_Courses;

-- ------------------------------------------------------------
-- Bảng 1: User	
-- Lưu thông tin cá nhân của khách hàng
-- ------------------------------------------------------------
DROP TABLE IF EXISTS User;
CREATE TABLE User (
    id            INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullname      VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE KEY,    
    username      VARCHAR(50)  NOT NULL UNIQUE KEY,
    phone         NVARCHAR(10)  NOT NULL UNIQUE KEY,      			
    password	  VARCHAR(50) NOT NULL,
    birthday	  VARCHAR(12) NOT NULL,
    role          ENUM('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT',
    createDate    DATETIME DEFAULT NOW(),
    last_login 	  DATETIME NULL,
    last_logout   DATETIME NULL,
    is_online     TINYINT(1) DEFAULT 0
    
);

-- ================================================================
-- BKAP_COURSES: Optimized Schema
-- ================================================================

-- ================================================================
-- 1. BẢNG DANH MỤC KHÓA HỌC
-- ================================================================
DROP TABLE IF EXISTS Category;
CREATE TABLE Category (
    id    INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, 			
    name  VARCHAR(100) NOT NULL, 											-- Tên hiển thị (Ví dụ: Full-Stack Developer)
    slug  VARCHAR(100) NOT NULL UNIQUE 										-- Dùng cho URL/Filter (Ví dụ: full-stack)
);

-- ================================================================
-- 2. BẢNG KHÓA HỌC (Một bảng duy nhất lưu data, liên kết 2 bảng ngoại)
-- ================================================================
DROP TABLE IF EXISTS Course;
CREATE TABLE Course (
    id             INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, 		
    category_id    INT UNSIGNED NOT NULL, 									-- Liên kết sang bảng Category
    instructor_id  INT UNSIGNED NOT NULL, 									-- Liên kết sang bảng User
    
    title          VARCHAR(200) NOT NULL, 						
    slug           VARCHAR(200) NOT NULL UNIQUE, 						
    subtitle       VARCHAR(300),                  						
    description    TEXT,													
    thumbnail_url  VARCHAR(500),											
    preview_video_url VARCHAR(500),											
    
    -- Phân loại & Giá cả
    level          ENUM('BEGINNER','INTERMEDIATE','ADVANCED','ALL') DEFAULT 'ALL',	
    is_pro         TINYINT(1) DEFAULT 0,												
    price          DECIMAL(12,0) DEFAULT 0,											
    original_price DECIMAL(12,0) DEFAULT 0,											
    price_type     ENUM('PAID','FREE','CONTACT') DEFAULT 'PAID',						
    
    -- Thống kê hiển thị thẳng (Hardcoded để tối giản)
    rating         DECIMAL(2,1) DEFAULT 0,								
    rating_count   INT DEFAULT 0,										
    student_count  INT DEFAULT 0,										
    lesson_count   INT DEFAULT 0,										
    duration_text  VARCHAR(50), -- Lưu text tĩnh hiển thị luôn (Ví dụ: "389h17p")								
    
    created_at     DATETIME DEFAULT NOW(),								
    updated_at     DATETIME DEFAULT NOW() ON UPDATE NOW(),				
    
    -- Định nghĩa các khóa ngoại để ràng buộc dữ liệu
    FOREIGN KEY (category_id)   REFERENCES Category(id),
    FOREIGN KEY (instructor_id) REFERENCES User(id)
);

-- ================================================================
-- 3. BẢNG ĐĂNG KÝ KHÓA HỌC & TIẾN ĐỘ
-- ================================================================
DROP TABLE IF EXISTS Enrollment;
CREATE TABLE Enrollment (
    id             INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id        INT UNSIGNED NOT NULL,                             
    course_id      INT UNSIGNED NOT NULL,                             
    
    -- Trạng thái học tập
    status         ENUM('ENROLLED', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'ENROLLED',
    
    -- Tiến độ tính theo % (Ví dụ: 75 nghĩa là 75%)
    progress_percent INT DEFAULT 0,                                   
    
    enrolled_at    DATETIME DEFAULT NOW(),
    last_studied_at DATETIME DEFAULT NOW() ON UPDATE NOW(),
    
    -- Đảm bảo một user không thể đăng ký trùng một khóa học 2 lần
    UNIQUE KEY unique_user_course (user_id, course_id),
    
    -- Khóa ngoại liên kết ràng buộc dữ liệu
    FOREIGN KEY (user_id)   REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE
);

-- ============================================================
-- DỮ LIỆU MẪU - Tài khoản admin để test
-- ============================================================
INSERT INTO User (fullname, email, username, phone, password, birthday, role, createDate, last_login, last_logout, is_online)
VALUES
('Phạm Minh Đức',   'phamminhduc@gmail.com',    'mducpham', '0901234564', 'Test@123456', '1998-12-25', 'ADMIN',   NOW(), NULL, NULL, 0),
('Nguyễn Văn An',   'nguyenvanan@gmail.com',     'vanan',    '0901234561', 'Test@123456', '2000-05-15', 'STUDENT', NOW(), NULL, NULL, 0),
('Trần Thị Bình',   'tranthibibinh@gmail.com',   'thibinh',  '0901234562', 'Test@123456', '1999-08-20', 'STUDENT', NOW(), NULL, NULL, 0),
('Lê Hoàng Cường',  'lehoangcuong@gmail.com',    'hcuong',   '0901234563', 'Test@123456', '2001-03-10', 'STUDENT', NOW(), NULL, NULL, 0),
('Hoàng Thị Em',    'hoangthiem@gmail.com',      'thiem',    '0901234565', 'Test@123456', '2002-07-04', 'STUDENT', NOW(), NULL, NULL, 0);

-- ================================================================
-- Bkap_Courses: Seed Data - 10 Khóa học mẫu
-- ================================================================
-- ================================================================
-- CHÈN DANH MỤC (Category)
-- ================================================================
INSERT INTO Category (id, name, slug) VALUES
(1, 'Full-Stack Developer', 'full-stack'),
(2, 'Frontend Developer', 'frontend'),
(3, 'AI & Data Science', 'ai-data-science'),
(4, 'Lập trình di động', 'mobile-developer'),
(5, 'DevOps & Cloud', 'devops-cloud');

-- ================================================================
-- CHÈN 10 KHÓA HỌC MẪU (Course)
-- ================================================================
INSERT INTO Course 
(category_id, instructor_id, title, slug, subtitle, description, thumbnail_url, preview_video_url, level, is_pro, price, original_price, price_type, rating, rating_count, student_count, lesson_count, duration_text) 
VALUES

-- Thuộc DM 1: Full-Stack Developer
(1, 2, 'Full-Stack Developer', 'full-stack-developer', 'React • Node.js • MongoDB', 'Thành thạo cả Frontend lẫn Backend, xây dựng ứng dụng hoàn chỉnh từ A-Z.', 'https://bkap.edu.vn/thumbnails/fullstack.png', 'https://youtube.com/embed/xyz1', 'ALL', 1, 1399000, 3299000, 'PAID', 4.6, 316, 1240, 498, '389h17p'),

-- Thuộc DM 2: Frontend Developer
(2, 3, 'Frontend Developer', 'frontend-developer', 'React • Tailwind • Figma', 'Làm chủ giao diện website hiện đại, tối ưu UX/UI và responsive tốt.', 'https://bkap.edu.vn/thumbnails/frontend.png', 'https://youtube.com/embed/xyz2', 'BEGINNER', 0, 299000, 400000, 'PAID', 4.5, 102, 890, 27, '6h18p'),
(2, 4, 'Tailwind CSS', 'tailwind-css-free', 'Build UI nhanh hơn', 'Học cách thiết kế giao diện cực nhanh bằng Utility-first CSS framework.', 'https://bkap.edu.vn/thumbnails/tailwind.png', 'https://youtube.com/embed/xyz4', 'BEGINNER', 0, 0, 0, 'FREE', 5.0, 0, 55, 6, '1h53p'),
(2, 3, 'JavaScript Masterclass', 'javascript-masterclass', 'ES6+ • Async • Web API', 'Nền tảng vững chắc nhất về ngôn ngữ core của mọi lập trình viên Frontend.', 'https://bkap.edu.vn/thumbnails/js.png', 'https://youtube.com/embed/xyz7', 'BEGINNER', 0, 199000, 599000, 'PAID', 4.7, 85, 620, 80, '20h30p'),

-- Thuộc DM 3: AI & Data Science
(3, 1, 'AI & Data Science', 'ai-data-science-pro', 'Python • TensorFlow • ML', 'Khai phá sức mạnh dữ liệu, xây dựng mô hình học máy và trí tuệ nhân tạo.', 'https://bkap.edu.vn/thumbnails/ai.png', 'https://youtube.com/embed/xyz3', 'ADVANCED', 1, 1399000, 3299000, 'PAID', 4.8, 612, 540, 296, '55h49p'),
(3, 1, 'Python Data Analysis', 'python-data-analysis', 'Pandas • NumPy • PowerBI', 'Thành thạo phân tích, xử lý dữ liệu và trực quan hóa báo cáo doanh nghiệp.', 'https://bkap.edu.vn/thumbnails/pydata.png', 'https://youtube.com/embed/xyz9', 'BEGINNER', 0, 599000, 1200000, 'PAID', 4.4, 45, 180, 55, '16h10p'),

-- Thuộc DM 4: Lập trình di động
(4, 2, 'Mobile Developer', 'mobile-developer-pro', 'React Native • Expo', 'Xây dựng ứng dụng di động đa nền tảng cho cả iOS và Android.', 'https://bkap.edu.vn/thumbnails/mobile.png', 'https://youtube.com/embed/xyz5', 'INTERMEDIATE', 0, 999000, 2500000, 'PAID', 4.3, 87, 498, 120, '25h00p'),

-- Thuộc DM 5: DevOps & Cloud
(5, 1, 'DevOps & Cloud AWS', 'devops-cloud-aws', 'Docker • AWS • K8s', 'Làm chủ hạ tầng đám mây, tự động hóa quy trình CI/CD chuẩn doanh nghiệp.', 'https://bkap.edu.vn/thumbnails/devops.png', 'https://youtube.com/embed/xyz6', 'ADVANCED', 1, 0, 0, 'CONTACT', 4.7, 198, 230, 150, '70h15p'),
(5, 5, 'Google Cloud Platform (GCP)', 'google-cloud-platform', 'GCP • GKE • IAM', 'Tìm hiểu dịch vụ đám mây của Google và cách kiến trúc hệ thống lớn.', 'https://bkap.edu.vn/thumbnails/gcp.png', 'https://youtube.com/embed/xyz10', 'INTERMEDIATE', 0, 0, 0, 'CONTACT', 4.2, 12, 75, 40, '18h00p'),
(1, 4, 'Node.js Backend Developer', 'nodejs-backend-developer', 'Express • MySQL • JWT', 'Xây dựng hệ thống RESTful API bảo mật, hiệu năng cao với Express và MySQL.', 'https://bkap.edu.vn/thumbnails/nodejs.png', 'https://youtube.com/embed/xyz8', 'INTERMEDIATE', 1, 799000, 1890000, 'PAID', 4.6, 92, 340, 95, '35h20p');

INSERT INTO Enrollment (user_id, course_id, status, progress_percent) VALUES
(2, 2, 'IN_PROGRESS', 75), -- User 2 đang học khóa Frontend Developer (Tiến độ 75%)
(2, 1, 'COMPLETED', 100);  -- User 2 đã hoàn thành khóa Full-Stack Developer

-- 1. Cập nhật ảnh cho khóa học: Full-Stack Developer
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'full-stack-developer';

-- 2. Cập nhật ảnh cho khóa học: Frontend Developer
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'frontend-developer';

-- 3. Cập nhật ảnh cho khóa học: Tailwind CSS
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1542831371-29b0f74f9713?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'tailwind-css-free';

-- 4. Cập nhật ảnh cho khóa học: JavaScript Masterclass
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1579468118864-1b9ea3c0db4a?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'javascript-masterclass';

-- 5. Cập nhật ảnh cho khóa học: AI & Data Science
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1677442136019-21780efad99a?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'ai-data-science-pro';

-- 6. Cập nhật ảnh cho khóa học: Python Data Analysis
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'python-data-analysis';

-- 7. Cập nhật ảnh cho khóa học: Mobile Developer
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'mobile-developer-pro';

-- 8. Cập nhật ảnh cho khóa học: DevOps & Cloud AWS
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1607799279861-4dd421887fb3?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'devops-cloud-aws';

-- 9. Cập nhật ảnh cho khóa học: Google Cloud Platform (GCP)
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'google-cloud-platform';

-- 10. Cập nhật ảnh cho khóa học: Node.js Backend Developer
UPDATE Course 
SET thumbnail_url = 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600&auto=format&fit=crop&q=80'
WHERE slug = 'nodejs-backend-developer';

-- Xác nhận kiểm tra lại dữ liệu sau khi cập nhật thành công
SELECT id, title, slug, thumbnail_url FROM Course;