package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDTO {
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Department name can only contain letters and spaces")
    private String name;
}
