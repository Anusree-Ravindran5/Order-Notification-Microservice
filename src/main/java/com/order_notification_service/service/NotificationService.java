package com.order_notification_service.service;

import com.order_notification_service.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Value("${notification.mock.url}")
    private String notificationMockUrl;


    private final RestTemplate restTemplate = new RestTemplate();


    public void sendNotification(Order order) {
        logger.info("Calling notification endpoint: {}", notificationMockUrl);
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", order.getId());
        payload.put("totalAmount", order.getTotalAmount());


        restTemplate.postForObject(
                notificationMockUrl,
                payload,
                String.class
        );
        logger.info("Notification API call completed for orderId={}", order.getId());
    }
}
