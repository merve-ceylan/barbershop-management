package com.barbershop.repository;

import com.barbershop.model.entity.Appointment;
import com.barbershop.model.entity.Review;
import com.barbershop.model.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByAppointment(Appointment appointment);

    Boolean existsByAppointment(Appointment appointment);

    @Query("SELECT r FROM Review r JOIN r.appointment a WHERE a.service = :service " +
            "ORDER BY r.createdAt DESC")
    Page<Review> findByService(@Param("service") Service service, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.appointment a WHERE a.service = :service")
    Double getAverageRatingForService(@Param("service") Service service);
}
