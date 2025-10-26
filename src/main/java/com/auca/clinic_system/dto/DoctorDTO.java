package com.auca.clinic_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private LocationDTO address;
    private List<AppointmentSummaryDTO> appointments = new ArrayList<>();
}
