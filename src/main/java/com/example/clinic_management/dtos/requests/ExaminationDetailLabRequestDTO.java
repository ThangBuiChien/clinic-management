package com.example.clinic_management.dtos.requests;

import com.example.clinic_management.enums.LabTest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationDetailLabRequestDTO {

    private LabTest examinationType;
}
