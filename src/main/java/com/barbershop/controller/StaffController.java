package com.barbershop.controller;

import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.StaffResponse;
import com.barbershop.model.entity.Staff;
import com.barbershop.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;

    /**
     * Get all active staff (Public)
     * GET /api/staff
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllActiveStaff() {
        List<StaffResponse> staff = staffService.getAllActiveStaff();
        return ResponseEntity.ok(ApiResponse.success(staff));
    }

    /**
     * Get staff by ID
     * GET /api/staff/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable Long id) {
        StaffResponse staff = staffService.getStaffById(id);
        return ResponseEntity.ok(ApiResponse.success(staff));
    }

    /**
     * Create new staff (Admin only)
     * POST /api/staff
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@Valid @RequestBody Staff staff) {
        StaffResponse created = staffService.createStaff(staff);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Staff created successfully", created));
    }

    /**
     * Update staff (Admin only)
     * PUT /api/staff/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody Staff staff) {
        StaffResponse updated = staffService.updateStaff(id, staff);
        return ResponseEntity.ok(ApiResponse.success("Staff updated successfully", updated));
    }

    /**
     * Delete staff (Admin only)
     * DELETE /api/staff/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable Long id) {
        staffService.deactivateStaff(id);
        return ResponseEntity.ok(ApiResponse.success("Staff deactivated successfully", null));
    }
}
