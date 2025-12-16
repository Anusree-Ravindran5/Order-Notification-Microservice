package com.order_notification_service;


import com.order_notification_service.dto.OrderItemDTO;
import com.order_notification_service.dto.OrderRequestDTO;
import com.order_notification_service.dto.OrderResponseDTO;
import com.order_notification_service.entity.Order;
import com.order_notification_service.repository.OrderRepository;
import com.order_notification_service.service.NotificationService;
import com.order_notification_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_success() {

        Order savedOrder = new Order();
        savedOrder.setId(1L);

        when(orderRepository.save(any())).thenReturn(savedOrder);
        doNothing().when(notificationService).sendNotification(any());

        OrderResponseDTO response =
                orderService.createOrder(mockOrderRequest());

        assertEquals("SENT", response.getNotificationStatus());
        assertEquals(1L, response.getOrderId());
    }

    @Test
    void createOrder_notificationFailure() {

        Order savedOrder = new Order();
        savedOrder.setId(2L);

        when(orderRepository.save(any())).thenReturn(savedOrder);
        doThrow(new RuntimeException("Notification service down"))
                .when(notificationService)
                .sendNotification(any());

        OrderResponseDTO response =
                orderService.createOrder(mockOrderRequest());

        assertEquals("FAILED", response.getNotificationStatus());
        assertEquals(2L, response.getOrderId());
    }

    private OrderRequestDTO mockOrderRequest() {

        OrderItemDTO item = new OrderItemDTO();
        item.setProductName("Bag");
        item.setQuantity(1);
        item.setPrice(BigDecimal.valueOf(1500));

        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerName("Test User");
        request.setTotalAmount(BigDecimal.valueOf(1500));
        request.setItems(List.of(item));

        return request;
    }
}
