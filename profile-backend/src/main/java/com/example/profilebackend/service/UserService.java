package com.example.profilebackend.service;

import com.example.profilebackend.model.User;
import com.example.profilebackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        
        // Initialize profile fields
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsPrivate(false);
        
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

    // Profile management methods
    public User getProfile(String userId) {
        logger.debug("Getting profile for user: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for user: {}", userId);
                    return new RuntimeException("User not found");
                });
    }

    public User getProfileByUsername(String username) {
        logger.debug("Getting profile for username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for username: {}", username);
                    return new RuntimeException("User not found");
                });
    }

    public User updateProfile(String userId, User updatedUser) {
        logger.debug("Updating profile for user: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found for update: {}", userId);
                    return new RuntimeException("User not found");
                });

        // Update only profile fields, not authentication data
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getBio() != null) {
            existingUser.setBio(updatedUser.getBio());
        }
        if (updatedUser.getAvatarUrl() != null) {
            existingUser.setAvatarUrl(updatedUser.getAvatarUrl());
        }
        if (updatedUser.getProfileImageUrl() != null) {
            existingUser.setProfileImageUrl(updatedUser.getProfileImageUrl());
        }
        existingUser.setIsPrivate(updatedUser.isPrivate());
        existingUser.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(existingUser);
        logger.info("Successfully updated profile for user: {}", userId);
        return savedUser;
    }

    // Follow system methods
    public void followUser(String followerId, String followingId) {
        logger.debug("User {} attempting to follow user {}", followerId, followingId);
        
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        // Check if already following
        if (follower.getFollowing().contains(followingId)) {
            throw new RuntimeException("Already following this user");
        }

        // Add to following list
        follower.getFollowing().add(followingId);
        follower.setFollowingCount(follower.getFollowingCount() + 1);

        // Add to followers list
        following.getFollowers().add(followerId);
        following.setFollowersCount(following.getFollowersCount() + 1);

        userRepository.save(follower);
        userRepository.save(following);
        logger.info("User {} successfully followed user {}", followerId, followingId);
    }

    public void unfollowUser(String followerId, String followingId) {
        logger.debug("User {} attempting to unfollow user {}", followerId, followingId);
        
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

        // Check if currently following
        if (!follower.getFollowing().contains(followingId)) {
            throw new RuntimeException("Not following this user");
        }

        // Remove from following list
        follower.getFollowing().remove(followingId);
        follower.setFollowingCount(follower.getFollowingCount() - 1);

        // Remove from followers list
        following.getFollowers().remove(followerId);
        following.setFollowersCount(following.getFollowersCount() - 1);

        userRepository.save(follower);
        userRepository.save(following);
        logger.info("User {} successfully unfollowed user {}", followerId, followingId);
    }

    public List<User> getFollowers(String userId) {
        logger.debug("Getting followers for user: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getFollowers().stream()
                .map(followerId -> userRepository.findById(followerId).orElse(null))
                .filter(follower -> follower != null)
                .collect(Collectors.toList());
    }

    public List<User> getFollowing(String userId) {
        logger.debug("Getting following list for user: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getFollowing().stream()
                .map(followingId -> userRepository.findById(followingId).orElse(null))
                .filter(following -> following != null)
                .collect(Collectors.toList());
    }

    // Search functionality
    public List<User> searchUsers(String query) {
        logger.debug("Searching users with query: {}", query);
        List<User> allUsers = userRepository.findAll();
        
        return allUsers.stream()
                .filter(user -> 
                    user.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                    user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    (user.getBio() != null && user.getBio().toLowerCase().contains(query.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll();
    }
} 