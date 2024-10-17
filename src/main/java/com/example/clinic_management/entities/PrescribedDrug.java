package com.example.clinic_management.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prescribed_drugs")
public class PrescribedDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drug_id")
    private Drug drug;

    private String symtomName;
    private String dosage;
    private String specialInstructions;
}