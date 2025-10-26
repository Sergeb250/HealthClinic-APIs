package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmail(String email);
    
    Optional<Patient> findByEmail(String email);
    
    List<Patient> findByGender(String gender);
    
    List<Patient> findByVillage_Cell_Sector_District_Province_Id(Long provinceId);
    
    List<Patient> findByVillage_Cell_Sector_District_Id(Long districtId);
    
    List<Patient> findByVillage_Cell_Sector_Id(Long sectorId);
    
    List<Patient> findByVillage_Cell_Id(Long cellId);
    
    List<Patient> findByVillage_Id(Long villageId);
    
    Page<Patient> findAll(Pageable pageable);
    
    Page<Patient> findByGender(String gender, Pageable pageable);
}
