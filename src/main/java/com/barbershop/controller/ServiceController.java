package com.barbershop.controller;

import com.barbershop.model.dto.request.ServiceRequest;
import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.ServiceResponse;
import com.barbershop.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * Get all active services (Public - for customers)
     * GET /api/services
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllActiveServices() {
        List<ServiceResponse> services = serviceService.getAllActiveServices();
        return ResponseEntity.ok(ApiResponse.success(services));
    }

    /**
     * Get service by ID
     * GET /api/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(ApiResponse.success(service));
    }

    /**
     * Create new service (Admin only - will add security later)
     * POST /api/services
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceService.createService(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Service created successfully", service));
    }

    /**
     * Update service (Admin only)
     * PUT /api/services/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceService.updateService(id, request);
        return ResponseEntity.ok(ApiResponse.success("Service updated successfully", service));
    }

    /**
     * Delete service (Admin only - soft delete)
     * DELETE /api/services/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully", null));
    }
}
