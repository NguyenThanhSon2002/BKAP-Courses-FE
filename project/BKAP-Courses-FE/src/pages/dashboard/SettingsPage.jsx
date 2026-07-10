import React, { useState, useCallback } from "react";
import { motion } from "framer-motion";
import { Eye, EyeOff, Bell, Globe, Lock, ChevronDown } from "lucide-react";
import Toast from "../../components/dashboard/Toast";

/**
 * Toggle Switch Component đơn giản
 */
function ToggleSwitch({ checked, onChange, id }) {
  return (
    <button
      id={id}
      role="switch"
      aria-checked={checked}
      onClick={() => onChange(!checked)}
      className="relative inline-flex items-center w-11 h-6 rounded-full transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-red-300"
      style={{ background: checked ? "#DC2626" : "#E5E7EB" }}
    >
      <span
        className="inline-block w-4 h-4 rounded-full bg-white shadow-sm transform transition-transform duration-300"
        style={{ transform: checked ? "translateX(22px)" : "translateX(4px)" }}
      />
    </button>
  );
}

/**
 * SettingsPage: Trang Cài Đặt /dashboard/settings
 * - Đổi mật khẩu
 * - Toggle thông báo
 * - Chọn ngôn ngữ
 */
export default function SettingsPage() {
  // ---- State: đổi mật khẩu ----
  const [passwords, setPasswords] = useState({
    current: "",
    newPass: "",
    confirm: "",
  });
  const [showPasswords, setShowPasswords] = useState({
    current: false,
    newPass: false,
    confirm: false,
  });

  // ---- State: thông báo ----
  const [notifications, setNotifications] = useState({
    emailNewCourse: true,
    progressReminder: false,
  });

  // ---- State: ngôn ngữ ----
  const [language, setLanguage] = useState("vi");

  // ---- Toast ----
  const [toast, setToast] = useState({ show: false, message: "", type: "success" });

  const showToast = useCallback((message, type = "success") => {
    setToast({ show: true, message, type });
  }, []);

  const toggleShow = (field) => {
    setShowPasswords((prev) => ({ ...prev, [field]: !prev[field] }));
  };

  const handleSavePassword = (e) => {
    e.preventDefault();
    if (!passwords.current || !passwords.newPass || !passwords.confirm) {
      showToast("Vui lòng điền đầy đủ thông tin!", "error");
      return;
    }
    if (passwords.newPass !== passwords.confirm) {
      showToast("Mật khẩu xác nhận không khớp!", "error");
      return;
    }
    if (passwords.newPass.length < 6) {
      showToast("Mật khẩu mới phải có ít nhất 6 ký tự!", "error");
      return;
    }
    setPasswords({ current: "", newPass: "", confirm: "" });
    showToast("Đổi mật khẩu thành công! 🔐");
  };

  const handleSaveNotifications = () => {
    showToast("Cài đặt thông báo đã được lưu! 🔔");
  };

  const handleSaveLanguage = () => {
    showToast("Ngôn ngữ đã được cập nhật! 🌐");
  };

  // Component Section wrapper
  const Section = ({ icon: Icon, title, children }) => (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
      <div
        className="flex items-center gap-3 px-6 py-4 border-b border-gray-100"
        style={{ background: "#FAFAFA" }}
      >
        <div
          className="w-8 h-8 rounded-lg flex items-center justify-center"
          style={{ background: "#FEE2E2" }}
        >
          <Icon style={{ width: 16, height: 16, color: "#DC2626" }} />
        </div>
        <h2 className="font-extrabold text-gray-800 text-sm">{title}</h2>
      </div>
      <div className="p-6">{children}</div>
    </div>
  );

  // Reusable password input
  const PasswordInput = ({ label, field, placeholder }) => (
    <div>
      <label className="block text-xs font-bold text-gray-600 mb-1.5 uppercase tracking-wide">
        {label}
      </label>
      <div className="relative">
        <input
          type={showPasswords[field] ? "text" : "password"}
          value={passwords[field]}
          onChange={(e) =>
            setPasswords((p) => ({ ...p, [field]: e.target.value }))
          }
          placeholder={placeholder}
          className="w-full px-4 py-2.5 pr-11 rounded-xl border border-gray-200 text-sm text-gray-700 focus:outline-none focus:border-red-400 focus:ring-2 focus:ring-red-100 transition-all"
        />
        <button
          type="button"
          onClick={() => toggleShow(field)}
          className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
        >
          {showPasswords[field] ? (
            <EyeOff style={{ width: 16, height: 16 }} />
          ) : (
            <Eye style={{ width: 16, height: 16 }} />
          )}
        </button>
      </div>
    </div>
  );

  return (
    <>
      <Toast
        message={toast.message}
        type={toast.type}
        show={toast.show}
        onClose={() => setToast((t) => ({ ...t, show: false }))}
      />

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, ease: "easeOut" }}
        className="space-y-6 max-w-2xl"
      >
        {/* Tiêu đề */}
        <div>
          <h1 className="text-2xl font-extrabold text-gray-800 mb-1">
            ⚙️ Cài đặt tài khoản
          </h1>
          <p className="text-gray-400 text-sm">
            Quản lý bảo mật và tùy chọn cá nhân
          </p>
        </div>

        {/* ---- SECTION 1: Đổi mật khẩu ---- */}
        <Section icon={Lock} title="Đổi mật khẩu">
          <form onSubmit={handleSavePassword} className="space-y-4">
            <PasswordInput
              label="Mật khẩu hiện tại"
              field="current"
              placeholder="••••••••"
            />
            <PasswordInput
              label="Mật khẩu mới"
              field="newPass"
              placeholder="Tối thiểu 6 ký tự"
            />
            <PasswordInput
              label="Xác nhận mật khẩu mới"
              field="confirm"
              placeholder="Nhập lại mật khẩu mới"
            />
            <div className="flex justify-end pt-1">
              <button
                type="submit"
                className="px-6 py-2.5 rounded-xl text-white font-bold text-sm shadow hover:opacity-90 active:scale-95 transition-all duration-200"
                style={{ background: "#DC2626" }}
              >
                Cập nhật mật khẩu
              </button>
            </div>
          </form>
        </Section>

        {/* ---- SECTION 2: Thông báo ---- */}
        <Section icon={Bell} title="Cài đặt thông báo">
          <div className="space-y-5">
            {/* Toggle 1 */}
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-semibold text-gray-700">
                  Email thông báo khóa học mới
                </p>
                <p className="text-xs text-gray-400 mt-0.5">
                  Nhận email khi có khóa học mới phù hợp với bạn
                </p>
              </div>
              <ToggleSwitch
                id="toggle-email-new-course"
                checked={notifications.emailNewCourse}
                onChange={(v) =>
                  setNotifications((p) => ({ ...p, emailNewCourse: v }))
                }
              />
            </div>

            <div className="border-t border-gray-50" />

            {/* Toggle 2 */}
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-semibold text-gray-700">
                  Nhắc nhở tiến độ học
                </p>
                <p className="text-xs text-gray-400 mt-0.5">
                  Nhận thông báo hàng tuần về tiến độ học tập của bạn
                </p>
              </div>
              <ToggleSwitch
                id="toggle-progress-reminder"
                checked={notifications.progressReminder}
                onChange={(v) =>
                  setNotifications((p) => ({ ...p, progressReminder: v }))
                }
              />
            </div>

            <div className="flex justify-end">
              <button
                onClick={handleSaveNotifications}
                className="px-6 py-2.5 rounded-xl text-white font-bold text-sm shadow hover:opacity-90 active:scale-95 transition-all duration-200"
                style={{ background: "#DC2626" }}
              >
                Lưu thông báo
              </button>
            </div>
          </div>
        </Section>

        {/* ---- SECTION 3: Ngôn ngữ ---- */}
        <Section icon={Globe} title="Ngôn ngữ">
          <div className="space-y-4">
            <div>
              <label className="block text-xs font-bold text-gray-600 mb-1.5 uppercase tracking-wide">
                Ngôn ngữ hiển thị
              </label>
              <div className="relative">
                <select
                  id="select-language"
                  value={language}
                  onChange={(e) => setLanguage(e.target.value)}
                  className="w-full px-4 py-2.5 pr-10 rounded-xl border border-gray-200 text-sm text-gray-700 focus:outline-none focus:border-red-400 focus:ring-2 focus:ring-red-100 appearance-none bg-white transition-all"
                >
                  <option value="vi">🇻🇳 Tiếng Việt</option>
                  <option value="en">🇬🇧 English</option>
                </select>
                <ChevronDown
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none"
                  style={{ width: 16, height: 16 }}
                />
              </div>
            </div>
            <div className="flex justify-end">
              <button
                onClick={handleSaveLanguage}
                className="px-6 py-2.5 rounded-xl text-white font-bold text-sm shadow hover:opacity-90 active:scale-95 transition-all duration-200"
                style={{ background: "#DC2626" }}
              >
                Lưu ngôn ngữ
              </button>
            </div>
          </div>
        </Section>
      </motion.div>
    </>
  );
}
