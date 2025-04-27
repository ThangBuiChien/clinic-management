package com.example.clinic_management.dtos.responses;

import java.time.LocalDate;
import java.util.List;

import com.example.clinic_management.enums.LabTest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationDetailResponseDTO {

    private Long id;
    private String patientName;

    private String doctorName;

    private LabTest examinationType;

    private String labDepartment;

    private String labPrice;

    private String status;

    private String examinationResult;

    private LocalDate createdAt;

    private List<ImageResponseDTO> imageResponseDTO;
}
