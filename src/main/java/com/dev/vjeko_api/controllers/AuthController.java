package com.dev.vjeko_api.controllers;

import com.dev.vjeko_api.custom.ApiResponse;
import com.dev.vjeko_api.custom.ChangePasswordRequest;
import com.dev.vjeko_api.custom.LoginResponse;
import com.dev.vjeko_api.entities.User;
import com.dev.vjeko_api.repositories.UserRepository;
import com.dev.vjeko_api.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public String addUser(@RequestBody User user) {
        return authService.saveUser(user);
    }

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> getToken(@RequestBody User loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                // Your token generation logic here
                Optional<User> userData = userRepository.findUserByUsername(loginRequest.getUsername());

                String generatedToken = authService.generateToken(loginRequest.getUsername());  // Replace with actual token generation
                User user = userData.get();

                LoginResponse response = new LoginResponse(
                        200,
                        true,
                        "Login successful",
                        generatedToken,
                        user // You can pass the token in the data field
                );

                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(401, false, "Authentication failed"));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(401, false, "Authentication failed: " + e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<LoginResponse> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);

        LoginResponse response = new LoginResponse(
                200,
                true,
                "Token is valid",
                null  // You can pass the token in the data field
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authService.logout(token);
        return new ApiResponse(200, true, "Logged out successfully.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

        String userId = changePasswordRequest.getUserId();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        logger.info("Received request to change password for userId: {}", userId);

        if (userId == null || oldPassword == null || newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, false, "Missing required parameters"));
        }

        boolean result = authService.changePassword(userId, oldPassword, newPassword);

        if (result) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(200, true, "Password changed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, false, "Password change failed"));
        }
    }

    @GetMapping("/profile/{userId}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ResponseEntity<ApiResponse> getUserProfile(@PathVariable String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse response = new ApiResponse(
                    401,
                    false,
                    "Unauthorized"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<User> user = userRepository.findById(UUID.fromString(userId));

        if (user.isPresent()) {
            User userProfile = user.get();
            ApiResponse response = new ApiResponse(
                    200,
                    true,
                    "Profile retrieved successfully",
                    userProfile
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(
                    404,
                    false,
                    "User not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}