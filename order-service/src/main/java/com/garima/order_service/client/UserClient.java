package com.garima.order_service.client;

import com.garima.order_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}/users"
)
public interface UserClient {
    @GetMapping("/{id}")
    UserResponse getUser(@PathVariable Long id);
}