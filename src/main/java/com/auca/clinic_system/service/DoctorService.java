package com.auca.clinic_system.service;

import com.auca.clinic_system.dto.*;
import com.auca.clinic_system.entity.*;
import com.auca.clinic_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public Optional<DoctorDTO> getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<DoctorMinimalDTO> getDoctorMinimalById(Long id) {
        return doctorRepository.findById(id)
                .map(this::convertToMinimalDTO);
    }
    
    public List<DoctorMinimalDTO> searchDoctorsByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToMinimalDTO)
                .collect(Collectors.toList());
    }
    
    public List<DoctorMinimalDTO> searchDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization).stream()
                .map(this::convertToMinimalDTO)
                .collect(Collectors.toList());
    }
    
    public List<DoctorMinimalDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(this::convertToMinimalDTO)
                .collect(Collectors.toList());
    }
    
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = convertToEntity(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDTO(savedDoctor);
    }
    
    public Optional<DoctorDTO> updateDoctor(Long id, DoctorDTO doctorDTO) {
        return doctorRepository.findById(id)
                .map(existingDoctor -> {
                    existingDoctor.setName(doctorDTO.getName());
                    existingDoctor.setSpecialization(doctorDTO.getSpecialization());
                    existingDoctor.setPhone(doctorDTO.getPhone());
                    existingDoctor.setEmail(doctorDTO.getEmail());
                    
                    Doctor updatedDoctor = doctorRepository.save(existingDoctor);
                    return convertToDTO(updatedDoctor);
                });
    }
    
    public boolean deleteDoctor(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setPhone(doctor.getPhone());
        dto.setEmail(doctor.getEmail());
        
        // Convert person location to address
        if (doctor.getPerson() != null && doctor.getPerson().getVillage() != null) {
            dto.setAddress(convertVillageToLocation(doctor.getPerson().getVillage()));
        }
        
        // Convert appointments to summaries
        if (doctor.getAppointments() != null) {
            List<AppointmentSummaryDTO> summaries = doctor.getAppointments().stream()
                    .map(appointment -> {
                        AppointmentSummaryDTO summary = new AppointmentSummaryDTO();
                        summary.setId(appointment.getId());
                        summary.setDate(appointment.getAppointmentDate());
                        summary.setPatientName(appointment.getPatient().getName());
                        summary.setDoctorName(doctor.getName());
                        summary.setStatus(appointment.getStatus());
                        return summary;
                    })
                    .collect(Collectors.toList());
            dto.setAppointments(summaries);
        }
        
        return dto;
    }
    
    public DoctorMinimalDTO convertToMinimalDTO(Doctor doctor) {
        DoctorMinimalDTO dto = new DoctorMinimalDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization());
        
        // Convert person location to address
        if (doctor.getPerson() != null && doctor.getPerson().getVillage() != null) {
            dto.setAddress(convertVillageToLocation(doctor.getPerson().getVillage()));
        }
        
        return dto;
    }
    
    public Doctor convertToEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPhone(dto.getPhone());
        doctor.setEmail(dto.getEmail());
        return doctor;
    }
    
    private LocationDTO convertVillageToLocation(Village village) {
        LocationDTO location = new LocationDTO();
        location.setVillageId(village.getId());
        location.setVillageName(village.getName());
        
        if (village.getCell() != null) {
            Cell cell = village.getCell();
            location.setCellId(cell.getId());
            location.setCellName(cell.getName());
            
            if (cell.getSector() != null) {
                Sector sector = cell.getSector();
                location.setSectorId(sector.getId());
                location.setSectorName(sector.getName());
                
                if (sector.getDistrict() != null) {
                    District district = sector.getDistrict();
                    location.setDistrictId(district.getId());
                    location.setDistrictName(district.getName());
                    
                    if (district.getProvince() != null) {
                        Province province = district.getProvince();
                        location.setProvinceId(province.getId());
                        location.setProvinceName(province.getName());
                    }
                }
            }
        }
        
        return location;
    }
}
