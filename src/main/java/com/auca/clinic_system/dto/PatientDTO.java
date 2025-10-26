package com.auca.clinic_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private Integer age;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String email;
    private LocationDTO location;
    private List<AppointmentSummaryDTO> appointments = new ArrayList<>();
}
