package com.example.clinic_management.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FeedbackResponseDTO {

    private int rating;

    private String comment;

    private LocalDateTime createdAt;

    private String patientName;

    private String doctorDepartmentName;

    private DoctorResponseDTO doctorResponseDTO;

}
