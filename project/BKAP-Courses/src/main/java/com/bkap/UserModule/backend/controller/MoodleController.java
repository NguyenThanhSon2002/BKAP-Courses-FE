package com.bkap.UserModule.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bkap.UserModule.backend.service.IMoodleService;
import com.bkap.UserModule.backend.service.IUserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/moodle")
@CrossOrigin(origins = "*")
public class MoodleController {

	@Autowired
	private IMoodleService moodleService;

	@Autowired
	private IUserService userService;

	@Value("${moodle.base-url}")
	private String moodleWsUrl; // https://lms.buni.vn/webservice/rest/server.php

	@Value("${moodle.token}")
	private String moodleToken; // 4d85d3304c8d064aac976c809ba2cefd

	@Value("${moodle.ws-format}")
	private String wsFormat; // json

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	// ─────────────────────────────────────────────
	// ENDPOINT 1: LẤY URL AUTO-LOGIN (SSO)
	// ─────────────────────────────────────────────

	// @PostMapping("/autologin-url")
	// public ResponseEntity<?> getAutoLoginUrl(@RequestBody Map<String, String>
	// body) {
	// try {
	// String username = body.get("username");
	//
	// if (username == null || username.isEmpty()) {
	// return ResponseEntity.status(400).body("Thiếu username!");
	// }
	//
	// // Tìm user trong DB để lấy password
	// User user = userService.findByUsername(username);
	// if (user == null) {
	// return ResponseEntity.status(404).body("Không tìm thấy user: " + username);
	// }
	//
	// // Gọi service — trả về URL đầy đủ hoặc null nếu lỗi
	// String loginUrl = moodleService.getMoodleAutoLoginUrl(username,
	// user.getPassword());
	//
	// if (loginUrl != null) {
	// // loginUrl đã có dạng:
	// // https://lms.buni.vn/login/index.php?token=xxx&wantsurl=...
	// return ResponseEntity.ok(Map.of("loginUrl", loginUrl));
	// }
	//
	// // loginUrl null → Moodle đã log lỗi trong service, trả về 500 cho frontend
	// return ResponseEntity.status(500).body("Không thể tạo auto-login URL. Kiểm
	// tra: "
	// + "1) Username đã tồn tại trên Moodle chưa? " + "2) Tên service có đúng
	// không?");
	//
	// } catch (Exception e) {
	// System.err.println("[MoodleController] Lỗi autologin: " + e.getMessage());
	// return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
	// }
	// }

