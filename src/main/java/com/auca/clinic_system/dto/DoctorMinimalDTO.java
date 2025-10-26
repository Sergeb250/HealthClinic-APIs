package com.auca.clinic_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorMinimalDTO {
    private Long id;
    private String name;
    private String specialization;
    private LocationDTO address;
}
