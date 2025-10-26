package com.auca.clinic_system.service;

import com.auca.clinic_system.dto.AppointmentDTO;
import com.auca.clinic_system.entity.Appointment;
import com.auca.clinic_system.entity.Doctor;
import com.auca.clinic_system.entity.Patient;
import com.auca.clinic_system.repository.AppointmentRepository;
import com.auca.clinic_system.repository.DoctorRepository;
import com.auca.clinic_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public Optional<AppointmentDTO> getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<AppointmentDTO> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentDTO> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = convertToEntity(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }
    
    public Optional<AppointmentDTO> updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        return appointmentRepository.findById(id)
                .map(existingAppointment -> {
                    existingAppointment.setAppointmentDate(appointmentDTO.getDate());
                    existingAppointment.setStatus(appointmentDTO.getStatus());
                    existingAppointment.setNotes(appointmentDTO.getNotes());
                    
                    Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
                    return convertToDTO(updatedAppointment);
                });
    }
    
    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        
        if (appointment.getDoctor() != null) {
            dto.setDoctorName(appointment.getDoctor().getName());
        }
        
        if (appointment.getPatient() != null) {
            dto.setPatientName(appointment.getPatient().getName());
        }
        
        return dto;
    }
    
    public Appointment convertToEntity(AppointmentDTO dto) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(dto.getDate());
        appointment.setStatus(dto.getStatus());
        appointment.setNotes(dto.getNotes());
        return appointment;
    }
}
