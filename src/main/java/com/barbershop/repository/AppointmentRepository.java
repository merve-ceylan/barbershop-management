package com.barbershop.repository;

import com.barbershop.model.entity.Appointment;
import com.barbershop.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomer(User customer);

    Page<Appointment> findByCustomer(User customer, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findAppointmentsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND " +
            "a.appointmentDateTime BETWEEN :start AND :end")
    Page<Appointment> findByStatusAndDateBetween(
            @Param("status") Appointment.AppointmentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query("SELECT a FROM Appointment a WHERE " +
            "DATE(a.appointmentDateTime) = DATE(:date) " +
            "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByDate(@Param("date") LocalDateTime date);

    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.items WHERE a.id = :id")
    Appointment findByIdWithItems(@Param("id") Long id);
}