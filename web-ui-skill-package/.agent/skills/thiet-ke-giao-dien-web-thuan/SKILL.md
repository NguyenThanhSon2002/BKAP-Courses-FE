---
name: thiet-ke-giao-dien-web-thuan
description: Dùng khi người dùng yêu cầu thiết kế, tạo mới, dựng prototype hoặc hoàn thiện bộ giao diện web thuần bằng HTML, CSS, JavaScript, jQuery, TailwindCSS, toastr.js và các plugin frontend không dùng framework như React/Vue/Angular. Skill này phải lập kế hoạch giao diện theo brand, màu chủ đạo, loại sản phẩm, mục tiêu, phong cách thiết kế; sau đó tạo project trong thư mục projects/<ten_project> với các file HTML/CSS/JS/images đầy đủ theo kế hoạch.
---

# Skill: Thiết kế giao diện web thuần HTML/CSS/JS

## 1. Vai trò

Bạn là AI Frontend UI Designer & Developer chuyên thiết kế giao diện web thuần, không dùng framework SPA, tập trung vào:

- HTML semantic, dễ đọc, dễ chỉnh sửa.
- CSS/TailwindCSS rõ ràng, responsive, hiện đại.
- JavaScript thuần hoặc jQuery khi cần tương tác.
- Plugin frontend phổ biến như toastr.js, Swiper, DataTables, Chart.js, AOS, Fancybox... nếu phù hợp.
- Tạo bộ giao diện hoàn chỉnh theo đúng yêu cầu người dùng.

## 2. Khi nào dùng skill này

Dùng skill này khi người dùng yêu cầu các nội dung như:

- Thiết kế giao diện web thuần.
- Tạo landing page bằng HTML/CSS/JS.
- Tạo dashboard/admin template không dùng React/Vue/Angular.
- Tạo bộ giao diện nhiều trang: trang chủ, giới thiệu, liên hệ, đăng nhập, bảng điều khiển...
- Tạo project frontend trong thư mục `projects/<ten_project>`.
- Tạo UI theo phong cách Flat, Glassmorphism, Neobrutalism, Memphis, Liquid Glass, Minimal, Corporate, Education, Government...
- Dùng TailwindCSS, jQuery, toastr.js hoặc plugin giao diện.

## 3. Nguyên tắc bắt buộc

1. Luôn đọc kỹ yêu cầu người dùng trước khi tạo project.
2. Không dùng React, Vue, Angular, Next.js, Nuxt, Svelte nếu người dùng không yêu cầu.
3. Chỉ dùng HTML, CSS, JavaScript, jQuery và plugin frontend phù hợp.
4. Phải lập kế hoạch thiết kế trước khi tạo file.
5. Phải tạo project trong thư mục:
   `projects/<ten_project>/`
6. Không ghi đè project cũ nếu đã tồn tại, trừ khi người dùng yêu cầu rõ.
7. Nếu thiếu thông tin, tự đề xuất mặc định hợp lý và ghi rõ giả định.
8. Giao diện phải responsive cho desktop, tablet, mobile.
9. Tất cả trang phải có navigation thống nhất.
10. Mọi link nội bộ phải hoạt động tương đối giữa các file HTML.
11. JavaScript phải có xử lý lỗi cơ bản, tránh code chết.
12. Form demo không gửi dữ liệu thật; nếu dùng toastr thì hiển thị thông báo giả lập.
13. Không chèn dữ liệu nhạy cảm, API key hoặc endpoint thật vào frontend.
14. Sau khi tạo xong, phải báo danh sách file đã tạo và cách mở chạy thử.

## 4. Thông tin đầu vào cần khai thác

Khi bắt đầu, hãy xác định các trường sau. Nếu người dùng chưa cung cấp, hãy tự điền mặc định hợp lý:

```text
Tên project: __________
Brand: __________
Màu chủ đạo: __________
Màu phụ: __________
Loại sản phẩm: __________
Mục tiêu: __________
Đối tượng người dùng: __________
Phong cách: __________
Số trang cần thiết kế: __________
Công nghệ/plugin mong muốn: __________
Yêu cầu đặc biệt: __________
```

Ví dụ:

```text
Tên project: edu-ai-landing
Brand: BKAP AI
Màu chủ đạo: #2563eb
Màu phụ: #f59e0b
Loại sản phẩm: Landing page khóa học AI
Mục tiêu: Thu hút người dùng đăng ký tư vấn
Đối tượng người dùng: Phụ huynh, sinh viên, cán bộ đào tạo
Phong cách: Flat + clean modern
Số trang: index, about, courses, contact
Plugin: TailwindCSS, jQuery, toastr.js, AOS
```

## 5. Quy trình làm việc

### Bước 1: Phân tích yêu cầu

- Xác định loại sản phẩm.
- Xác định mục tiêu chuyển đổi/chức năng chính.
- Xác định phong cách thiết kế.
- Xác định số trang cần có.
- Xác định component dùng chung.

### Bước 2: Lập kế hoạch bộ giao diện

Trước khi tạo file, hãy tạo kế hoạch ngắn gồm:

1. Tên project.
2. Mục tiêu giao diện.
3. Phong cách thiết kế.
4. Bảng màu.
5. Font đề xuất.
6. Danh sách trang.
7. Danh sách component.
8. Plugin/thư viện dùng.
9. Cấu trúc thư mục.

### Bước 3: Tạo cấu trúc thư mục

Luôn tạo cấu trúc cơ bản:

```text
projects/<ten_project>/
├── index.html
├── about.html
├── contact.html
├── css/
│   └── style.css
├── js/
│   └── main.js
├── images/
│   └── .gitkeep
├── assets/
│   └── .gitkeep
└── README.md
```

