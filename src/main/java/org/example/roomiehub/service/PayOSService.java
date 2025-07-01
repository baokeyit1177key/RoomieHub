package org.example.roomiehub.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.roomiehub.dto.request.PaymentRequest;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;

public interface PayOSService {
    CheckoutResponseData createPayment(PaymentRequest request);
    ObjectNode getPaymentLinkInformation(long orderCode);

}