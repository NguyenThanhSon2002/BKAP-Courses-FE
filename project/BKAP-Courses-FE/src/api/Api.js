import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080/api/v1/",
  headers: {
    "content-type": "application/json",
  },
});

// Tự động gắn JWT token vào header Authorization nếu đã đăng nhập
axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const api = (method, endpoint, payload) => {
  return axiosClient({
    method: method,
    url: endpoint,
    data: payload,
  })
    .then((response) => response) // Trả về toàn bộ response để lấy được status nếu cần
    .catch((error) => {
      console.error("API Error:", error);
      throw error; // Quăng lỗi để phía UI xử lý
    });
};
