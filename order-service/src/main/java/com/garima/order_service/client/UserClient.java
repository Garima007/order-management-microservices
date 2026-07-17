package com.garima.order_service.client;

import com.garima.order_service.config.FeignConfig;
import com.garima.order_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        configuration = FeignConfig.class
)
public interface UserClient {
    @GetMapping("/{id}")
    UserResponse getUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization,
                         @PathVariable("id") Long id);
}
