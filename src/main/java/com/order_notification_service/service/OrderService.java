package com.order_notification_service.service;

import com.order_notification_service.dto.OrderRequestDTO;
import com.order_notification_service.dto.OrderResponseDTO;
import com.order_notification_service.entity.Order;
import com.order_notification_service.entity.OrderItem;
import com.order_notification_service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;


    public OrderService(OrderRepository orderRepository,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }


    public OrderResponseDTO createOrder(OrderRequestDTO request) {

        logger.info("Creating order for customer: {}", request.getCustomerName());

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setTotalAmount(request.getTotalAmount());

        List<OrderItem> items = request.getItems().stream().map(dto -> {
            OrderItem item = new OrderItem();
            item.setProductName(dto.getProductName());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved successfully with orderId={}", savedOrder.getId());

        String status;
        try {
            logger.info("Sending notification for orderId={}", savedOrder.getId());
            notificationService.sendNotification(savedOrder);
            status = "SENT";
            logger.info("Notification sent successfully for orderId={}", savedOrder.getId());
        } catch (Exception e) {
            status = "FAILED";
            logger.error("Notification failed for orderId={}", savedOrder.getId(), e);
        }

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(savedOrder.getId());
        response.setNotificationStatus(status);
        return response;
    }



    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();

    }
}