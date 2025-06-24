package org.example.roomiehub.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.PaymentRequest;
import org.example.roomiehub.service.PayOSService;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    private final PayOS payOS;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CheckoutResponseData createPayment(PaymentRequest request) {
        try {
            ItemData item = ItemData.builder()
                    .name("Gói " + request.getPackageType().name())
                    .price(request.getPackageType().getPrice())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(request.getOrderCode())
                    .amount(request.getPackageType().getPrice())
                    .description(request.getDescription())
                    .cancelUrl("https://roomiehub.vn/payment/cancel")
                    .returnUrl("https://roomiehub.vn/payment/success")
                    .item(item)
                    .build();

            return payOS.createPaymentLink(paymentData);
        } catch (Exception e) {
            throw new RuntimeException("Tạo thanh toán thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public ObjectNode getPaymentLinkInformation(long orderCode) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            System.out.println("Fetching payment link info for orderCode: " + orderCode);
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderCode);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "Lấy thông tin thanh toán thất bại: " + e.getMessage());
            response.set("data", null);
        }
        return response;
    }

    @Override
    public ObjectNode cancelPaymentLink(long orderCode, String cancellationReason) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            System.out.println("Cancelling payment link for orderCode: " + orderCode + ", reason: " + cancellationReason);
            PaymentLinkData order = payOS.cancelPaymentLink(orderCode, cancellationReason);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "Hủy thanh toán thất bại: " + e.getMessage());
            response.set("data", null);
        }
        return response;
    }

    @Override
    public ObjectNode confirmWebhook(String webhookUrl) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            System.out.println("Confirming webhook URL: " + webhookUrl);
            String result = payOS.confirmWebhook(webhookUrl);
            System.out.println("Webhook confirmation result: " + result);
            response.set("data", objectMapper.valueToTree(result));
            response.put("error", 0);
            response.put("message", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "Xác thực webhook thất bại: " + e.getMessage());
            response.set("data", null);
        }
        return response;
    }

    @Override
    public ObjectNode verifyPaymentWebhookData(Webhook webhookBody) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            System.out.println("Webhook received: " + objectMapper.writeValueAsString(webhookBody));
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            System.out.println("Webhook verified: " + objectMapper.writeValueAsString(data));
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "Xác minh webhook thất bại: " + e.getMessage());
            response.set("data", null);
        }
        return response;
    }
}