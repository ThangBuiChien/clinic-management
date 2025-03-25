package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackCreateDTO {

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comment;
}
