package org.example.roomiehub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
