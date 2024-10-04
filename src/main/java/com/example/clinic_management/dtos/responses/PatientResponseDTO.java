package com.example.clinic_management.dtos.responses;

import java.time.LocalDate;

import com.example.clinic_management.enums.AccountStatus;
import com.example.clinic_management.enums.Gender;
import com.example.clinic_management.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDTO {
    private Long id;
    private String fullName;
    private Long citizenId;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate birthDate;
    private Role role;
    private AccountStatus status;
}