package com.auca.clinic_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSummaryDTO {
    private Long id;
    private LocalDateTime date;
    private String patientName;
    private String doctorName;
    private String status;
}
