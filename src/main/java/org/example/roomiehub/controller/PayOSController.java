package org.example.roomiehub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.PaymentRequest;
import org.example.roomiehub.service.PayOSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

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


    @PostMapping("/receive-hook")
    public ResponseEntity<Void> receiveHook(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Webhook body: " + body);
        return ResponseEntity.ok().build();
    }

}

