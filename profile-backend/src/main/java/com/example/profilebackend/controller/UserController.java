package com.example.profilebackend.controller;

import com.example.profilebackend.model.User;
import com.example.profilebackend.service.UserService;
import com.example.profilebackend.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private FileUploadService fileUploadService;

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

    // Profile endpoints
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getProfile(@PathVariable String userId) {
        logger.info("Received get profile request for user: {}", userId);
        try {
            User profile = userService.getProfile(userId);
            logger.info("Successfully retrieved profile for user: {}", userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error getting profile for user: {} - Error: {}", userId, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/profile/username/{username}")
    public ResponseEntity<User> getProfileByUsername(@PathVariable String username) {
        logger.info("Received get profile request for username: {}", username);
        try {
            User profile = userService.getProfileByUsername(username);
            logger.info("Successfully retrieved profile for username: {}", username);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error getting profile for username: {} - Error: {}", username, e.getMessage());
            throw e;
        }
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateProfile(@PathVariable String userId, @RequestBody User updatedUser) {
        logger.info("Received update profile request for user: {}", userId);
        try {
            User updatedProfile = userService.updateProfile(userId, updatedUser);
            logger.info("Successfully updated profile for user: {}", userId);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            logger.error("Error updating profile for user: {} - Error: {}", userId, e.getMessage());
            throw e;
        }
    }

    // Follow system endpoints
    @PostMapping("/follow/{followerId}/{followingId}")
    public ResponseEntity<Void> followUser(@PathVariable String followerId, @PathVariable String followingId) {
        logger.info("Received follow request: {} following {}", followerId, followingId);
        try {
            userService.followUser(followerId, followingId);
            logger.info("Successfully processed follow request: {} following {}", followerId, followingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error processing follow request: {} following {} - Error: {}", followerId, followingId, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/unfollow/{followerId}/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable String followerId, @PathVariable String followingId) {
        logger.info("Received unfollow request: {} unfollowing {}", followerId, followingId);
        try {
            userService.unfollowUser(followerId, followingId);
            logger.info("Successfully processed unfollow request: {} unfollowing {}", followerId, followingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error processing unfollow request: {} unfollowing {} - Error: {}", followerId, followingId, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<User>> getFollowers(@PathVariable String userId) {
        logger.info("Received get followers request for user: {}", userId);
        try {
            List<User> followers = userService.getFollowers(userId);
            logger.info("Successfully retrieved {} followers for user: {}", followers.size(), userId);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            logger.error("Error getting followers for user: {} - Error: {}", userId, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<User>> getFollowing(@PathVariable String userId) {
        logger.info("Received get following request for user: {}", userId);
        try {
            List<User> following = userService.getFollowing(userId);
            logger.info("Successfully retrieved {} following for user: {}", following.size(), userId);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            logger.error("Error getting following for user: {} - Error: {}", userId, e.getMessage());
            throw e;
        }
    }

    // Search endpoints
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        logger.info("Received search request with query: {}", query);
        try {
            List<User> searchResults = userService.searchUsers(query);
            logger.info("Successfully retrieved {} search results for query: {}", searchResults.size(), query);
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            logger.error("Error searching users with query: {} - Error: {}", query, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Received get all users request");
        try {
            List<User> allUsers = userService.getAllUsers();
            logger.info("Successfully retrieved {} users", allUsers.size());
            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            logger.error("Error getting all users - Error: {}", e.getMessage());
            throw e;
        }
    }

    // File upload endpoints
    @PostMapping("/upload-profile-image/{userId}")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        logger.info("Received profile image upload request for user: {}", userId);
        try {
            String imageUrl = fileUploadService.uploadProfileImage(file, userId);
            
            // Update user's profile image URL
            User user = userService.getProfile(userId);
            user.setProfileImageUrl(imageUrl);
            userService.updateProfile(userId, user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile image uploaded successfully");
            response.put("imageUrl", imageUrl);
            
            logger.info("Successfully uploaded profile image for user: {}", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error uploading profile image for user: {} - Error: {}", userId, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/upload-avatar/{userId}")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        logger.info("Received avatar upload request for user: {}", userId);
        try {
            String imageUrl = fileUploadService.uploadProfileImage(file, userId);
            
            // Update user's avatar URL
            User user = userService.getProfile(userId);
            user.setAvatarUrl(imageUrl);
            userService.updateProfile(userId, user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Avatar uploaded successfully");
            response.put("avatarUrl", imageUrl);
            
            logger.info("Successfully uploaded avatar for user: {}", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error uploading avatar for user: {} - Error: {}", userId, e.getMessage());
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