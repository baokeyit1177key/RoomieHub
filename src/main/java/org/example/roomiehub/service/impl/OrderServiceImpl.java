package org.example.roomiehub.service.impl;

import org.example.roomiehub.model.Order;
import org.example.roomiehub.repository.OrderRepository;
import org.example.roomiehub.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void updateOrCreateOrder(Long orderCode, Long amount, String status, String description) {
        Order order = orderRepository.findById(orderCode).orElse(null);

        if (order != null) {
            order.setStatus(status);
            order.setDescription(description);
            orderRepository.save(order);
        } else {
            Order newOrder = new Order();
            newOrder.setOrderCode(orderCode);
            newOrder.setAmount(amount);
            newOrder.setStatus(status);
            newOrder.setDescription(description);
            orderRepository.save(newOrder);
        }
    }
}
