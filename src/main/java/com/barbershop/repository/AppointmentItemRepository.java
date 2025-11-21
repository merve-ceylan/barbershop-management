package com.barbershop.repository;

import com.barbershop.model.entity.AppointmentItem;
import com.barbershop.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentItemRepository extends JpaRepository<AppointmentItem, Long> {

    @Query("SELECT ai FROM AppointmentItem ai WHERE ai.staff = :staff AND " +
            "ai.scheduledTime BETWEEN :start AND :end AND " +
            "ai.status NOT IN ('CANCELLED')")
    List<AppointmentItem> findStaffSchedule(
            @Param("staff") Staff staff,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Simplified conflict check - we'll do time calculation in service layer
    @Query("SELECT ai FROM AppointmentItem ai WHERE " +
            "ai.staff = :staff AND " +
            "ai.scheduledTime BETWEEN :startTime AND :endTime AND " +
            "ai.status NOT IN ('CANCELLED')")
    List<AppointmentItem> findConflictingAppointments(
            @Param("staff") Staff staff,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}