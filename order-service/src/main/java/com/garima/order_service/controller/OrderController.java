package com.garima.order_service.controller;

import com.garima.order_service.dto.OrderRequest;
import com.garima.order_service.dto.OrderResponse;
import com.garima.order_service.entity.Order;
import com.garima.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest order,
                                     @RequestHeader("Authorization") String authorization){
        log.info("Authorization Header: {}", authorization);
        return orderService.createOrder(order,authorization);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public  void deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
    }

}