package com.example.profilebackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    
    private final Path uploadDir = Paths.get("uploads/profile-images");
    
    public FileUploadService() {
        try {
            Files.createDirectories(uploadDir);
            logger.info("Created upload directory: {}", uploadDir.toAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to create upload directory: {}", e.getMessage());
        }
    }
    
    public String uploadProfileImage(MultipartFile file, String userId) {
        logger.debug("Uploading profile image for user: {}", userId);
        
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("File must be an image");
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String filename = userId + "_" + UUID.randomUUID().toString() + extension;
        
        try {
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            String fileUrl = "/uploads/profile-images/" + filename;
            logger.info("Successfully uploaded profile image for user: {} to: {}", userId, fileUrl);
            return fileUrl;
            
        } catch (IOException e) {
            logger.error("Failed to upload profile image for user: {} - Error: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
    
    public void deleteProfileImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        
        try {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = uploadDir.resolve(filename);
            Files.deleteIfExists(filePath);
            logger.info("Deleted profile image: {}", filename);
        } catch (IOException e) {
            logger.error("Failed to delete profile image: {} - Error: {}", imageUrl, e.getMessage());
        }
    }
}
