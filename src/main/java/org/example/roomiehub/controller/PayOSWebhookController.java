package org.example.roomiehub.controller;

import org.example.roomiehub.service.OrderService;
import org.example.roomiehub.util.HMACUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class PayOSWebhookController {
    @Value("${payos.checksum-key}")
    private String secretKey;
    ; //Thay bằng secret thật từ PayOS dashboard

    @Autowired
    private OrderService orderService;

    @PostMapping("/payos")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            //Lấy dữ liệu từ webhook
            String orderCodeStr = String.valueOf(payload.get("orderCode"));
            String amountStr = String.valueOf(payload.get("amount"));
            String status = String.valueOf(payload.get("status"));
            String description = String.valueOf(payload.get("description"));
            String receivedSignature = String.valueOf(payload.get("signature"));

            // Tạo chuỗi rawData để kiểm tra signature
            String rawData = orderCodeStr + amountStr + status;
            String calculatedSignature = HMACUtil.calculateHMAC(rawData, secretKey);

            // So sánh chữ ký bảo mật
            if (!receivedSignature.equals(calculatedSignature)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            // Parse dữ liệu
            Long orderCode = Long.parseLong(orderCodeStr);
            Long amount = Long.parseLong(amountStr);

            // Gọi service xử lý lưu hoặc cập nhật đơn hàng
            orderService.updateOrCreateOrder(orderCode, amount, status, description);

            return ResponseEntity.ok("✅ Webhook received and order processed");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Webhook error: " + e.getMessage());
        }
    }
}
