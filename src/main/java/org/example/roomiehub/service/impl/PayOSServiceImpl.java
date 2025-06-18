package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.PaymentRequest;
import org.example.roomiehub.service.PayOSService;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    private final PayOS payOS;

    @Override
    public CheckoutResponseData createPayment(PaymentRequest request) {
        try {
            int amount = request.getPackageType().getPrice();

            ItemData item = ItemData.builder()
                    .name("Gói " + request.getPackageType().name())
                    .price(amount)
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(request.getOrderCode())
                    .amount(amount)
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


}
