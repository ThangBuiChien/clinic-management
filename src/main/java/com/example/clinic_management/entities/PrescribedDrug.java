package com.example.clinic_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "prescribed_drugs")
public class PrescribedDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "prescribed_drug_drugs",
            joinColumns = @JoinColumn(name = "prescribed_drug_id"),
            inverseJoinColumns = @JoinColumn(name = "drug_id")
    )
    private List<Drug> drugs;

    private String symtomName;
    private String dosage;
    private String specialInstructions;



}