package org.example.roomiehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String email;
    private String fullName;
    private LocalDate createdDate;
}
