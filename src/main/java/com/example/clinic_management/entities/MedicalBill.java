package com.example.clinic_management.entities;

import com.example.clinic_management.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @Column(name = "symptom_name")
    private String symptomName;

    @Size(max = 3070, message = "Syndrome description must be less than 3070 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,!?\"'()-]*$",
            message = "Syndrome description can only contain letters, numbers, space and basic punctuations")
    private String syndrome;

    @ElementCollection
    @CollectionTable(
            name = "medical_bill_drug_names",
            joinColumns = @JoinColumn(name = "medical_bill_id")
    )
    @Column(name = "drug_name")
    private List<String> drugName;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "prescribed_drugs_id", referencedColumnName = "id")
    private PrescribedDrug prescribedDrug;

    // Convenience methods for patient info
    public String getPatientName() {
        return patient.getFullName();
    }

    public LocalDate getPatientBirthDate() {
        return patient.getBirthDate();
    }

    public Gender getPatientGender() {
        return patient.getGender();
    }

    // Convenience methods for prescribed drug info
    public Drug getPrescribedDrug() {
        return prescribedDrug.getDrug();
    }

    public String getPrescribedDrugSymptom() {
        return prescribedDrug.getSymtomName();
    }

    public String getPrescribedDrugDosage() {
        return prescribedDrug.getDosage();
    }

    public String getPrescribedDrugSpeIns() {
        return prescribedDrug.getSpecialInstructions();
    }

    // Helper methods to set prescribed drug info from direct fields
    public void copyPrescribedDrugInfo() {
        if (prescribedDrug != null) {
            this.symptomName = prescribedDrug.getSymtomName();
//            this.syndrome = prescribedDrug.getSyndrome();
            // Assuming Drug entity has a name field
            this.drugName = List.of(prescribedDrug.getDrug().getName());
            this.dosage = prescribedDrug.getDosage();
            this.specialInstructions = prescribedDrug.getSpecialInstructions();
        }
    }
}