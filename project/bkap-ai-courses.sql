DROP DATABASE IF EXISTS bkap_courses;
CREATE DATABASE bkap_courses;
USE bkap_courses;

-- ------------------------------------------------------------
-- Bảng 1: User	
-- Lưu thông tin cá nhân của khách hàng
-- ------------------------------------------------------------
DROP TABLE IF EXISTS User;
CREATE TABLE User (
    id          INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullname    VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    phone       VARCHAR(10)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    birthday    VARCHAR(12)     NOT NULL,
    role        ENUM('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT',
    createDate  DATETIME        DEFAULT NOW(),
    last_login  DATETIME        NULL,
    last_logout DATETIME        NULL,
    is_online   TINYINT(1)      DEFAULT 0
);

-- ================================================================
-- BKAP_COURSES: Optimized Schema
-- ================================================================

-- ================================================================
-- 1. BẢNG DANH MỤC KHÓA HỌC
-- ================================================================
DROP TABLE IF EXISTS Category;
CREATE TABLE Category (
    id          INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    slug        VARCHAR(100)    NOT NULL UNIQUE,
    status      VARCHAR(20)     NOT NULL DEFAULT 'active',
    prioty      INT             NOT NULL DEFAULT 0,
    created_at  DATETIME        DEFAULT NOW(),
    updated_at  DATETIME        DEFAULT NOW() ON UPDATE NOW()
);

-- ================================================================
-- 2. BẢNG KHÓA HỌC (Một bảng duy nhất lưu data, liên kết 2 bảng ngoại)
-- ================================================================
DROP TABLE IF EXISTS Course;
CREATE TABLE Course (
    id                  INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category_id         INT UNSIGNED    NOT NULL,
    instructor_id       INT UNSIGNED    NOT NULL,
    title               VARCHAR(200)    NOT NULL,
    slug                VARCHAR(200)    NOT NULL UNIQUE,
    subtitle            VARCHAR(300),
    description         TEXT,
    thumbnail_url       VARCHAR(500),
    preview_video_url   VARCHAR(500),
    level               ENUM('BEGINNER','INTERMEDIATE','ADVANCED','ALL') DEFAULT 'ALL',
    is_pro              TINYINT(1)      DEFAULT 0,
    price               DECIMAL(12,0)   DEFAULT 0,
    original_price      DECIMAL(12,0)   DEFAULT 0,
    price_type          ENUM('PAID','FREE','CONTACT') DEFAULT 'PAID',
    rating              DECIMAL(2,1)    DEFAULT 0,
    rating_count        INT             DEFAULT 0,
    student_count       INT             DEFAULT 0,
    lesson_count        INT             DEFAULT 0,
    duration_text       VARCHAR(50),
    created_at          DATETIME        DEFAULT NOW(),
    updated_at          DATETIME        DEFAULT NOW() ON UPDATE NOW(),

    FOREIGN KEY (category_id)   REFERENCES Category(id),
    FOREIGN KEY (instructor_id) REFERENCES User(id)
);

-- ================================================================
-- 3. BẢNG ĐĂNG KÝ KHÓA HỌC & TIẾN ĐỘ
-- ================================================================
DROP TABLE IF EXISTS Enrollment;
CREATE TABLE Enrollment (
    id               INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id          INT UNSIGNED NOT NULL,
    course_id        INT UNSIGNED NOT NULL,
    status           ENUM('ENROLLED','IN_PROGRESS','COMPLETED') NOT NULL DEFAULT 'ENROLLED',
    progress_percent INT          DEFAULT 0,
    enrolled_at      DATETIME     DEFAULT NOW(),
    last_studied_at  DATETIME     DEFAULT NOW() ON UPDATE NOW(),

    UNIQUE KEY unique_user_course (user_id, course_id),
    FOREIGN KEY (user_id)   REFERENCES User(id)   ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE
);

-- ================================================================
-- DATA MẪU: USER
-- ================================================================
INSERT INTO User (fullname, email, username, phone, password, birthday, role) VALUES
('Phạm Minh Đức',  'phamminhduc@gmail.com',  'mducpham', '0901234564', 'Test@123456', '1998-12-25', 'ADMIN'),
('Nguyễn Văn An',  'nguyenvanan@gmail.com',  'vanan',    '0901234561', 'Test@123456', '2000-05-15', 'STUDENT'),
('Trần Thị Bình',  'tranthibibinh@gmail.com','thibinh',  '0901234562', 'Test@123456', '1999-08-20', 'STUDENT'),
('Lê Hoàng Cường', 'lehoangcuong@gmail.com', 'hcuong',   '0901234563', 'Test@123456', '2001-03-10', 'STUDENT'),
('Hoàng Thị Em',   'hoangthiem@gmail.com',   'thiem',    '0901234565', 'Test@123456', '2002-07-04', 'STUDENT');

-- ================================================================
-- DATA MẪU: CATEGORY (10 bản ghi)
-- ================================================================
INSERT INTO Category (name, slug, status, prioty) VALUES
('Full-Stack Developer', 'full-stack',       'active',   1),
('Frontend Developer',   'frontend',         'active',   2),
('AI & Data Science',    'ai-data-science',  'active',   3),
('Lập trình di động',    'mobile-developer', 'active',   4),
('DevOps & Cloud',       'devops-cloud',     'active',   5),
('Backend Developer',    'backend',          'active',   6),
('Lập trình C/C++',      'c-cpp',            'active',   7),
('Cơ sở dữ liệu',        'database',         'active',   8),
('Bảo mật mạng',         'security',         'inactive', 9),
('UI/UX Design',         'uiux-design',      'active',   10);

-- ================================================================
-- DATA MẪU: COURSE (10 bản ghi)
-- ================================================================
INSERT INTO Course (category_id, instructor_id, title, slug, subtitle, description, thumbnail_url, level, is_pro, price, original_price, price_type, rating, rating_count, student_count, lesson_count, duration_text) VALUES
(1, 2, 'Full-Stack Developer',       'full-stack-developer',    'React • Node.js • MongoDB',  'Thành thạo cả Frontend lẫn Backend.',        'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=600', 'ALL',          1, 1399000, 3299000, 'PAID',    4.6, 316, 1240, 498, '389h17p'),
(2, 3, 'Frontend Developer',         'frontend-developer',      'React • Tailwind • Figma',   'Làm chủ giao diện website hiện đại.',        'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=600', 'BEGINNER',     0,  299000,  400000, 'PAID',    4.5, 102,  890,  27, '6h18p'),
(2, 4, 'Tailwind CSS',               'tailwind-css-free',       'Build UI nhanh hơn',         'Thiết kế giao diện cực nhanh.',              'https://images.unsplash.com/photo-1542831371-29b0f74f9713?w=600', 'BEGINNER',     0,       0,       0, 'FREE',    5.0,   0,   55,   6, '1h53p'),
(2, 3, 'JavaScript Masterclass',     'javascript-masterclass',  'ES6+ • Async • Web API',     'Nền tảng vững chắc về JavaScript.',          'https://images.unsplash.com/photo-1579468118864-1b9ea3c0db4a?w=600', 'BEGINNER',     0,  199000,  599000, 'PAID',    4.7,  85,  620,  80, '20h30p'),
(3, 1, 'AI & Data Science',          'ai-data-science-pro',     'Python • TensorFlow • ML',   'Xây dựng mô hình học máy và AI.',            'https://images.unsplash.com/photo-1677442136019-21780efad99a?w=600', 'ADVANCED',     1, 1399000, 3299000, 'PAID',    4.8, 612,  540, 296, '55h49p'),
(3, 1, 'Python Data Analysis',       'python-data-analysis',    'Pandas • NumPy • PowerBI',   'Phân tích và trực quan hóa dữ liệu.',        'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600', 'BEGINNER',     0,  599000, 1200000, 'PAID',    4.4,  45,  180,  55, '16h10p'),
(4, 2, 'Mobile Developer',           'mobile-developer-pro',    'React Native • Expo',        'Xây dựng app di động đa nền tảng.',          'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=600', 'INTERMEDIATE', 0,  999000, 2500000, 'PAID',    4.3,  87,  498, 120, '25h00p'),
(5, 1, 'DevOps & Cloud AWS',         'devops-cloud-aws',        'Docker • AWS • K8s',         'Làm chủ hạ tầng đám mây và CI/CD.',         'https://images.unsplash.com/photo-1607799279861-4dd421887fb3?w=600', 'ADVANCED',     1,       0,       0, 'CONTACT', 4.7, 198,  230, 150, '70h15p'),
(5, 5, 'Google Cloud Platform',      'google-cloud-platform',   'GCP • GKE • IAM',            'Kiến trúc hệ thống lớn trên Google Cloud.',  'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600', 'INTERMEDIATE', 0,       0,       0, 'CONTACT', 4.2,  12,   75,  40, '18h00p'),
(1, 4, 'Node.js Backend Developer',  'nodejs-backend-developer','Express • MySQL • JWT',       'Xây dựng RESTful API bảo mật hiệu năng cao.','https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600', 'INTERMEDIATE', 1,  799000, 1890000, 'PAID',    4.6,  92,  340,  95, '35h20p');

-- ================================================================
-- DATA MẪU: ENROLLMENT
-- ================================================================
INSERT INTO Enrollment (user_id, course_id, status, progress_percent) VALUES
(2, 2, 'IN_PROGRESS', 75),
(2, 1, 'COMPLETED',  100);

-- ================================================================
-- KIỂM TRA
-- ================================================================
SELECT * FROM User;
SELECT * FROM Category ORDER BY prioty;
SELECT * FROM Course;
SELECT * FROM Enrollment;

DESCRIBE Bkap_Courses.User;