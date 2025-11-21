package com.barbershop.repository;

import com.barbershop.model.entity.Appointment;
import com.barbershop.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByAppointment(Appointment appointment);

    Boolean existsByAppointment(Appointment appointment);

    Page<Review> findAll(Pageable pageable);

    // TODO: These methods need to be rewritten after AppointmentItem structure
    // Because Appointment no longer has direct 'service' field

    /*
    @Query("SELECT r FROM Review r JOIN r.appointment a JOIN a.items ai WHERE ai.service = :service " +
           "ORDER BY r.createdAt DESC")
    Page<Review> findByService(@Param("service") Service service, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.appointment a JOIN a.items ai WHERE ai.service = :service")
    Double getAverageRatingForService(@Param("service") Service service);
    */
}