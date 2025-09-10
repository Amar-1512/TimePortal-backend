package com.example.timeentrysystem.controller;
 
import com.example.timeentrysystem.entity.User;
import com.example.timeentrysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@RestController
@RequestMapping("/api/users")
public class UserController {
 
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
 
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
 
    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        logger.info("Received request to add user: {}", user.getEmail());
        try {
            User createdUser = userService.createUser(user);
            logger.info("User created successfully: {}", createdUser.getEmail());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create user: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Internal server error while creating user", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Internal server error while fetching users", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody String status) {
        logger.info("Received request to update user status: userId={}, status={}", id, status);
        try {
            boolean success = userService.updateUserStatus(id, status);
            if (success) {
                logger.info("User status updated successfully: userId={}", id);
                return new ResponseEntity<>("User status updated successfully", HttpStatus.OK);
            } else {
                logger.warn("User not found: userId={}", id);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Internal server error while updating user status", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
 
