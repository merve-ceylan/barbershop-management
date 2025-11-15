package com.barbershop.repository;

import com.barbershop.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByActiveTrue();

    List<Staff> findByActiveTrueOrderByNameAsc();
}
