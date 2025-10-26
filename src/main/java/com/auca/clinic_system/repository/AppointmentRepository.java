package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Appointment;
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
    List<Appointment> findByDoctorId(Long doctorId);
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByStatus(String status);
    
    Page<Appointment> findAll(Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate >= :startDate AND a.appointmentDate <= :endDate")
    List<Appointment> findDoctorAppointmentsBetweenDates(
        @Param("doctorId") Long doctorId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.appointmentDate DESC")
    List<Appointment> findPatientAppointmentsOrderByDateDesc(@Param("patientId") Long patientId);
}
