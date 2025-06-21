package org.example.roomiehub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.PaymentRequest;
import org.example.roomiehub.service.PayOSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PayOSController {

    private final PayOSService payOSService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        try {
            CheckoutResponseData response = payOSService.createPayment(request);
            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("error", 0);
            jsonResponse.put("message", "success");
            jsonResponse.set("data", objectMapper.valueToTree(response));
            return ResponseEntity.ok(jsonResponse);
        } catch (IllegalArgumentException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", -1);
            errorResponse.put("message", "Lỗi dữ liệu: " + e.getMessage());
            errorResponse.set("data", null);
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", -1);
            errorResponse.put("message", "Đã xảy ra lỗi khi tạo thanh toán: " + e.getMessage());
            errorResponse.set("data", null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<?> getPaymentLinkInformation(@PathVariable long orderCode) {
        ObjectNode response = payOSService.getPaymentLinkInformation(orderCode);
        return ResponseEntity.status(response.get("error").asInt() == 0 ? 200 : 500).body(response);
    }

    @PutMapping("/cancel/{orderCode}")
    public ResponseEntity<?> cancelPaymentLink(@PathVariable long orderCode,
                                               @RequestBody(required = false) String cancellationReason) {
        ObjectNode response = payOSService.cancelPaymentLink(orderCode, cancellationReason);
        return ResponseEntity.status(response.get("error").asInt() == 0 ? 200 : 500).body(response);
    }

    @PostMapping("/confirm-webhook")
    public ResponseEntity<?> confirmWebhook(@RequestBody String webhookUrl) {
        ObjectNode response = payOSService.confirmWebhook(webhookUrl);
        return ResponseEntity.status(response.get("error").asInt() == 0 ? 200 : 500).body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> verifyPaymentWebhookData(@RequestBody Webhook webhookBody) {
        ObjectNode response = payOSService.verifyPaymentWebhookData(webhookBody);
        return ResponseEntity.status(response.get("error").asInt() == 0 ? 200 : 500).body(response);
    }
}