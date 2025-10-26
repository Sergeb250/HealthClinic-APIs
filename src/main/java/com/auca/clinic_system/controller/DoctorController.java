package com.auca.clinic_system.controller;

import com.auca.clinic_system.dto.DoctorDTO;
import com.auca.clinic_system.dto.DoctorMinimalDTO;
import com.auca.clinic_system.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;
    
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy) {
        
        if (page != null && size != null) {
            Sort sort = sortBy != null ? Sort.by(sortBy) : Sort.by("id");
            Page<DoctorDTO> doctors = doctorService.getAllDoctors(PageRequest.of(page, size, sort));
            return ResponseEntity.ok(doctors.getContent());
        }
        
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search/name")
    public ResponseEntity<List<DoctorMinimalDTO>> searchDoctorsByName(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctorsByName(name));
    }
    
    @GetMapping("/search/specialization")
    public ResponseEntity<List<DoctorMinimalDTO>> searchDoctorsBySpecialization(@RequestParam String specialization) {
        return ResponseEntity.ok(doctorService.searchDoctorsBySpecialization(specialization));
    }
    
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorMinimalDTO>> getDoctorsBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization));
    }
    
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        DoctorDTO savedDoctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        return doctorService.updateDoctor(id, doctorDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorService.deleteDoctor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
