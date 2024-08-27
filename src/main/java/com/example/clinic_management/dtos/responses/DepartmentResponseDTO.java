package com.example.clinic_management.dtos.responses;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private List<DoctorResponseDTO> doctors;
}
