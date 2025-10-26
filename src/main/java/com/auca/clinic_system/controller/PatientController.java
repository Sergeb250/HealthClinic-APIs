package com.auca.clinic_system.controller;

import com.auca.clinic_system.dto.PatientDTO;
import com.auca.clinic_system.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy) {
        
        if (page != null && size != null) {
            Sort sort = sortBy != null ? Sort.by(sortBy) : Sort.by("id");
            Page<PatientDTO> patients = patientService.getAllPatients(PageRequest.of(page, size, sort));
            return ResponseEntity.ok(patients.getContent());
        }
        
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<PatientDTO>> getPatientsByGender(@PathVariable String gender) {
        return ResponseEntity.ok(patientService.getPatientsByGender(gender));
    }
    
    @GetMapping("/location/province/{provinceId}")
    public ResponseEntity<List<PatientDTO>> getPatientsByProvince(@PathVariable Long provinceId) {
        return ResponseEntity.ok(patientService.getPatientsByProvince(provinceId));
    }
    
    @GetMapping("/location/district/{districtId}")
    public ResponseEntity<List<PatientDTO>> getPatientsByDistrict(@PathVariable Long districtId) {
        return ResponseEntity.ok(patientService.getPatientsByDistrict(districtId));
    }
    
    @GetMapping("/location/sector/{sectorId}")
    public ResponseEntity<List<PatientDTO>> getPatientsBySector(@PathVariable Long sectorId) {
        return ResponseEntity.ok(patientService.getPatientsBySector(sectorId));
    }
    
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO savedPatient = patientService.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatient(id, patientDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        if (patientService.deletePatient(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
