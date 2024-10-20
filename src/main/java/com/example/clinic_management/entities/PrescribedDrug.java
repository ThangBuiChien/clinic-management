package com.example.clinic_management.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prescribed_drugs")
public class PrescribedDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_bill_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MedicalBill medicalBill;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drug_id")
    private Drug drug;

    private int dosage;
    private int duration;
    private String frequency;
    private String specialInstructions;
}