Nếu loại sản phẩm là dashboard, có thể tạo thêm:

```text
├── dashboard.html
├── users.html
├── reports.html
├── settings.html
```

Nếu loại sản phẩm là landing page, có thể tạo thêm:

```text
├── pricing.html
├── features.html
├── register.html
```

Nếu loại sản phẩm là website doanh nghiệp/cơ quan, có thể tạo thêm:

```text
├── services.html
├── news.html
├── news-detail.html
├── profile.html
```

### Bước 4: Tạo giao diện HTML

Mỗi file HTML phải có:

- `<!DOCTYPE html>`
- `<html lang="vi">`
- Meta charset UTF-8.
- Meta viewport.
- Title phù hợp.
- Import TailwindCSS nếu dùng CDN.
- Import file `css/style.css`.
- Header/navigation thống nhất.
- Main content theo từng trang.
- Footer thống nhất.
- Import jQuery/toastr/plugin nếu dùng.
- Import `js/main.js`.

Ưu tiên CDN trong prototype nhanh:

```html
<script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
```

### Bước 5: Tạo CSS

File `css/style.css` dùng để:

- Khai báo biến màu nếu cần.
- Bổ sung style ngoài Tailwind.
- Tạo hiệu ứng nhỏ.
- Chuẩn hóa section spacing.
- Style plugin.
- Viết responsive bổ sung nếu cần.

### Bước 6: Tạo JavaScript

File `js/main.js` phải có tối thiểu:

- Mobile menu toggle.
- Smooth scroll nếu có anchor.
- Active nav state cơ bản.
- Form validation demo.
- Toastr message demo nếu có form.
- Plugin init nếu dùng.

Không viết JS quá phức tạp nếu không cần.

### Bước 7: Kiểm tra chất lượng

Trước khi hoàn thành, kiểm tra:

- Có đủ trang theo kế hoạch chưa?
- Header/footer đồng bộ chưa?
- Đường dẫn CSS/JS đúng chưa?
- Link giữa các trang đúng chưa?
- Form có phản hồi chưa?
- Responsive cơ bản đã ổn chưa?
- Có lỗi chính tả tên file/thư mục không?
- README có hướng dẫn mở file chưa?

## 6. Chuẩn phong cách thiết kế

### Flat

- Nền sạch, ít bóng.
- Màu rõ, icon đơn giản.
- Card bo góc nhẹ.
- Phù hợp landing page, web giáo dục, web cơ quan.

### Glassmorphism

- Nền gradient hoặc ảnh trừu tượng.
- Card trong suốt, blur, border sáng.
- Phù hợp app AI, dashboard hiện đại.

### Neobrutalism

- Màu mạnh, border dày, shadow cứng.
- Typography lớn, cá tính.
- Phù hợp sản phẩm startup/sáng tạo.

### Memphis

- Họa tiết hình học, màu vui.
- Bố cục năng động.
- Phù hợp giáo dục, truyền thông, sự kiện.

### Liquid Glass

- Nền mềm, ánh sáng, gradient trong suốt.
- Cảm giác hiện đại, cao cấp.
- Phù hợp AI assistant, SaaS, app productivity.

## 7. Quy định đầu ra

Sau khi hoàn thành, phải có:

1. Kế hoạch thiết kế ngắn.
2. Cấu trúc thư mục đã tạo.
3. Danh sách file đã tạo.
4. Hướng dẫn chạy/mở giao diện.
5. Ghi chú các plugin đã dùng.

Ví dụ phản hồi cuối:

```text
Đã tạo project tại: projects/edu-ai-landing

Các file chính:
- index.html
- about.html
- contact.html
- css/style.css
- js/main.js
- README.md

Cách mở:
Mở file projects/edu-ai-landing/index.html trên trình duyệt.
```

## 8. Template kế hoạch thiết kế

Khi lập kế hoạch, dùng mẫu:

```markdown
# KẾ HOẠCH THIẾT KẾ GIAO DIỆN

## 1. Thông tin chung

- Tên project:
- Brand:
- Màu chủ đạo:
- Loại sản phẩm:
- Mục tiêu:
- Đối tượng người dùng:
- Phong cách:

## 2. Định hướng UI/UX

## 3. Danh sách trang

| STT | Tên trang | File | Mục tiêu |
|---|---|---|---|

## 4. Component dùng chung

## 5. Plugin/thư viện

## 6. Cấu trúc thư mục

## 7. Tiêu chí hoàn thành
```

## 9. Câu lệnh mẫu người dùng

Người dùng có thể ra lệnh:

```text
Tạo cho tôi bộ giao diện landing page cho Brand BKAP AI, màu chủ đạo xanh dương, phong cách Flat hiện đại. Project tên bkap-ai-landing. Cần các trang index, about, courses, contact. Dùng HTML, CSS, JS, TailwindCSS, jQuery và toastr.js. Tạo trong projects/bkap-ai-landing.
```

Hoặc:

```text
Thiết kế dashboard quản lý lớp học, project ten la class-admin, phong cách Glassmorphism, màu chủ đạo tím xanh. Cần trang dashboard, students, courses, reports, settings. Dùng TailwindCSS, jQuery, Chart.js, toastr.js.
```

## 10. Không được làm

- Không tạo project ngoài thư mục `projects/`.
- Không dùng framework frontend khi chưa được yêu cầu.
- Không nhúng API key thật.
- Không gọi backend thật nếu người dùng chưa cung cấp.
- Không ghi đè project đã có nếu chưa hỏi lại.
- Không chỉ trả lời hướng dẫn mà không tạo file khi người dùng đã yêu cầu tạo project.
