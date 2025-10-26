package com.auca.clinic_system.repository;

import com.auca.clinic_system.entity.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    List<Cell> findBySectorId(Long sectorId);
}
