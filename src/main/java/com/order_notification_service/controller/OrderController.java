package com.order_notification_service.controller;

import com.order_notification_service.dto.OrderRequestDTO;
import com.order_notification_service.dto.OrderResponseDTO;
import com.order_notification_service.entity.Order;
import com.order_notification_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }


    @GetMapping
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }
}

