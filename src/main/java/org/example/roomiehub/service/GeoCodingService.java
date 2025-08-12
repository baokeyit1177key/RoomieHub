package org.example.roomiehub.service;

public interface GeoCodingService {
    /**
     * Trả về mảng [latitude, longitude] từ địa chỉ.
     * Nếu không tìm được, trả về [0.0, 0.0] hoặc ném exception tùy bạn xử lý.
     */
    double[] getLatLongFromAddress(String address);
}
