package com.example.clinic_management.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import com.example.clinic_management.enums.AccountStatus;
import com.example.clinic_management.enums.Gender;
import com.example.clinic_management.enums.Role;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@MappedSuperclass
public abstract class UserAbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotNull(message = "Citizen ID is required")
    @Min(value = 1000000000L, message = "Citizen ID must be 10 digits")
    @Max(value = 9999999999L, message = "Citizen ID must be 10 digits")
    private long citizenId;

    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email
    @NaturalId
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "gender")
    @NotNull(message = "gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address")
    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @Column(name = "birth_date")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private Role role;

    private AccountStatus status;
}
