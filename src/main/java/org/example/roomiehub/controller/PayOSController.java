package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.PaymentRequest;
import org.example.roomiehub.service.PayOSService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PayOSController {

    private final PayOSService payOSService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        try {
            CheckoutResponseData response = payOSService.createPayment(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi dữ liệu: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo thanh toán: " + e.getMessage());
        }
    }
}
