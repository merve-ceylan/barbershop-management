package com.barbershop.service;

import com.barbershop.exception.BadRequestException;
import com.barbershop.exception.ResourceNotFoundException;
import com.barbershop.model.dto.response.UserResponse;
import com.barbershop.model.entity.User;
import com.barbershop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Find user by ID
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Check if email exists
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Get user response by ID
     */
    public UserResponse getUserById(Long id) {
        User user = findById(id);
        return UserResponse.fromEntity(user);
    }

    /**
     * Search customers with pagination
     */
    public Page<UserResponse> searchCustomers(String search, Pageable pageable) {
        Page<User> users = userRepository.searchCustomers(search, pageable);
        return users.map(UserResponse::fromEntity);
    }

    /**
     * Get all customers with pagination
     */
    public Page<UserResponse> getAllCustomers(Pageable pageable) {
        Page<User> customers = userRepository.findByRole(User.UserRole.CUSTOMER, pageable);
        return customers.map(UserResponse::fromEntity);
    }

    /**
     * Create new user
     */
    public User createUser(User user) {
        // Check if email already exists
        if (existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(User.UserRole.CUSTOMER);
        }

        // Set active by default
        if (user.getActive() == null) {
            user.setActive(true);
        }

        return userRepository.save(user);
    }

    /**
     * Update user information
     */
    public UserResponse updateUser(Long id, User updateRequest) {
        User user = findById(id);

        // Update fields
        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getPhone() != null) {
            user.setPhone(updateRequest.getPhone());
        }

        // Email update requires validation
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (existsByEmail(updateRequest.getEmail())) {
                throw new BadRequestException("Email already in use");
            }
            user.setEmail(updateRequest.getEmail());
        }

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    /**
     * Update password
     */
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = findById(userId);

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Encode and save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Deactivate user (soft delete)
     */
    public void deactivateUser(Long id) {
        User user = findById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Activate user
     */
    public void activateUser(Long id) {
        User user = findById(id);
        user.setActive(true);
        userRepository.save(user);
    }
}
