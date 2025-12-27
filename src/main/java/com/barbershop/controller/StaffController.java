package com.barbershop.controller;

import com.barbershop.model.dto.response.ApiResponse;
import com.barbershop.model.dto.response.StaffResponse;
import com.barbershop.model.entity.Staff;
import com.barbershop.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Staff Management",
        description = "Endpoints for managing barbershop staff members. Public endpoints for viewing staff, admin-only endpoints for CRUD operations."
)
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;

    @Operation(
            summary = "Get all active staff members",
            description = "Retrieve a list of all active (non-deleted) staff members. This is a public endpoint accessible without authentication. Returns staff name, specialties, working hours, and contact information."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved staff list",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllActiveStaff() {
        List<StaffResponse> staff = staffService.getAllActiveStaff();
        return ResponseEntity.ok(ApiResponse.success(staff));
    }

    @Operation(
            summary = "Get staff member by ID",
            description = "Retrieve detailed information about a specific staff member including their specialties, working hours, and booking availability. Public endpoint."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Staff member found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(
            @Parameter(description = "Staff member ID", example = "1")
            @PathVariable Long id) {
        StaffResponse staff = staffService.getStaffById(id);
        return ResponseEntity.ok(ApiResponse.success(staff));
    }

    @Operation(
            summary = "Create new staff member",
            description = "Add a new staff member to the system. Requires ADMIN role. You can specify working hours, specialties, and contact information. Staff member will be active by default.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Staff member created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN role required",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Staff member details",
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """
                        {
                          "name": "Ahmet Yılmaz",
                          "phone": "5551234567",
                          "email": "ahmet@barbershop.com",
                          "specialties": "Classic cuts, Fade, Beard styling",
                          "workStartTime": "09:00:00",
                          "workEndTime": "18:00:00",
                          "active": true
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody Staff staff) {
        StaffResponse created = staffService.createStaff(staff);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Staff created successfully", created));
    }

    @Operation(
            summary = "Update staff member",
            description = "Update information of an existing staff member. You can update name, contact info, specialties, and working hours. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Staff member updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @Parameter(description = "Staff member ID to update", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated staff details (partial update supported)",
                    content = @Content(
                            schema = @Schema(
                                    example = """
                        {
                          "name": "Ahmet Yılmaz",
                          "specialties": "Classic cuts, Fade, Beard styling, Hair coloring",
                          "workStartTime": "10:00:00",
                          "workEndTime": "19:00:00"
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody Staff staff) {
        StaffResponse updated = staffService.updateStaff(id, staff);
        return ResponseEntity.ok(ApiResponse.success("Staff updated successfully", updated));
    }

    @Operation(
            summary = "Deactivate staff member",
            description = "Soft delete a staff member by marking them as inactive. They will no longer appear in active staff listings or be available for new appointments. Existing appointments remain valid. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Staff member deactivated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(
            @Parameter(description = "Staff member ID to deactivate", example = "1")
            @PathVariable Long id) {
        staffService.deactivateStaff(id);
        return ResponseEntity.ok(ApiResponse.success("Staff deactivated successfully", null));
    }
}