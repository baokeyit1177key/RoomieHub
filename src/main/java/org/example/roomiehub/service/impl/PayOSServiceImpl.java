package org.example.roomiehub.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.Enum.PackageType;
import org.example.roomiehub.dto.request.PaymentRequest;
import org.example.roomiehub.model.Payment;
import org.example.roomiehub.model.UserPackage;
import org.example.roomiehub.repository.PaymentRepository;
import org.example.roomiehub.repository.UserPackageRepository;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.PayOSService;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    private final PayOS payOS;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UserPackageRepository userPackageRepository;

    // Hàm tạo orderCode tự động (timestamp + số random)
    private long generateOrderCode() {
        return System.currentTimeMillis();
    }

    @Override
    public CheckoutResponseData createPayment(PaymentRequest request, String email) {
        try {
            long orderCode = generateOrderCode();

            Payment payment = Payment.builder()
                    .orderCode(orderCode)
                    .amount(request.getPackageType().getPrice())
                    .email(email)
                    .status("PENDING")
                    .build();
            paymentRepository.save(payment);

            ItemData item = ItemData.builder()
                    .name("Gói " + request.getPackageType().name())
                    .price(request.getPackageType().getPrice())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
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
    public void cancelPayment(Long orderCode) {
        paymentRepository.findByOrderCode(orderCode).ifPresent(payment -> {
            payment.setStatus("CANCELLED");
            paymentRepository.save(payment);
            System.out.println("Thanh toán đã bị hủy: " + orderCode);
        });
    }

    public String getEmailByOrderCode(Long orderCode) {
        return paymentRepository.findByOrderCode(orderCode)
                .map(Payment::getEmail)
                .orElse(null);
    }

    @Override
    public void confirmWebhook(Webhook webhook) {
        WebhookData data = webhook.getData();
        Long orderCode = data.getOrderCode();

        String desc = webhook.getDesc();
        String code = webhook.getCode();
        Boolean success = webhook.getSuccess();

        if (desc != null && desc.toLowerCase().contains("hết hạn")) {
            paymentRepository.findByOrderCode(orderCode).ifPresent(payment -> {
                payment.setStatus("EXPIRED");
                paymentRepository.save(payment);
            });
            System.out.println("Webhook - Đơn hàng hết hạn: " + orderCode);
            return;
        }

        if (Boolean.TRUE.equals(success) && "00".equals(code)) {
            paymentRepository.findByOrderCode(orderCode).ifPresent(payment -> {
                payment.setStatus("PAID");
                paymentRepository.save(payment);

                String email = payment.getEmail();
                userRepository.findByEmail(email).ifPresent(user -> {
                    PackageType type = getPackageTypeFromAmount(payment.getAmount());

                    int posts = switch (type) {
                        case BASIC -> 10;
                        case PROFESSIONAL -> 30;
                        case VIP -> 100;
                        case VR -> 3;
                    };

                    boolean isVr = (type == PackageType.VR);

                    UserPackage userPackage = UserPackage.builder()
                            .user(user)
                            .packageType(type)
                            .totalPosts(posts)
                            .remainingPosts(posts)
                            .isVrSupported(isVr)
                            .active(true)
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now().plusMonths(1))
                            .build();

                    userPackageRepository.save(userPackage);
                });
            });
        } else {
            paymentRepository.findByOrderCode(orderCode).ifPresent(payment -> {
                payment.setStatus("FAILED");
                paymentRepository.save(payment);
            });
            System.out.println("Webhook - Thanh toán thất bại hoặc bị huỷ: " + orderCode + " - " + desc);
        }
    }

    private PackageType getPackageTypeFromAmount(int amount) {
        for (PackageType type : PackageType.values()) {
            if (type.getPrice() == amount) return type;
        }
        throw new IllegalArgumentException("Không tìm thấy gói với giá tiền: " + amount);
    }
}
