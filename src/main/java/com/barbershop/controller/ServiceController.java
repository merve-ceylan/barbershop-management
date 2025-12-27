package com.barbershop.controller;

import com.barbershop.model.dto.request.ServiceRequest;
import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.ServiceResponse;
import com.barbershop.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Services", description = "Barbershop service management endpoints")
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService serviceService;

    @Operation(summary = "Get all active services", description = "Retrieve list of all active barbershop services")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllActiveServices() {
        List<ServiceResponse> services = serviceService.getAllActiveServices();
        return ResponseEntity.ok(ApiResponse.success(services));
    }

    @Operation(summary = "Get service by ID", description = "Retrieve a specific service by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(ApiResponse.success(service));
    }

    @Operation(
            summary = "Create new service",
            description = "Create a new barbershop service. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceService.createService(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Service created successfully", service));
    }

    @Operation(
            summary = "Update service",
            description = "Update an existing service. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceService.updateService(id, request);
        return ResponseEntity.ok(ApiResponse.success("Service updated successfully", service));
    }

    @Operation(
            summary = "Delete service",
            description = "Soft delete a service (marks as inactive). Requires ADMIN role.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully", null));
    }
}