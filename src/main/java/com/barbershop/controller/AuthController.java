package com.barbershop.controller;

import com.barbershop.model.dto.request.LoginRequest;
import com.barbershop.model.dto.request.RegisterRequest;
import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.AuthResponse;
import com.barbershop.model.dto.response.UserResponse;
import com.barbershop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // For frontend access - will be configured properly later
public class AuthController {

    private final AuthService authService;

    /**
     * Register new customer
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", user));
    }

    /**
     * Login user
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.loginWithToken(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Test endpoint to check if API is working
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.success("API is working!"));
    }
}