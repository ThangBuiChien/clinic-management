package com.example.clinic_management.dtos.requests;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationDetailRequestDTO {

//    private String patientName;
//
//    private String doctorName;

    private String examinationType;

    private String examinationResult;
}
