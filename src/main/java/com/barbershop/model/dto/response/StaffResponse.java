package com.barbershop.model.dto.response;

import com.barbershop.model.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String specialties;
    private String photoUrl;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Boolean active;

    public static StaffResponse fromEntity(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getName(),
                staff.getPhone(),
                staff.getEmail(),
                staff.getSpecialties(),
                staff.getPhotoUrl(),
                staff.getWorkStartTime(),
                staff.getWorkEndTime(),
                staff.getActive()
        );
    }
}
