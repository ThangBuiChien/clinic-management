package com.example.clinic_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "drug")
public class Drug {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Drug name is required")
    @Size(max = 100, message = "Service name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Service name can only contain letters, numbers and spaces")
    @Column(unique = true)
    @Length(min = 1, max = 100, message = "Drug name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Standard dosage is required")
    @Length(min = 1, max = 255, message = "Standard dosage must be between 1 and 255 characters")
    private String standardDosage;

    @NotBlank(message = "Drug function is required")
    @Length(min = 1, max = 255, message = "Drug function must be between 1 and 255 characters")
    private String drugFunction;

    @Min(value = 1, message = "Unit must be a positive integer")
    private int unit;

    @NotBlank(message = "Drug Effects is required")
    @Length(min = 1, max = 255, message = "Drug Effects must be between 1 and 255 characters")
    private String sideEffect;
}
