package org.example.roomiehub.exception;

public class OutOfPostQuotaException extends RuntimeException {
    public OutOfPostQuotaException(String message) {
        super(message);
    }
}

