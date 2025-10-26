package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
    List<Village> findByCellId(Long cellId);
}
