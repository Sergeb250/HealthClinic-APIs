package com.auca.clinic_system.controller;

import com.auca.clinic_system.entity.*;
import com.auca.clinic_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    
    @Autowired
    private ProvinceRepository provinceRepository;
    
    @Autowired
    private DistrictRepository districtRepository;
    
    @Autowired
    private SectorRepository sectorRepository;
    
    @Autowired
    private CellRepository cellRepository;
    
    @Autowired
    private VillageRepository villageRepository;
    
    // Province endpoints
    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(provinceRepository.findAll());
    }
    
    @GetMapping("/provinces/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        return provinceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/provinces")
    public ResponseEntity<Province> createProvince(@RequestBody Province province) {
        Province savedProvince = provinceRepository.save(province);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProvince);
    }
    
    // District endpoints
    @GetMapping("/districts")
    public ResponseEntity<List<District>> getAllDistricts() {
        return ResponseEntity.ok(districtRepository.findAll());
    }
    
    @GetMapping("/districts/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable Long id) {
        return districtRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/provinces/{provinceId}/districts")
    public ResponseEntity<List<District>> getDistrictsByProvince(@PathVariable Long provinceId) {
        return ResponseEntity.ok(districtRepository.findByProvinceId(provinceId));
    }
    
    @PostMapping("/districts")
    public ResponseEntity<District> createDistrict(@RequestBody District district) {
        District savedDistrict = districtRepository.save(district);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDistrict);
    }
    
    // Sector endpoints
    @GetMapping("/sectors")
    public ResponseEntity<List<Sector>> getAllSectors() {
        return ResponseEntity.ok(sectorRepository.findAll());
    }
    
    @GetMapping("/sectors/{id}")
    public ResponseEntity<Sector> getSectorById(@PathVariable Long id) {
        return sectorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/districts/{districtId}/sectors")
    public ResponseEntity<List<Sector>> getSectorsByDistrict(@PathVariable Long districtId) {
        return ResponseEntity.ok(sectorRepository.findByDistrictId(districtId));
    }
    
    @PostMapping("/sectors")
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector) {
        Sector savedSector = sectorRepository.save(sector);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSector);
    }
    
    // Cell endpoints
    @GetMapping("/cells")
    public ResponseEntity<List<Cell>> getAllCells() {
        return ResponseEntity.ok(cellRepository.findAll());
    }
    
    @GetMapping("/cells/{id}")
    public ResponseEntity<Cell> getCellById(@PathVariable Long id) {
        return cellRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sectors/{sectorId}/cells")
    public ResponseEntity<List<Cell>> getCellsBySector(@PathVariable Long sectorId) {
        return ResponseEntity.ok(cellRepository.findBySectorId(sectorId));
    }
    
    @PostMapping("/cells")
    public ResponseEntity<Cell> createCell(@RequestBody Cell cell) {
        Cell savedCell = cellRepository.save(cell);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCell);
    }
    
    // Village endpoints
    @GetMapping("/villages")
    public ResponseEntity<List<Village>> getAllVillages() {
        return ResponseEntity.ok(villageRepository.findAll());
    }
    
    @GetMapping("/villages/{id}")
    public ResponseEntity<Village> getVillageById(@PathVariable Long id) {
        return villageRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cells/{cellId}/villages")
    public ResponseEntity<List<Village>> getVillagesByCell(@PathVariable Long cellId) {
        return ResponseEntity.ok(villageRepository.findByCellId(cellId));
    }
    
    @PostMapping("/villages")
    public ResponseEntity<Village> createVillage(@RequestBody Village village) {
        Village savedVillage = villageRepository.save(village);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVillage);
    }
}
