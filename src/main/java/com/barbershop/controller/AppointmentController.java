package com.barbershop.controller;

import com.barbershop.model.dto.request.AppointmentCreateRequest;
import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.AppointmentResponse;
import com.barbershop.security.UserPrincipal;
import com.barbershop.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(
        name = "Appointments",
        description = "Appointment booking and management. Supports multi-service appointments with different staff members for each service. Includes conflict detection and working hours validation."
)
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Create new appointment",
            description = """
            Book a new appointment with one or multiple services. Each service can have:
            - Different staff member
            - Different scheduled time
            - Automatic conflict detection
            - Working hours validation
            
            The system will:
            - Check staff availability
            - Validate appointment is in the future
            - Calculate total price and duration
            - Prevent double-booking
            
            Requires customer authentication.
            """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Appointment created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation errors or appointment in the past",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Conflict - staff not available at requested time",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Appointment details with multiple services",
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """
                        {
                          "appointmentDateTime": "2025-12-30T14:00:00",
                          "items": [
                            {
                              "serviceId": 1,
                              "staffId": 1,
                              "scheduledTime": "2025-12-30T14:00:00"
                            },
                            {
                              "serviceId": 2,
                              "staffId": 1,
                              "scheduledTime": "2025-12-30T14:30:00"
                            }
                          ],
                          "notes": "Please use scissors only, short on sides"
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody AppointmentCreateRequest request) {

        Long customerId = currentUser.getId();

        AppointmentResponse appointment = appointmentService.createAppointment(customerId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment created successfully", appointment));
    }

    @Operation(
            summary = "Get appointment by ID",
            description = "Retrieve detailed information about a specific appointment including all services, staff assignments, prices, and status."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Appointment found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(
            @Parameter(description = "Appointment ID", example = "1")
            @PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success(appointment));
    }

    @Operation(
            summary = "Get my appointments",
            description = """
            Retrieve all appointments for the currently authenticated customer.
            Supports pagination for large result sets.
            
            Returns appointments with:
            - All service details
            - Staff assignments
            - Total price and duration
            - Current status (PENDING, CONFIRMED, COMPLETED, CANCELLED)
            
            Requires customer authentication.
            """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved appointments",
                    content = @Content(schema = @Schema(implementation = Page.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            )
    })
    @GetMapping("/my-appointments")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getMyAppointments(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Long customerId = currentUser.getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentResponse> appointments = appointmentService.getCustomerAppointments(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @Operation(
            summary = "Get all appointments (Admin)",
            description = """
            Retrieve all appointments in the system with pagination.
            Admin only endpoint for managing all customer appointments.
            
            Useful for:
            - Viewing daily schedule
            - Managing bookings
            - Generating reports
            
            Requires ADMIN role.
            """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all appointments"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN role required"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getAllAppointments(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentResponse> appointments = appointmentService.getAllAppointments(pageable);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @Operation(
            summary = "Get appointments by date",
            description = """
            Retrieve all appointments for a specific date.
            Returns appointments sorted by time.
            
            Date format: ISO 8601 (e.g., 2025-12-27T00:00:00)
            
            Useful for:
            - Daily schedule view
            - Calendar integration
            - Staff workload planning
            """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved appointments for the date"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid date format"
            )
    })
    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDate(
            @Parameter(
                    description = "Date in ISO format (YYYY-MM-DDTHH:MM:SS)",
                    example = "2025-12-30T00:00:00"
            )
            @RequestParam String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDate(dateTime);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @Operation(
            summary = "Cancel appointment",
            description = """
            Cancel an existing appointment. 
            
            Rules:
            - Customers can only cancel their own appointments
            - Cannot cancel completed appointments
            - Cancellation marks appointment and all items as CANCELLED
            
            Requires customer authentication.
            """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Appointment cancelled successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Cannot cancel - appointment already completed or not owned by customer"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            )
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "Appointment ID to cancel", example = "1")
            @PathVariable Long id) {

        Long customerId = currentUser.getId();

        AppointmentResponse appointment = appointmentService.cancelAppointment(id, customerId);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", appointment));
    }

    @Operation(
            summary = "Confirm appointment (Admin)",
            description = """
            Change appointment status from PENDING to CONFIRMED.
            Admin only operation.
            
            Used when:
            - Admin reviews and approves booking
            - Customer payment is verified
            - Appointment is ready to be serviced
            
            Requires ADMIN role.
            """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Appointment confirmed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN role required"
            )
    })
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(
            @Parameter(description = "Appointment ID to confirm", example = "1")
            @PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment confirmed successfully", appointment));
    }

    @Operation(
            summary = "Complete appointment (Admin/Staff)",
            description = """
            Mark appointment as completed after services are rendered.
            Changes appointment status to COMPLETED and all items to COMPLETED (except cancelled ones).
            
            This action:
            - Marks the appointment as done
            - Enables review submission for customer
            - Updates staff performance metrics
            
            Requires ADMIN or STAFF role.
            """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Appointment completed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN or STAFF role required"
            )
    })
    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(
            @Parameter(description = "Appointment ID to complete", example = "1")
            @PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment completed successfully", appointment));
    }
}