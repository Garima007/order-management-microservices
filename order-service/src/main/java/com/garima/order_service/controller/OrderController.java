package com.garima.order_service.controller;

import com.garima.order_service.dto.OrderRequest;
import com.garima.order_service.dto.OrderResponse;
import com.garima.order_service.entity.Order;
import com.garima.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest order){
        return orderService.createOrder(order);
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