package com.barbershop.model.dto.response;

import com.barbershop.model.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private UserResponse customer;
    private List<AppointmentItemResponse> items;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String notes;
    private BigDecimal totalPrice;
    private Integer totalDuration;
    private LocalDateTime createdAt;

    public static AppointmentResponse fromEntity(Appointment appointment) {
        List<AppointmentItemResponse> itemResponses = appointment.getItems()
                .stream()
                .map(AppointmentItemResponse::fromEntity)
                .collect(Collectors.toList());

        BigDecimal totalPrice = appointment.getItems()
                .stream()
                .map(item -> item.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalDuration = appointment.getItems()
                .stream()
                .map(item -> item.getDurationMinutes())
                .reduce(0, Integer::sum);

        return new AppointmentResponse(
                appointment.getId(),
                UserResponse.fromEntity(appointment.getCustomer()),
                itemResponses,
                appointment.getAppointmentDateTime(),
                appointment.getStatus().name(),
                appointment.getNotes(),
                totalPrice,
                totalDuration,
                appointment.getCreatedAt()
        );
    }
}
