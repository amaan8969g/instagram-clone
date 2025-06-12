package com.example.profilebackend.service;

import com.example.profilebackend.model.User;
import com.example.profilebackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    public User signup(User user) {
        logger.debug("Checking if username exists: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Signup failed - Username already exists: {}", user.getUsername());
            throw new RuntimeException("Username already exists");
        }
        
        logger.debug("Checking if email exists: {}", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Signup failed - Email already exists: {}", user.getEmail());
            throw new RuntimeException("Email already exists");
        }
        
        logger.debug("Saving new user: {}", user.getUsername());
        User savedUser = userRepository.save(user);
        logger.info("Successfully saved new user: {}", user.getUsername());
        return savedUser;
    }

    public User login(String username, String password) {
        logger.debug("Attempting to find user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed - User not found: {}", username);
                    return new RuntimeException("User not found");
                });
        
        logger.debug("Verifying password for user: {}", username);
        if (!user.getPassword().equals(password)) {
            logger.warn("Login failed - Invalid password for user: {}", username);
            throw new RuntimeException("Invalid password");
        }
        
        logger.info("Successfully authenticated user: {}", username);
        return user;
    }

    public void logout() {
        logger.info("Processing logout request");
        // Since we're not using any session management or tokens,
        // logout is just a placeholder method
    }
} 