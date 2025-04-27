package com.example.clinic_management.entities;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "symptom")
public class Symptom {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Symptom name is required")
    @Size(max = 100, message = "Symptom name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Symptom name can only contain letters, numbers and space")
    @Column(unique = true)
    private String name;

    //    @NotBlank(message = "Symptom description is required")
    @Size(max = 3070, message = "Symptom description must be less than 3070 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,!?\"'()-]*$",
            message = "Symptom description can only contain letters, numbers, space and basic punctuations")
    @Column(unique = true)
    private String description;

    @OneToMany(mappedBy = "symptom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescribedDrug> prescribedDrugs;

    // helper method to add prescribed drug
    public void addPrescribedDrug(PrescribedDrug prescribedDrug) {
        this.prescribedDrugs.add(prescribedDrug);
        prescribedDrug.setSymptom(this);
    }

    public void addPrescribedDrugs(List<PrescribedDrug> prescribedDrugs) {
        for (PrescribedDrug prescribedDrug : prescribedDrugs) {
            this.addPrescribedDrug(prescribedDrug);
        }
    }
}
