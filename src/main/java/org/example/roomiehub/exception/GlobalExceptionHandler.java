package org.example.roomiehub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 - Conflict
                .body(new ErrorResponse("AUTH_ERROR", ex.getMessage()));
    }

    // Có thể thêm các handler khác ở đây

    // Class để trả lỗi chuẩn
    record ErrorResponse(String code, String message) {}

    @ExceptionHandler(NoActivePackageException.class)
    public ResponseEntity<Object> handleNoActivePackageException(NoActivePackageException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // hoặc 403 tùy bạn
                .body(new ErrorResponse("NO_ACTIVE_PACKAGE", ex.getMessage()));
    }

    @ExceptionHandler(OutOfPostQuotaException.class)
    public ResponseEntity<?> handleOutOfPostQuotaException(OutOfPostQuotaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "OutOfPostQuota");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // 400
    }
}
