package com.barbershop.controller;

import com.barbershop.model.dto.request.LoginRequest;
import com.barbershop.model.dto.request.RegisterRequest;
import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.AuthResponse;
import com.barbershop.model.dto.response.UserResponse;
import com.barbershop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User authentication and registration endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register new customer",
            description = "Create a new customer account. Returns user details without password."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", user));
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user with email and password. Returns JWT token valid for 24 hours."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Operation(
            summary = "API health check",
            description = "Test endpoint to verify API is running"
    )
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.success("API is working!"));
    }
}