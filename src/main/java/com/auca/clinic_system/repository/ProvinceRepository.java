package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findByCode(String code);
    Optional<Province> findByName(String name);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