	@PostMapping("/autologin-url")
	public ResponseEntity<?> getMoodleAutoLoginUrl(@RequestBody Map<String, String> body) {
		try {
			String username = body.get("username");
			String password = body.get("password");
			String email = body.get("email");
			String fullname = body.get("fullname");

			if (username == null || username.trim().isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("error", "Thiếu username"));
			}

			username = username.trim().toLowerCase();
			if (email == null || email.trim().isEmpty()) {
				email = username + "@buni.vn";
			}

			// Parse firstname / lastname từ fullname
			String firstname = username, lastname = "Student";
			if (fullname != null && !fullname.trim().isEmpty()) {
				fullname = fullname.trim();
				int idx = fullname.lastIndexOf(" ");
				if (idx != -1) {
					firstname = fullname.substring(idx + 1);
					lastname = fullname.substring(0, idx);
				} else {
					firstname = fullname;
				}
			}

			// BƯỚC 1: Kiểm tra user đã tồn tại trên Moodle chưa
			String checkUrl = moodleWsUrl + "?wstoken=" + moodleToken + "&wsfunction=core_user_get_users_by_field"
					+ "&moodlewsrestformat=json" + "&field=username&values[0]=" + username;

			String checkResponse = restTemplate.getForObject(checkUrl, String.class);
			JsonNode usersArray = objectMapper.readTree(checkResponse);

			// BƯỚC 2: Tạo user Moodle nếu chưa có
			if (usersArray == null || usersArray.size() == 0) {
				com.bkap.UserModule.dto.MoodleUserRequest dto = new com.bkap.UserModule.dto.MoodleUserRequest();
				dto.setUsername(username);
				dto.setPassword(password);
				dto.setEmail(email);
				dto.setFirstname(firstname);
				dto.setLastname(lastname);
				moodleService.createMoodleUser(dto);
				System.out.println("[MoodleController] Tạo user Moodle: " + username);
			} else {
				System.out.println("[MoodleController] User đã có: " + username);
			}

			// BƯỚC 3: Lấy login URL SSO qua auth_userkey
			String loginUrl = moodleService.getMoodleAutoLoginUrl(username, password);

			if (loginUrl != null) {
				return ResponseEntity.ok(Map.of("loginUrl", loginUrl));
			}

			return ResponseEntity.status(500)
					.body(Map.of("error", "Không lấy được SSO URL. Kiểm tra plugin auth_userkey đã bật chưa?"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Lỗi: " + e.getMessage()));
		}
	}

	// ─────────────────────────────────────────────
	// ENDPOINT 2: GHI DANH VÀO KHÓA HỌC
	// ─────────────────────────────────────────────
	@PostMapping("/enrol")
	public ResponseEntity<?> enrolToCourse(@RequestBody Map<String, Integer> body) {
		try {
			Integer moodleUserId = body.get("moodleUserId");
			Integer courseId = body.get("courseId");

			if (moodleUserId == null || courseId == null) {
				return ResponseEntity.status(400).body("Thiếu moodleUserId hoặc courseId!");
			}

			String result = moodleService.enrolUserToCourse(courseId, moodleUserId);
			return ResponseEntity.ok(Map.of("message", "Ghi danh thành công!", "moodleResponse",
					result != null ? result : "null (thành công)"));

		} catch (Exception e) {
			System.err.println("[MoodleController] Lỗi enrol: " + e.getMessage());
			return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
		}
	}

	@GetMapping("/logintoken")
	public ResponseEntity<?> getMoodleLoginToken() {
		try {
			// GET trang login để lấy logintoken từ HTML
			RestTemplate restTemplate = new RestTemplate();
			String html = restTemplate.getForObject("https://lms.buni.vn/login/index.php", String.class);

			if (html == null) {
				return ResponseEntity.status(500).body(Map.of("error", "Không lấy được trang login Moodle"));
			}

			// Parse logintoken bằng regex
			java.util.regex.Pattern pattern = java.util.regex.Pattern
					.compile("name=\"logintoken\"\\s+value=\"([^\"]+)\"");
			java.util.regex.Matcher matcher = pattern.matcher(html);

			if (matcher.find()) {
				String logintoken = matcher.group(1);
				System.out.println("[MoodleController] logintoken: " + logintoken);
				return ResponseEntity.ok(Map.of("logintoken", logintoken));
			}

			return ResponseEntity.status(500).body(Map.of("error", "Không tìm thấy logintoken trong HTML"));

		} catch (Exception e) {
			System.err.println("[MoodleController] Lỗi lấy logintoken: " + e.getMessage());
			return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/sso-redirect")
	public void ssoRedirect(@RequestParam String username, @RequestParam String password,
			jakarta.servlet.http.HttpServletResponse response) throws Exception {

		// BƯỚC 1: Lấy MoodleSession từ service
		String result = moodleService.getMoodleAutoLoginUrl(username, password);
		if (result == null) {
			response.sendRedirect("https://lms.buni.vn/login/index.php");
			return;
		}

		com.fasterxml.jackson.databind.JsonNode json = new com.fasterxml.jackson.databind.ObjectMapper()
				.readTree(result);

		String moodleSession = json.get("moodleSession").asText();
		// Tách "MoodleSession=value" thành name và value
		String[] parts = moodleSession.split("=", 2);
		String cookieName = parts[0];
		String cookieValue = parts[1];

		// BƯỚC 2: Set cookie vào response rồi redirect về Dashboard
		jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(cookieName, cookieValue);
		cookie.setDomain("lms.buni.vn");
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setMaxAge(7200); // 2 giờ
		response.addCookie(cookie);

		// BƯỚC 3: Redirect thẳng về Dashboard Moodle
		response.sendRedirect("https://lms.buni.vn/my/");
	}
}