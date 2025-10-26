package com.auca.clinic_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private LocalDateTime date;
    private String doctorName;
    private String patientName;
    private String status;
    private String notes;
}
