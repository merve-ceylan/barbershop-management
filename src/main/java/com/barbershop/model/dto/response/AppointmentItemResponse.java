package com.barbershop.model.dto.response;

import com.barbershop.model.entity.AppointmentItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentItemResponse {
    private Long id;
    private ServiceResponse service;
    private StaffResponse staff;
    private BigDecimal price;
    private Integer durationMinutes;
    private LocalDateTime scheduledTime;
    private String status;

    public static AppointmentItemResponse fromEntity(AppointmentItem item) {
        return new AppointmentItemResponse(
                item.getId(),
                ServiceResponse.fromEntity(item.getService()),
                item.getStaff() != null ? StaffResponse.fromEntity(item.getStaff()) : null,
                item.getPrice(),
                item.getDurationMinutes(),
                item.getScheduledTime(),
                item.getStatus().name()
        );
    }
}
