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
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private VillageRepository villageRepository;
    
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public Optional<PatientDTO> getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<PatientDTO> getPatientsByGender(String gender) {
        return patientRepository.findByGender(gender).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<PatientDTO> getPatientsByProvince(Long provinceId) {
        return patientRepository.findByVillage_Cell_Sector_District_Province_Id(provinceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<PatientDTO> getPatientsByDistrict(Long districtId) {
        return patientRepository.findByVillage_Cell_Sector_District_Id(districtId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<PatientDTO> getPatientsBySector(Long sectorId) {
        return patientRepository.findByVillage_Cell_Sector_Id(sectorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }
    
    public Optional<PatientDTO> updatePatient(Long id, PatientDTO patientDTO) {
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    existingPatient.setName(patientDTO.getName());
                    existingPatient.setDob(patientDTO.getDob());
                    existingPatient.setGender(patientDTO.getGender());
                    existingPatient.setPhone(patientDTO.getPhone());
                    existingPatient.setEmail(patientDTO.getEmail());
                    
                    Patient updatedPatient = patientRepository.save(existingPatient);
                    return convertToDTO(updatedPatient);
                });
    }
    
    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setDob(patient.getDob());
        dto.setGender(patient.getGender());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        
        // Calculate age from date of birth
        if (patient.getDob() != null) {
            dto.setAge(Period.between(patient.getDob(), LocalDate.now()).getYears());
        }
        
        // Convert village to location hierarchy
        if (patient.getVillage() != null) {
            dto.setLocation(convertVillageToLocation(patient.getVillage()));
        }
        
        // Convert appointments to summaries
        if (patient.getAppointments() != null) {
            List<AppointmentSummaryDTO> summaries = patient.getAppointments().stream()
                    .map(appointment -> {
                        AppointmentSummaryDTO summary = new AppointmentSummaryDTO();
                        summary.setId(appointment.getId());
                        summary.setDate(appointment.getAppointmentDate());
                        summary.setDoctorName(appointment.getDoctor().getName());
                        summary.setPatientName(patient.getName());
                        summary.setStatus(appointment.getStatus());
                        return summary;
                    })
                    .collect(Collectors.toList());
            dto.setAppointments(summaries);
        }
        
        return dto;
    }
    
    public Patient convertToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        
        // Set village if location is provided
        if (dto.getLocation() != null && dto.getLocation().getVillageId() != null) {
            villageRepository.findById(dto.getLocation().getVillageId())
                    .ifPresent(patient::setVillage);
        }
        
        return patient;
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
