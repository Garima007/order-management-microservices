package com.garima.user_service.service;

import com.garima.user_service.dto.LoginRequest;
import com.garima.user_service.dto.LoginResponse;
import com.garima.user_service.entity.User;
import com.garima.user_service.exceptions.InvalidCredentialsException;
import com.garima.user_service.repository.UserRepository;
import com.garima.user_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return response;
    }
}
