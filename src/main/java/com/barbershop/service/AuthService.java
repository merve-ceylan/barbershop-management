package com.barbershop.service;

import com.barbershop.exception.BadRequestException;
import com.barbershop.exception.UnauthorizedException;
import com.barbershop.model.dto.request.LoginRequest;
import com.barbershop.model.dto.request.RegisterRequest;
import com.barbershop.model.dto.response.AuthResponse;
import com.barbershop.model.dto.response.UserResponse;
import com.barbershop.model.entity.User;
import com.barbershop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // TODO: Add JwtTokenProvider when we implement JWT

    /**
     * Register a new customer
     */
    public UserResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(User.UserRole.CUSTOMER);  // Default role
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    /**
     * Login user (without JWT for now)
     */
    public UserResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // Check if user is active
        if (!user.getActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return UserResponse.fromEntity(user);
    }

    /**
     * Login with JWT (will be implemented later)
     */
    public AuthResponse loginWithToken(LoginRequest request) {
        // Verify credentials
        UserResponse user = login(request);

        // TODO: Generate JWT token
        String token = "temporary-token-" + user.getId();

        return new AuthResponse(token, user);
    }

    /**
     * Verify user credentials
     */
    public boolean verifyPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }
}
