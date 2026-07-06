package com.bkap.UserModule.backend.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bkap.UserModule.dto.MoodleUserRequest;
import com.bkap.config.MoodleConfig;

@Service
public class MoodleService implements IMoodleService {

	@Autowired
	private MoodleConfig moodleConfig;

	// RestTemplate dùng để bắn request HTTP ẩn sang Moodle
	private final RestTemplate restTemplate = new RestTemplate();

	// =========================================================
	// PHƯƠNG THỨC 1: TẠO TÀI KHOẢN MOODLE (BẢN CHUẨN HÓA URL)
	// =========================================================
	@Override
	public String createMoodleUser(MoodleUserRequest user) {
		try {
			if (user == null || user.getUsername() == null || user.getPassword() == null) {
				System.err.println("[MoodleService] Dữ liệu user bị null!");
				return null;
			}

			String url = moodleConfig.getBaseUrl() + "?wstoken=" + moodleConfig.getToken()
					+ "&wsfunction=core_user_create_users" + "&moodlewsrestformat=" + moodleConfig.getWsFormat();

			String cleanUsername = user.getUsername().trim().toLowerCase();
			String cleanPassword = user.getPassword().trim();
			String cleanEmail = user.getEmail().trim().toLowerCase();

			// ✅ FIX: Bỏ dấu tiếng Việt trước khi gửi Moodle
			String cleanFirstName = removeAccents(user.getFirstname() != null ? user.getFirstname().trim() : "User");
			String cleanLastName = removeAccents(user.getLastname() != null ? user.getLastname().trim() : "Khach");

			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("users[0][username]", cleanUsername);
			formData.add("users[0][password]", cleanPassword);
			formData.add("users[0][firstname]", cleanFirstName);
			formData.add("users[0][lastname]", cleanLastName);
			formData.add("users[0][email]", cleanEmail);
			formData.add("users[0][auth]", "manual");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

			System.out.println("[MoodleService] Gửi dữ liệu: " + formData);
			String response = restTemplate.postForObject(url, requestEntity, String.class);
			System.out.println("[MoodleService] Moodle trả về: " + response);

			return response;

		} catch (Exception e) {
			System.err.println("[MoodleService] Lỗi: " + e.getMessage());
			return null;
		}
	}

	// ✅ Hàm bỏ dấu tiếng Việt — dùng java.text.Normalizer có sẵn, không cần thêm
	// thư viện
	private String removeAccents(String input) {
		if (input == null) {
			return "";
		}
		// NFD tách ký tự thành base + dấu, replaceAll bỏ phần dấu (\\p{M})
		return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "")
				// Đ/đ không thuộc NFD nên phải xử lý riêng
				.replace("Đ", "D").replace("đ", "d");
	}

	// =========================================================
	// PHƯƠNG THỨC 2: GHI DANH VÀO KHÓA HỌC
	// =========================================================
	@Override
	public String enrolUserToCourse(int courseId, int moodleUserId) {
		try {
			String url = moodleConfig.getBaseUrl() + "?wstoken=" + moodleConfig.getToken()
					+ "&wsfunction=enrol_manual_enrol_users" + "&moodlewsrestformat=" + moodleConfig.getWsFormat()
					+ "&enrolments[0][roleid]=5" + "&enrolments[0][userid]=" + moodleUserId
					+ "&enrolments[0][courseid]=" + courseId;

			System.out.println("[MoodleService] Đường dẫn API ghi danh: " + url);

			String response = restTemplate.postForObject(url, null, String.class);
			return response;

		} catch (Exception e) {
			System.err.println("[MoodleService] Lỗi ghi danh: " + e.getMessage());
			return null;
		}
	}

	// =========================================================
	// PHƯƠNG THỨC 3: LẤY URL AUTO-LOGIN (SSO)
	// =========================================================
	// @Override
	// public String getMoodleAutoLoginUrl(String username, String password) {
	// try {
	// String tokenUrl = "https://lms.buni.vn/login/token.php" + "?username=" +
	// username.trim().toLowerCase()
	// + "&password=" + password.trim() + "&service=bkap_courses"; // ← đổi thành
	// short name đúng sau khi
	// // hỏi thầy
	//
	// System.out.println("[MoodleService] Gọi token URL: " + tokenUrl);
	//
	// String response = restTemplate.getForObject(tokenUrl, String.class);
	// System.out.println("[MoodleService] Moodle trả về: " + response);
	//
	// if (response == null) {
	// return null;
	// }
	//
	// // ✅ Parse JSON đúng cách bằng Jackson
	// com.fasterxml.jackson.databind.ObjectMapper mapper = new
	// com.fasterxml.jackson.databind.ObjectMapper();
	// com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(response);
	//
	// // Nếu có token → thành công
	// if (node.has("token")) {
	// String token = node.get("token").asText();
	// // Trả về URL auto-login + redirect thẳng vào Dashboard
	// return "https://lms.buni.vn/login/index.php" + "?token=" + token +
	// "&wantsurl=https://lms.buni.vn/my/";
	// }
	//
	// // Nếu có lỗi → log ra và trả null
	// if (node.has("error")) {
	// System.err.println("[MoodleService] Moodle lỗi: " +
	// node.get("error").asText() + " | errorcode: "
	// + node.get("errorcode").asText());
	// }
	//
	// return null;
	//
	// } catch (Exception e) {
	// System.err.println("[MoodleService] Lỗi getMoodleAutoLoginUrl: " +
	// e.getMessage());
	// return null;
	// }
	// }

	@Override
	public String getMoodleAutoLoginUrl(String username, String password) {
		try {
			if (username == null) {
				return null;
			}

			String cleanUsername = username.trim().toLowerCase();

			// Gọi auth_userkey để lấy login URL dùng 1 lần
			// Token này là moodle.token trong application.properties
			String url = moodleConfig.getBaseUrl() + "?wstoken=" + moodleConfig.getToken()
					+ "&wsfunction=auth_userkey_request_login_url" + "&moodlewsrestformat=json" + "&user[username]="
					+ cleanUsername;

			System.out.println("[MoodleService] Gọi userkey URL: " + url);

			String response = restTemplate.postForObject(url, null, String.class);
			System.out.println("[MoodleService] Moodle trả về: " + response);

			// Parse JSON lấy loginurl
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(response);

			if (node.has("exception")) {
				System.err.println("[MoodleService] Lỗi Moodle: " + node.get("message").asText());
				return null;
			}

			if (node.has("loginurl")) {
				return node.get("loginurl").asText();
			}

			System.err.println("[MoodleService] Không có loginurl trong response!");
			return null;

		} catch (Exception e) {
			System.err.println("[MoodleService] Lỗi: " + e.getMessage());
			return null;
		}
	}
}