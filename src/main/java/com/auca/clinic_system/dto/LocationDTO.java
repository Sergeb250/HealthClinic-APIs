package com.auca.clinic_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private Long sectorId;
    private String sectorName;
    private Long cellId;
    private String cellName;
    private Long villageId;
    private String villageName;
}
