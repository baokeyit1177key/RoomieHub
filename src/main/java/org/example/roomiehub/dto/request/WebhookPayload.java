package org.example.roomiehub.dto.request;

import lombok.Data;
import vn.payos.type.WebhookData;

@Data
public class WebhookPayload {
    private int code;
    private String desc;
    private boolean success;
    private WebhookData data;
    private String signature;
}

