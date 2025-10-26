package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByProvinceId(Long provinceId);
    List<District> findByProvinceName(String provinceName);
}
