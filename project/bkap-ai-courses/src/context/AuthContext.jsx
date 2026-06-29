import React, {
  createContext,
  useContext,
  useState,
  useCallback,
  useEffect,
} from "react";
import { loginApi } from "../api/LoginApi";
import { registerApi } from "../api/RegisterApi";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [authModal, setAuthModal] = useState(null); // "login" | "register" | null

  /**
   * HÀM HELPER: Chuẩn hóa dữ liệu user và lưu vào localStorage
   * Tách ra để dùng chung cho cả Login và Register
   */
  const handleAuthSuccess = useCallback((serverUser, password) => {
    console.log("=== handleAuthSuccess ===");
    console.log("serverUser:", serverUser);
    console.log("password nhận được:", password);
    console.log("========================");

    // 1. Lưu token
    if (serverUser.token) {
      localStorage.setItem("token", serverUser.token);
    }

    // 2. Lưu password vào sessionStorage (giữ nguyên)
    if (password) {
      sessionStorage.setItem("_mp", password);
    }

    // 3. Chuẩn hóa object user — ✅ THÊM password vào đây
    const authenticatedUser = {
      ...serverUser,
      name: serverUser.user?.name || serverUser.user?.username,
      email: serverUser.user?.email,
      username: serverUser.user?.username,
      fullname: serverUser.user?.name || serverUser.user?.fullname,
      password: password,
      avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(
        serverUser.user?.name || serverUser.user?.username || "User",
      )}&background=C8102E&color=fff`,
    };

    // ⚠️ KHÔNG lưu password vào localStorage vì lý do bảo mật
    // Chỉ lưu user không có password
    const userForStorage = { ...authenticatedUser };
    delete userForStorage.password;
    localStorage.setItem("user", JSON.stringify(userForStorage));

    // 4. State có password để dùng trong session
    setUser(authenticatedUser);
    setAuthModal(null);
  }, []);

  /**
   * HÀM LOGIN: Đăng nhập bằng username + mật khẩu
   */
  const login = useCallback(
    async (username, password) => {
      try {
        const response = await loginApi(username, password);
        if (response && response.data) {
          // Gọi helper xử lý lưu trữ dữ liệu đăng nhập
          handleAuthSuccess(response.data, password);
          return { success: true };
        }
      } catch (error) {
        return {
          success: false,
          message: "Tên đăng nhập hoặc mật khẩu không đúng!",
        };
      }
    },
    [handleAuthSuccess],
  );

  /**
   * HÀM REGISTER: Đăng ký thành viên mới
   * ✅ ĐÃ CẬP NHẬT: Đăng ký thành công sẽ tự động đăng nhập luôn
   */
  const register = useCallback(
    async (fullName, username, email, phone, birthday, password) => {
      try {
        const response = await registerApi(
          fullName,
          username,
          email,
          phone,
          birthday,
          password,
        );

        // Theo hình Postman 1 và 3, API Register trả về HTTP Status 201 cùng dữ liệu token/user giống API Login
        if (response && (response.status === 201 || response.data)) {
          const serverUser = response.data;

          // Tiến hành lưu token, user vào localStorage và đăng nhập ngay lập tức
          handleAuthSuccess(serverUser, password);

          return { success: true };
        }
        return {
          success: false,
          message: "Đăng ký không thành công. Vui lòng thử lại.",
        };
      } catch (error) {
        console.error("Register Error:", error);
        return {
          success: false,
          message:
            error.response?.data?.message ||
            "Tên đăng nhập hoặc Email đã tồn tại!",
        };
      }
    },
    [handleAuthSuccess],
  );

  // Khôi phục user + token từ localStorage khi reload trang
  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);

      // ✅ Khôi phục password từ sessionStorage khi reload
      const savedPassword = sessionStorage.getItem("_mp");
      if (savedPassword) {
        parsedUser.password = savedPassword;
      }

      setUser(parsedUser);
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    sessionStorage.removeItem("_mp");
    setUser(null);
  }, []);

  const openLogin = useCallback(() => setAuthModal("login"), []);
  const openRegister = useCallback(() => setAuthModal("register"), []);
  const closeModal = useCallback(() => setAuthModal(null), []);

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        register,
        authModal,
        openLogin,
        openRegister,
        closeModal,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
