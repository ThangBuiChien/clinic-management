package com.example.clinic_management.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FeedbackResponseDTO {

    private int rating;

    private String comment;

    private DoctorResponseDTO doctorResponseDTO;

}
