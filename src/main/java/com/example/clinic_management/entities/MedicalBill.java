package com.example.clinic_management.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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
@Table(name = "medical_bill")
public class MedicalBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToMany(mappedBy = "medicalBill", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PrescribedDrug> drugs = new ArrayList<>();

    @OneToMany(mappedBy = "medicalBill", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExaminationDetail> examinationDetails = new ArrayList<>();

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Size(max = 3070, message = "Syndrome description must be less than 3070 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,!?\"'()-]*$",
            message = "Syndrome description can only contain letters, numbers, space and basic punctuations")
    @Column(name = "syndrome")
    private String syndrome;

    private Double weight;
    private Integer heartRate;
    private String bloodPressure;
    private Double temperature;

    private String finalDiagnosis;

    private LocalDate nextAppointmentDate;

    public void addPrescribedDrug(PrescribedDrug prescribedDrug) {
        drugs.add(prescribedDrug);
        prescribedDrug.setMedicalBill(this);
    }

    public void addPrescribedDrugs(List<PrescribedDrug> prescribedDrugs) {
        prescribedDrugs.forEach(this::addPrescribedDrug);
    }

    public void removePrescribedDrug(PrescribedDrug prescribedDrug) {
        drugs.remove(prescribedDrug);
        prescribedDrug.setMedicalBill(null);
    }

    public void addExaminationDetail(ExaminationDetail examinationDetail) {
        examinationDetails.add(examinationDetail);
        examinationDetail.setMedicalBill(this);
    }

    public void addExaminationDetails(List<ExaminationDetail> examinationDetails) {
        examinationDetails.forEach(this::addExaminationDetail);
    }

    public boolean checkIfAllExaminationDetailsAreDone() {
        if(examinationDetails == null) {
            return true;
        }
        return examinationDetails.stream().allMatch(ExaminationDetail::isDone);
    }
}
