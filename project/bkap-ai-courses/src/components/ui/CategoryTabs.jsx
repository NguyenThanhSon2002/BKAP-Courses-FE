import React, { useState, useEffect } from "react";
import { getAllCategoriesApi } from "../../api/CourseApi";
import { RefreshCw } from "lucide-react";

export default function CategoryTabs({ activeCategory, onSelectCategory }) {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const fetchCategories = () => {
    setLoading(true);
    setError(false);
    getAllCategoriesApi()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        setCategories(data);
      })
      .catch((err) => {
        console.error("Failed to load categories:", err);
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  if (loading) {
    return (
      <div className="w-full flex items-center gap-3 overflow-x-auto pb-4 hide-scrollbar">
        {/* Skeleton for "Tất cả" */}
        <div className="h-10 w-24 bg-gray-200 rounded-full animate-pulse flex-shrink-0" />
        {/* Skeletons for other categories */}
        {[...Array(5)].map((_, i) => (
          <div
            key={i}
            className="h-10 w-32 bg-gray-200 rounded-full animate-pulse flex-shrink-0"
            style={{ width: `${Math.random() * 40 + 100}px` }} // Random width for varied look
          />
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="w-full flex flex-col items-center justify-center py-6 gap-3">
        <p className="text-gray-500 font-medium text-sm">
          Không thể tải danh mục. Vui lòng thử lại sau.
        </p>
        <button
          onClick={fetchCategories}
          className="flex items-center gap-2 px-5 py-2 rounded-full border border-gray-200 text-gray-700 bg-white hover:bg-gray-50 transition-colors text-sm font-semibold"
        >
          <RefreshCw className="w-4 h-4" />
          Thử lại
        </button>
      </div>
    );
  }

  return (
    <div className="w-full relative">
      <div className="flex items-center gap-2.5 overflow-x-auto pb-4 hide-scrollbar scroll-smooth">
        {/* Tab "Tất cả" */}
        <button
          onClick={() => onSelectCategory("all")}
          className={`flex-shrink-0 px-6 py-2 rounded-full text-sm font-bold transition-all duration-300 ${
            activeCategory === "all"
              ? "bg-[#C40D2E] text-white shadow-md transform scale-105"
              : "border border-gray-200 text-gray-700 bg-white hover:bg-gray-50 hover:border-gray-300"
          }`}
        >
          Tất cả
        </button>

        {/* Các danh mục từ API */}
        {categories.map((cat) => {
          const isActive = activeCategory === cat.slug;
          return (
            <button
              key={cat.id || cat.slug}
              onClick={() => onSelectCategory(cat.slug)}
              className={`flex-shrink-0 px-6 py-2 rounded-full text-sm font-bold transition-all duration-300 ${
                isActive
                  ? "bg-[#C40D2E] text-white shadow-md transform scale-105"
                  : "border border-gray-200 text-gray-700 bg-white hover:bg-gray-50 hover:border-gray-300"
              }`}
            >
              {cat.name}
            </button>
          );
        })}
      </div>

      {/* Global Style for hiding scrollbar */}
      <style dangerouslySetInnerHTML={{__html: `
        .hide-scrollbar::-webkit-scrollbar {
          display: none;
        }
        .hide-scrollbar {
          -ms-overflow-style: none;
          scrollbar-width: none;
        }
      `}} />
    </div>
  );
}
