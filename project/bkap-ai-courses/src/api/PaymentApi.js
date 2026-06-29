import { api } from "./Api";

// Tạo đơn hàng thanh toán
export const createOrderApi = async (data) => {
  return api.post("/api/v1/payment/create-order", data);
};

// Kiểm tra mã voucher
export const checkVoucherApi = async (code) => {
  return api.get(`/api/v1/payment/voucher/${code}`);
};

// Kiểm tra trạng thái giao dịch
export const checkPaymentStatusApi = async (orderId) => {
  return api.get(`/api/v1/payment/status/${orderId}`);
};
