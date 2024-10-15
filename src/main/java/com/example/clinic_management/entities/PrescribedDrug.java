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

    @Column(nullable = false)
    private String templateName;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug; // Assuming you have a Drug entity

    @Column(nullable = false)
    private Integer dosage;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String frequency;

    @Column(length = 200)
    private String specialInstructions;
}
