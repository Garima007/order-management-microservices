package com.garima.user_service.service;

import com.garima.user_service.entity.User;
import com.garima.user_service.exceptions.EmailAlreadyExistsException;
import com.garima.user_service.exceptions.UserNotFoundException;
import com.garima.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void deleteUserById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User updateUserById(Long id, User newUser) {
        User user = getUserById(id);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        return userRepository.save(user);
    }
}