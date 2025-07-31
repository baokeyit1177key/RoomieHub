package org.example.roomiehub.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.model.Payment;
import org.example.roomiehub.repository.ApartmentRentalRepository;
import org.example.roomiehub.repository.PaymentRepository;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.AdminPaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ApartmentRentalRepository apartmentRentalRepository;

    @Override
    public Long getTotalPaidAmount() {
        return paymentRepository
                .findAll()
                .stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .mapToLong(p -> p.getAmount() != null ? p.getAmount() : 0)
                .sum();
    }

    @Override
    public Long getTotalPaidOrders() {
        return paymentRepository
                .findAll()
                .stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .count();
    }

    @Override
public Map<String, Object> getPaidStatisticsByMonthYear(int month, int year) {
    List<Payment> paidPayments = paymentRepository.findByStatus("PAID");

    // 1. Tổng doanh thu toàn bộ
    int totalAllTime = paidPayments.stream()
            .mapToInt(Payment::getAmount)
            .sum();

    // 2. Tính khoảng thời gian của tháng được truyền vào
    LocalDate startOfMonth = LocalDate.of(year, month, 1);
    LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

    // Lọc các payment trong tháng
    List<Payment> monthlyPayments = paidPayments.stream()
            .filter(p -> {
                LocalDate date = p.getCreatedAt().toLocalDate();
                return !date.isBefore(startOfMonth) && !date.isAfter(endOfMonth);
            })
            .toList();

    // 3. Tổng doanh thu trong tháng
    int totalMonth = monthlyPayments.stream()
            .mapToInt(Payment::getAmount)
            .sum();

    // 4. Doanh thu theo từng tuần trong tháng
    Map<Integer, Integer> weeklyRevenue = new HashMap<>();
    for (Payment payment : monthlyPayments) {
        int week = payment.getCreatedAt().get(ChronoField.ALIGNED_WEEK_OF_MONTH); // tuần trong tháng
        weeklyRevenue.put(week,
                weeklyRevenue.getOrDefault(week, 0) + payment.getAmount());
    }

    return Map.of(
        "totalAllTime", totalAllTime,
        "totalMonth", totalMonth,
        "weeklyRevenue", weeklyRevenue
    );
}
  @Override
    public Long countDistinctPaidEmails() {
        return paymentRepository.countDistinctEmailByStatus("PAID");
    }

    @Override
    public Long countTotalUsers() {
        return userRepository.countTotalUsers();
    }

    @Override
    public boolean updateUserRole(String email, Enums.Role newRole) {
        return userRepository.findByEmail(email).map(user -> {
            user.setRole(newRole);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            userRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteApartmentById(Long id) {
        if (apartmentRentalRepository.existsById(id)) {
            apartmentRentalRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
