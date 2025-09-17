package com.example.profilebackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    
    // Profile fields
    private String bio;
    private String avatarUrl;
    private String profileImageUrl;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Follow system
    private List<String> followers = new ArrayList<>();
    private List<String> following = new ArrayList<>();
    private int followersCount = 0;
    private int followingCount = 0;
    
    // Posts count (for future use)
    private int postsCount = 0;
} 