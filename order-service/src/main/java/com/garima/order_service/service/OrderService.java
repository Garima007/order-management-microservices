package com.garima.order_service.service;

import com.garima.order_service.client.UserClient;
import com.garima.order_service.dto.OrderRequest;
import com.garima.order_service.dto.OrderResponse;
import com.garima.order_service.dto.UserResponse;
import com.garima.order_service.entity.Order;
import com.garima.order_service.event.OrderCreatedEvent;
import com.garima.order_service.exceptions.OrderNotFoundException;
import com.garima.order_service.exceptions.UserNotFoundException;
import com.garima.order_service.exceptions.UserServiceUnavailableException;
import com.garima.order_service.kafka.OrderEventProducer;
import com.garima.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;
    private final UserClient userClient;

    public OrderResponse createOrder(OrderRequest request,String authorization) {
        try {
            log.info("Calling User Service to validate user with ID: {}", request.getUserId());
            log.info("Authorization = [{}]", authorization);
            UserResponse user = userClient.getUser(authorization, request.getUserId());

        } catch (HttpClientErrorException.NotFound e) {
            log.error("User with ID: {} not found  {}", request.getUserId(), e.getMessage());
            throw new UserNotFoundException(
                    "User not found");

        } catch (ResourceAccessException e) {
            log.error("Failed to access User Service  {}" , e.getMessage());
            throw new UserServiceUnavailableException(
                    "User Service is unavailable");
        }
        Order order = new Order();
        order.setAmount(request.getAmount());
        order.setProductName(request.getProductName());
        order.setUserId(request.getUserId());
        Order savedOrder = orderRepository.save(order);

        publishKafkaEvent(savedOrder);

        OrderResponse response = new OrderResponse();
        response.setAmount(savedOrder.getAmount());
        response.setProductName(savedOrder.getProductName());
        response.setUserId(savedOrder.getUserId());
        response.setId(savedOrder.getId());
        return response;
    }

    private void publishKafkaEvent(Order savedOrder) {
        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(savedOrder.getId());
        event.setUserId(savedOrder.getUserId());
        event.setAmount(savedOrder.getAmount());
        event.setTimestamp(LocalDateTime.now());
        orderEventProducer.publishOrderCreated(event);
        log.info("Published OrderCreatedEvent to Kafka for Order ID: {}", savedOrder.getId());
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public Order updateOrderById(Long orderId, Order newOrder) {
        Order order = getOrderById(orderId);
        order.setAmount(newOrder.getAmount());
        order.setProductName(newOrder.getProductName());
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long orderId) {
        Order order = getOrderById(orderId);
        orderRepository.delete(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
