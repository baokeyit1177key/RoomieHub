package org.example.roomiehub.service;

public interface OrderService {
    void updateOrCreateOrder(Long orderCode, Long amount, String status, String description);
}
