package com.garima.order_service.service;

import com.garima.order_service.dto.UserResponse;
import com.garima.order_service.entity.Order;
import com.garima.order_service.exceptions.OrderNotFoundException;
import com.garima.order_service.exceptions.UserNotFoundException;
import com.garima.order_service.exceptions.UserServiceUnavailableException;
import com.garima.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String USER_SERVICE_URL = "http://localhost:8080/users/";
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public Order createOrder(Order order) {
        try {

            restTemplate.getForObject(
                    USER_SERVICE_URL + order.getUserId(),
                    UserResponse.class);

        } catch (HttpClientErrorException.NotFound e) {

            throw new UserNotFoundException(
                    "User not found");

        } catch (ResourceAccessException e) {

            throw new UserServiceUnavailableException(
                    "User Service is unavailable");
        }
        return orderRepository.save(order);
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
