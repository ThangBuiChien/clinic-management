package com.example.clinic_management.dtos.requests;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.*;

import com.example.clinic_management.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRequestDTO {
    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotNull(message = "Citizen ID is required")
    @Min(value = 1000000000L, message = "Citizen ID must be 10 digits")
    @Max(value = 9999999999L, message = "Citizen ID must be 10 digits")
    private long citizenId;

    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Working days are required")
    private Set<DayOfWeek> workingDays;
}
