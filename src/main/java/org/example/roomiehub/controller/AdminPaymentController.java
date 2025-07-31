package org.example.roomiehub.controller;
import lombok.RequiredArgsConstructor;

import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.service.AdminPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService  adminPaymentService;

    @GetMapping("/statistics")
    public Map<String, Object> getPaymentStatistics() {
        return Map.of(
            "totalPaidAmount", adminPaymentService.getTotalPaidAmount(),
            "totalPaidOrders", adminPaymentService.getTotalPaidOrders()
        );
    }

    @GetMapping("/statistics/period")
@PreAuthorize("hasRole('ADMIN')")
public Map<String, Object> getPaidStatisticsByMonthYear(
        @RequestParam int month,
        @RequestParam int year
) {
    return adminPaymentService.getPaidStatisticsByMonthYear(month, year);
}
     @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        Long totalUsers = adminPaymentService.countTotalUsers();
        Long paidUsers = adminPaymentService.countDistinctPaidEmails();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("paidUsers", paidUsers);

        return ResponseEntity.ok(stats);
    }

      // ✅ Cập nhật role người dùng
    @PutMapping("/user/update-role")
    public ResponseEntity<String> updateUserRole(
            @RequestParam String email,
            @RequestParam Enums.Role role
    ) {
        boolean updated = adminPaymentService.updateUserRole(email, role);
        return updated ? ResponseEntity.ok("User role updated.")
                       : ResponseEntity.status(404).body("User not found.");
    }

    // ✅ Xóa người dùng theo email
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUserByEmail(@RequestParam String email) {
        boolean deleted = adminPaymentService.deleteUserByEmail(email);
        return deleted ? ResponseEntity.ok("User deleted.")
                       : ResponseEntity.status(404).body("User not found.");
    }

    // ✅ Xóa căn hộ theo id
    @DeleteMapping("/apartment/{id}")
    public ResponseEntity<String> deleteApartmentById(@PathVariable Long id) {
        boolean deleted = adminPaymentService.deleteApartmentById(id);
        return deleted ? ResponseEntity.ok("Apartment deleted.")
                       : ResponseEntity.status(404).body("Apartment not found.");
    }
}
