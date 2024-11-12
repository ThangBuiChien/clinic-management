package com.example.clinic_management.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResponseDTO {
    private Long id;
    private String fileName;
    private String downloadUrl;
}
