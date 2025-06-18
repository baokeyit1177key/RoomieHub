package org.example.roomiehub.service;

import org.example.roomiehub.dto.request.PaymentRequest;
import vn.payos.type.CheckoutResponseData;

public interface PayOSService {
    CheckoutResponseData createPayment(PaymentRequest request);
}
