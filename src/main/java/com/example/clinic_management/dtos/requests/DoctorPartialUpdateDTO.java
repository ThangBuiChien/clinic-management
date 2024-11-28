package com.example.clinic_management.dtos.requests;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import com.example.clinic_management.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorPartialUpdateDTO {
    private String fullName;
    private Long citizenId;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate birthDate;
    private Long departmentId;
    private Long scheduleId;
    Set<DayOfWeek> workingDays;
}
