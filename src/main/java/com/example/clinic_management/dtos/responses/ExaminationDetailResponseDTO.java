package com.example.clinic_management.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExaminationDetailResponseDTO {

    private Long id;
    private String patientName;

    private String doctorName;

    private String examinationType;

    private String examinationResult;

    private List<ImageResponseDTO> imageResponseDTO;
}
