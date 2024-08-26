package com.example.clinic_management.dtos.responses;

import java.util.List;

import com.example.clinic_management.entities.Doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private List<Doctor> doctors;
}
