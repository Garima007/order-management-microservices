package com.garima.order_service.exceptions;

public class UserServiceUnavailableException
        extends RuntimeException {

    public UserServiceUnavailableException(String message) {
        super(message);
    }
}