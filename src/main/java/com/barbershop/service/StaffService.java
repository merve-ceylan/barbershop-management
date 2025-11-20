package com.barbershop.service;

import com.barbershop.exception.ResourceNotFoundException;
import com.barbershop.model.dto.response.StaffResponse;
import com.barbershop.model.entity.Staff;
import com.barbershop.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {

    private final StaffRepository staffRepository;

    /**
     * Get all active staff
     */
    public List<StaffResponse> getAllActiveStaff() {
        List<Staff> staffList = staffRepository.findByActiveTrueOrderByNameAsc();
        return staffList.stream()
                .map(StaffResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all staff (including inactive) - Admin only
     */
    public List<StaffResponse> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();
        return staffList.stream()
                .map(StaffResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find staff by ID
     */
    public Staff findById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }

    /**
     * Get staff response by ID
     */
    public StaffResponse getStaffById(Long id) {
        Staff staff = findById(id);
        return StaffResponse.fromEntity(staff);
    }

    /**
     * Create new staff
     */
    public StaffResponse createStaff(Staff staff) {
        // Set defaults
        if (staff.getActive() == null) {
            staff.setActive(true);
        }
        if (staff.getWorkStartTime() == null) {
            staff.setWorkStartTime(LocalTime.of(9, 0));
        }
        if (staff.getWorkEndTime() == null) {
            staff.setWorkEndTime(LocalTime.of(18, 0));
        }

        Staff savedStaff = staffRepository.save(staff);
        return StaffResponse.fromEntity(savedStaff);
    }

    /**
     * Update staff information
     */
    public StaffResponse updateStaff(Long id, Staff updateRequest) {
        Staff staff = findById(id);

        // Update fields
        if (updateRequest.getName() != null) {
            staff.setName(updateRequest.getName());
        }
        if (updateRequest.getPhone() != null) {
            staff.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getEmail() != null) {
            staff.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getSpecialties() != null) {
            staff.setSpecialties(updateRequest.getSpecialties());
        }
        if (updateRequest.getPhotoUrl() != null) {
            staff.setPhotoUrl(updateRequest.getPhotoUrl());
        }
        if (updateRequest.getWorkStartTime() != null) {
            staff.setWorkStartTime(updateRequest.getWorkStartTime());
        }
        if (updateRequest.getWorkEndTime() != null) {
            staff.setWorkEndTime(updateRequest.getWorkEndTime());
        }
        if (updateRequest.getActive() != null) {
            staff.setActive(updateRequest.getActive());
        }

        Staff updatedStaff = staffRepository.save(staff);
        return StaffResponse.fromEntity(updatedStaff);
    }

    /**
     * Deactivate staff (soft delete)
     */
    public void deactivateStaff(Long id) {
        Staff staff = findById(id);
        staff.setActive(false);
        staffRepository.save(staff);
    }

    /**
     * Activate staff
     */
    public void activateStaff(Long id) {
        Staff staff = findById(id);
        staff.setActive(true);
        staffRepository.save(staff);
    }

    /**
     * Delete staff permanently
     */
    public void deleteStaff(Long id) {
        Staff staff = findById(id);
        staffRepository.delete(staff);
    }
}
