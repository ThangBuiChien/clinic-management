package com.example.clinic_management.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "examination_detail")
public class ExaminationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    private String doctorName;

    @Column(name = "examination_type")
    private String examinationType;

    @Column(name = "examination_result")
    private String examinationResult;

    @ManyToOne(fetch = FetchType.LAZY /*, optional = false */)
    @JoinColumn(name = "medical_bill_id" /*, nullable = false*/)
    private MedicalBill medicalBill;

    @OneToMany(mappedBy = "examinationDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imagesTest = new ArrayList<>();

    public void addImage(Image image){
        this.imagesTest.add(image);
        image.setExaminationDetail(this);
    }

    public void addImage(List<Image> images){
        for (Image image : images) {
            addImage(image);
        }

    }

}
