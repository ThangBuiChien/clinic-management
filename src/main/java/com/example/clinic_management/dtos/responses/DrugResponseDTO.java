package com.example.clinic_management.dtos.responses;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrugResponseDTO {
    private Long id;
    private String name;
    private String standardDosage;
    private String drugFunction;
    private int unit;
    private String sideEffect;

}
