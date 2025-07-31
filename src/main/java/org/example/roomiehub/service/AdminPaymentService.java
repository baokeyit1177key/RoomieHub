package org.example.roomiehub.service;

import org.example.roomiehub.Enum.Enums;

import java.util.Map;

public interface AdminPaymentService {
    Long getTotalPaidAmount();
    Long getTotalPaidOrders();
    Map<String, Object> getPaidStatisticsByMonthYear(int month, int year);
     Long countDistinctPaidEmails();
    Long countTotalUsers();

      boolean updateUserRole(String email, Enums.Role newRole);
    boolean deleteUserByEmail(String email);
    boolean deleteApartmentById(Long id);

}
