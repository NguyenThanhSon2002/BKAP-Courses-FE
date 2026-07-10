import React, { useState } from "react";
import { motion } from "framer-motion";
import { Search, Filter } from "lucide-react";

// ---- Mock data (giống DashboardHome) ----
const mockEnrolledCourses = [
  {
    id: 1,
    title: "ReactJS từ Zero đến Hero",
    instructor: "Nguyễn Đức Tài",
    thumbnail: "https://placehold.co/400x225/1e293b/white?text=ReactJS",
    progress: 75,
    totalLessons: 48,
    completedLessons: 36,
    lastAccessed: "Hôm nay",
    status: "Đang học",
    category: "Frontend",
  },
  {
    id: 2,
    title: "Spring Boot Backend từ A-Z",
    instructor: "Trần Minh Quân",
    thumbnail: "https://placehold.co/400x225/0f172a/white?text=Spring+Boot",
    progress: 100,
    totalLessons: 60,
    completedLessons: 60,
    lastAccessed: "3 ngày trước",
    status: "Hoàn thành",
    category: "Backend",
  },
  {
    id: 3,
    title: "Thiết kế UI/UX với Figma",
    instructor: "Lê Thị Hoa",
    thumbnail: "https://placehold.co/400x225/7c3aed/white?text=UI%2FUX+Figma",
    progress: 20,
    totalLessons: 35,
    completedLessons: 7,
    lastAccessed: "1 tuần trước",
    status: "Đang học",
    category: "Design",
  },
];

const TABS = ["Tất cả", "Đang học", "Hoàn thành"];

const CATEGORY_COLORS = {
  Frontend: { bg: "#EFF6FF", text: "#1D4ED8" },
  Backend: { bg: "#F0FDF4", text: "#166534" },
  Design: { bg: "#FAF5FF", text: "#7C3AED" },
};

/**
 * MyCoursesPage: Trang Khóa học của tôi /dashboard/my-courses
 * - Filter tabs, search, danh sách ngang với progress bar animation
 */
export default function MyCoursesPage() {
  const [activeTab, setActiveTab] = useState("Tất cả");
  const [searchQuery, setSearchQuery] = useState("");

  // Lọc theo tab và search
  const filteredCourses = mockEnrolledCourses.filter((course) => {
    const matchTab =
      activeTab === "Tất cả" || course.status === activeTab;
    const matchSearch = course.title
      .toLowerCase()
      .includes(searchQuery.toLowerCase());
    return matchTab && matchSearch;
  });

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4, ease: "easeOut" }}
      className="space-y-6"
    >
      {/* ---- Tiêu đề ---- */}
      <div>
        <h1 className="text-2xl font-extrabold text-gray-800 mb-1">
          📚 Khóa học của tôi
        </h1>
        <p className="text-gray-400 text-sm">
          Theo dõi tiến độ học tập của bạn
        </p>
      </div>

      {/* ---- Thanh lọc & Tìm kiếm ---- */}
      <div className="flex flex-col sm:flex-row gap-3">
        {/* Filter tabs */}
        <div className="flex bg-gray-100 p-1 rounded-xl gap-1">
          {TABS.map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className="px-4 py-1.5 rounded-lg text-sm font-semibold transition-all duration-200"
              style={
                activeTab === tab
                  ? { background: "#DC2626", color: "#fff" }
                  : { background: "transparent", color: "#64748B" }
              }
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Search box */}
        <div className="relative flex-1">
          <Search
            className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
            style={{ width: 16, height: 16 }}
          />
          <input
            type="text"
            placeholder="Tìm kiếm khóa học..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 rounded-xl border border-gray-200 text-sm text-gray-700 focus:outline-none focus:border-red-400 focus:ring-2 focus:ring-red-100 bg-white transition-all"
          />
        </div>
      </div>

      {/* ---- Danh sách khóa học ---- */}
      {filteredCourses.length === 0 ? (
        <div className="text-center py-16 text-gray-400">
          <Filter className="w-12 h-12 mx-auto mb-3 opacity-30" />
          <p className="font-semibold">Không tìm thấy khóa học nào</p>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredCourses.map((course, index) => {
            const catColor = CATEGORY_COLORS[course.category] || {
              bg: "#F3F4F6",
              text: "#374151",
            };
            const isCompleted = course.status === "Hoàn thành";

            return (
              <motion.div
                key={course.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ duration: 0.35, delay: index * 0.08, ease: "easeOut" }}
                className="bg-white rounded-2xl p-4 md:p-5 shadow-sm border border-gray-100 flex gap-4 hover:shadow-md transition-shadow duration-300"
              >
                {/* Thumbnail nhỏ */}
                <div className="flex-shrink-0">
                  <img
                    src={course.thumbnail}
                    alt={course.title}
                    className="w-28 h-20 md:w-36 md:h-24 object-cover rounded-xl"
                  />
                </div>

                {/* Nội dung chi tiết */}
                <div className="flex-1 min-w-0 flex flex-col gap-1.5">
                  {/* Row 1: Category + Status */}
                  <div className="flex items-center gap-2 flex-wrap">
                    <span
                      className="text-xs font-bold px-2 py-0.5 rounded-md"
                      style={{ background: catColor.bg, color: catColor.text }}
                    >
                      {course.category}
                    </span>
                    <span
                      className="text-xs font-bold px-2 py-0.5 rounded-full"
                      style={
                        isCompleted
                          ? { background: "#DCFCE7", color: "#166534" }
                          : { background: "#DBEAFE", color: "#1D4ED8" }
                      }
                    >
                      {course.status}
                    </span>
                  </div>

                  {/* Tên khóa học */}
                  <h3 className="font-bold text-gray-800 text-sm md:text-base leading-snug line-clamp-1">
                    {course.title}
                  </h3>

                  {/* Giảng viên */}
                  <p className="text-xs text-gray-400">
                    👨‍🏫 {course.instructor}
                  </p>

                  {/* Progress bar với animation */}
                  <div className="mt-1">
                    <div className="flex justify-between items-center mb-1">
                      <span className="text-xs text-gray-500">
                        {course.completedLessons}/{course.totalLessons} bài học
                      </span>
                      <span className="text-xs font-bold text-red-600">
                        {course.progress}%
                      </span>
                    </div>
                    <div className="w-full h-2 bg-gray-100 rounded-full overflow-hidden">
                      <motion.div
                        className="h-full rounded-full"
                        style={{ background: isCompleted ? "#22C55E" : "#DC2626" }}
                        initial={{ width: 0 }}
                        animate={{ width: `${course.progress}%` }}
                        transition={{
                          duration: 0.9,
                          ease: "easeOut",
                          delay: 0.2 + index * 0.1,
                        }}
                      />
                    </div>
                  </div>

                  {/* Footer: truy cập + nút */}
                  <div className="flex items-center justify-between mt-1">
                    <span className="text-xs text-gray-400">
                      🕐 Truy cập lần cuối: {course.lastAccessed}
                    </span>
                    <button
                      className="text-xs font-bold px-3 py-1.5 rounded-lg transition-all duration-200"
                      style={
                        isCompleted
                          ? {
                              background: "#F0FDF4",
                              color: "#166534",
                              border: "1px solid #BBF7D0",
                            }
                          : { background: "#DC2626", color: "#fff" }
                      }
                    >
                      {isCompleted ? "Xem lại" : "Tiếp tục học →"}
                    </button>
                  </div>
                </div>
              </motion.div>
            );
          })}
        </div>
      )}
    </motion.div>
  );
}
