package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByNameContainingIgnoreCase(String name);
    
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
    
    boolean existsByEmail(String email);
    
    Optional<Doctor> findByEmail(String email);
    
    Page<Doctor> findAll(Pageable pageable);
    
    Page<Doctor> findBySpecialization(String specialization, Pageable pageable);
    
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<Doctor> findBySpecializationContainingIgnoreCase(String specialization, Pageable pageable);
}
