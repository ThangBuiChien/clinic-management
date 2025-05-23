package com.example.clinic_management.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.example.clinic_management.enums.ExaminationDetailStatus;
import com.example.clinic_management.enums.LabTest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "examination_detail")
@EntityListeners(AuditingEntityListener.class)
public class ExaminationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name", nullable = true)
    private String patientName;

    private String doctorName;

    //    @Column(name = "examination_type")
    //    private String examinationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "examination_type")
    private LabTest examinationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExaminationDetailStatus status = ExaminationDetailStatus.UNPAID;

    @Column(name = "examination_result")
    private String examinationResult;

    @ManyToOne(fetch = FetchType.LAZY /*, optional = false */)
    @JoinColumn(name = "medical_bill_id" /*, nullable = false*/)
    private MedicalBill medicalBill;

    @OneToMany(mappedBy = "examinationDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imagesTest = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    public void addImage(Image image) {
        this.imagesTest.add(image);
        image.setExaminationDetail(this);
    }

    public void addImage(List<Image> images) {
        for (Image image : images) {
            addImage(image);
        }
    }

    public boolean isDone() {
        return examinationResult != null;
    }
}
