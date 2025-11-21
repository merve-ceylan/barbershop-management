package com.barbershop.service;

import com.barbershop.exception.AppointmentConflictException;
import com.barbershop.exception.BadRequestException;
import com.barbershop.exception.ResourceNotFoundException;
import com.barbershop.model.dto.request.AppointmentCreateRequest;
import com.barbershop.model.dto.request.AppointmentItemRequest;
import com.barbershop.model.dto.response.AppointmentResponse;
import com.barbershop.model.entity.*;
import com.barbershop.repository.AppointmentItemRepository;
import com.barbershop.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentItemRepository appointmentItemRepository;
    private final UserService userService;
    private final ServiceService serviceService;
    private final StaffService staffService;

    /**
     * Create new appointment with multiple services
     */
    public AppointmentResponse createAppointment(Long customerId, AppointmentCreateRequest request) {
        // Validate customer
        User customer = userService.findById(customerId);

        // Validate appointment time
        if (request.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot book appointment in the past");
        }

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);
        appointment.setNotes(request.getNotes());

        // Process each service item
        for (AppointmentItemRequest itemRequest : request.getItems()) {
            // Validate service
            com.barbershop.model.entity.Service service = serviceService.findById(itemRequest.getServiceId());

            if (!service.getActive()) {
                throw new BadRequestException("Service is not active: " + service.getName());
            }

            // Validate and assign staff
            Staff staff = null;
            if (itemRequest.getStaffId() != null) {
                staff = staffService.findById(itemRequest.getStaffId());

                if (!staff.getActive()) {
                    throw new BadRequestException("Staff is not active");
                }

                // Check staff availability
                checkStaffAvailability(staff, itemRequest.getScheduledTime(), service.getDurationMinutes());

                // Check working hours
                validateWorkingHours(staff, itemRequest.getScheduledTime());
            }

            // Create appointment item
            AppointmentItem item = new AppointmentItem();
            item.setService(service);
            item.setStaff(staff);
            item.setPrice(service.getPrice());
            item.setDurationMinutes(service.getDurationMinutes());
            item.setScheduledTime(itemRequest.getScheduledTime());
            item.setStatus(AppointmentItem.ItemStatus.PENDING);

            // Add item to appointment
            appointment.addItem(item);
        }

        // Save appointment (items will be saved automatically due to CascadeType.ALL)
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return AppointmentResponse.fromEntity(savedAppointment);
    }

    /**
     * Check if staff is available at the given time
     */
    private void checkStaffAvailability(Staff staff, LocalDateTime startTime, Integer durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        // Get all appointments for this staff around this time
        LocalDateTime searchStart = startTime.minusMinutes(120); // 2 hours before
        LocalDateTime searchEnd = endTime.plusMinutes(120);       // 2 hours after

        List<AppointmentItem> potentialConflicts = appointmentItemRepository
                .findConflictingAppointments(staff, searchStart, searchEnd);

        // Check each appointment for actual time conflict
        for (AppointmentItem existing : potentialConflicts) {
            LocalDateTime existingStart = existing.getScheduledTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existing.getDurationMinutes());

            // Check if times overlap
            boolean hasConflict = (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart));

            if (hasConflict) {
                throw new AppointmentConflictException(
                        "Staff is not available at " + startTime + ". Please choose another time or staff member.");
            }
        }
    }

    /**
     * Validate appointment time is within staff working hours
     */
    private void validateWorkingHours(Staff staff, LocalDateTime appointmentTime) {
        if (appointmentTime.toLocalTime().isBefore(staff.getWorkStartTime())) {
            throw new BadRequestException(
                    "Appointment time is before staff working hours. Staff starts at " + staff.getWorkStartTime());
        }

        if (appointmentTime.toLocalTime().isAfter(staff.getWorkEndTime())) {
            throw new BadRequestException(
                    "Appointment time is after staff working hours. Staff ends at " + staff.getWorkEndTime());
        }
    }

    /**
     * Get appointment by ID
     */
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findByIdWithItems(id);
        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment", "id", id);
        }
        return AppointmentResponse.fromEntity(appointment);
    }

    /**
     * Get customer's appointments
     */
    public Page<AppointmentResponse> getCustomerAppointments(Long customerId, Pageable pageable) {
        User customer = userService.findById(customerId);
        Page<Appointment> appointments = appointmentRepository.findByCustomer(customer, pageable);
        return appointments.map(AppointmentResponse::fromEntity);
    }

    /**
     * Get all appointments (Admin)
     */
    public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        return appointments.map(AppointmentResponse::fromEntity);
    }

    /**
     * Get appointments by date
     */
    public List<AppointmentResponse> getAppointmentsByDate(LocalDateTime date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        return appointments.stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get appointments between dates
     */
    public List<AppointmentResponse> getAppointmentsBetween(LocalDateTime start, LocalDateTime end) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsBetween(start, end);
        return appointments.stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update appointment status
     */
    public AppointmentResponse updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);

        return AppointmentResponse.fromEntity(updated);
    }

    /**
     * Cancel appointment
     */
    public AppointmentResponse cancelAppointment(Long id, Long customerId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        // Verify ownership
        if (!appointment.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You can only cancel your own appointments");
        }

        // Check if already completed
        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        // Cancel appointment and all items
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.getItems().forEach(item -> item.setStatus(AppointmentItem.ItemStatus.CANCELLED));

        Appointment updated = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(updated);
    }

    /**
     * Confirm appointment (Admin)
     */
    public AppointmentResponse confirmAppointment(Long id) {
        return updateAppointmentStatus(id, Appointment.AppointmentStatus.CONFIRMED);
    }

    /**
     * Complete appointment (Admin/Staff)
     */
    public AppointmentResponse completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        // Mark appointment as completed
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);

        // Mark all items as completed
        appointment.getItems().forEach(item -> {
            if (item.getStatus() != AppointmentItem.ItemStatus.CANCELLED) {
                item.setStatus(AppointmentItem.ItemStatus.COMPLETED);
            }
        });

        Appointment updated = appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(updated);
    }

    /**
     * Get staff schedule for a day
     */
    public List<AppointmentItem> getStaffSchedule(Long staffId, LocalDateTime date) {
        Staff staff = staffService.findById(staffId);
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);

        return appointmentItemRepository.findStaffSchedule(staff, startOfDay, endOfDay);
    }
}