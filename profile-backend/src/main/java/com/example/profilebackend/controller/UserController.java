package com.example.profilebackend.controller;

import com.example.profilebackend.model.User;
import com.example.profilebackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        logger.info("Received signup request for user: {}", user.getUsername());
        try {
            User createdUser = userService.signup(user);
            logger.info("Successfully created user: {}", user.getUsername());
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.error("Error during signup for user: {} - Error: {}", user.getUsername(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for user: {}", loginRequest.getUsername());
        try {
            User loggedInUser = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            logger.info("Successfully logged in user: {}", loginRequest.getUsername());
            return ResponseEntity.ok(loggedInUser);
        } catch (Exception e) {
            logger.error("Error during login for user: {} - Error: {}", loginRequest.getUsername(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logger.info("Received logout request");
        try {
            userService.logout();
            logger.info("Successfully logged out user");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error during logout - Error: {}", e.getMessage());
            throw e;
        }
    }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 