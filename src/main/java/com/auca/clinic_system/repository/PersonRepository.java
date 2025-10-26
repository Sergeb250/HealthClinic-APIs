package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);
    
    // Complex derived query navigating through location hierarchy
    List<Person> findByVillage_Cell_Sector_District_Province_Name(String provinceName);
    
    List<Person> findByVillage_Cell_Sector_District_Province_Code(String provinceCode);
    
    @Query("SELECT p FROM Person p WHERE p.village.cell.sector.district.province.name = :provinceName")
    List<Person> findByProvinceName(@Param("provinceName") String provinceName);
    
    @Query("SELECT p FROM Person p WHERE p.village.cell.sector.district.province.code = :provinceCode")
    List<Person> findByProvinceCode(@Param("provinceCode") String provinceCode);
    
    Page<Person> findAll(Pageable pageable);
    
    List<Person> findByVillageId(Long villageId);
}
