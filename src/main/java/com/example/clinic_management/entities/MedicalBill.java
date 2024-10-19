package com.example.clinic_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medical_bill")
public class MedicalBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "patient_dob", nullable = false)
    private String patientDob;

    @Column(name = "patient_gender", nullable = false)
    private String patientGender;

    @Column(name = "symptom_name", nullable = false)
    private String symptomName;

    @Size(max = 3070, message = "Syndrome description must be less than 3070 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,!?\"'()-]*$",
            message = "Syndrome description can only contain letters, numbers, space and basic punctuations"
    )
    @Column(name = "syndrome")
    private String syndrome;

    @ElementCollection
    @CollectionTable(name = "medical_bill_drug_names", joinColumns = @JoinColumn(name = "medical_bill_id"))
    @Column(name = "drug_name")
    private List<String> drugNames;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescribed_drugs_id")
    private PrescribedDrug prescribedDrug;

}














