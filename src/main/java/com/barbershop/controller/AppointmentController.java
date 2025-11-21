package com.barbershop.controller;

import com.barbershop.model.dto.request.AppointmentCreateRequest;
import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.AppointmentResponse;
import com.barbershop.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Create new appointment
     * POST /api/appointments
     *
     * For now, we'll use a hardcoded customerId (1)
     * Later, we'll get it from JWT token
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody AppointmentCreateRequest request) {
        // TODO: Get customerId from JWT token
        Long customerId = 1L;  // Hardcoded for now

        AppointmentResponse appointment = appointmentService.createAppointment(customerId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment created successfully", appointment));
    }

    /**
     * Get appointment by ID
     * GET /api/appointments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success(appointment));
    }

    /**
     * Get customer's appointments
     * GET /api/appointments/my-appointments?page=0&size=10
     */
    @GetMapping("/my-appointments")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getMyAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // TODO: Get customerId from JWT token
        Long customerId = 1L;  // Hardcoded for now

        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentResponse> appointments = appointmentService.getCustomerAppointments(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    /**
     * Get all appointments (Admin)
     * GET /api/appointments?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentResponse> appointments = appointmentService.getAllAppointments(pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    /**
     * Get appointments by date
     * GET /api/appointments/by-date?date=2025-11-20T00:00:00
     */
    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDate(
            @RequestParam String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDate(dateTime);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    /**
     * Cancel appointment
     * PUT /api/appointments/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(@PathVariable Long id) {
        // TODO: Get customerId from JWT token
        Long customerId = 1L;  // Hardcoded for now

        AppointmentResponse appointment = appointmentService.cancelAppointment(id, customerId);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", appointment));
    }

    /**
     * Confirm appointment (Admin)
     * PUT /api/appointments/{id}/confirm
     */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment confirmed successfully", appointment));
    }

    /**
     * Complete appointment (Admin/Staff)
     * PUT /api/appointments/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment completed successfully", appointment));
    }
}