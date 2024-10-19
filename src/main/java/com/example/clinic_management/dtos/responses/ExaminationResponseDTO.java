package com.example.clinic_management.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationResponseDTO {
    private Long id;
    private String patientName;
    private String examinationType;
    private String examinationResult;
}
