package com.example.clinic_management.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.enums.LabTest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Repository
public interface ExaminationRepository extends JpaRepository<ExaminationDetail, Long> {

    @Query(
            "SELECT e FROM ExaminationDetail e WHERE e.examinationType = :examinationType AND SIZE(e.imagesTest) = 0 ORDER BY e.id")
    Page<ExaminationDetail> findByExaminationTypeAndImagesTestIsEmpty(
            @Param("examinationType") LabTest examinationType, Pageable pageable);


    @Query(
            "SELECT e FROM ExaminationDetail e WHERE e.examinationType IN :labTests AND SIZE(e.imagesTest) = 0 ORDER BY e.id")
    Page<ExaminationDetail> findByDepartmentAndImagesTestIsEmpty(
            @Param("labTests") Collection<LabTest> labTests, Pageable pageable);

    @Query("SELECT e FROM ExaminationDetail e WHERE e.examinationType IN :labTests AND SIZE(e.imagesTest) = 0 AND e.createdAt = :createdAt ORDER BY e.id")
    Page<ExaminationDetail> findByDepartmentAndImagesTestIsEmptyAndCreatedAt(
            @Param("labTests") Collection<LabTest> labTests,
            @Param("createdAt") LocalDate createdAt,
            Pageable pageable);
    @Query("SELECT e FROM ExaminationDetail e WHERE e.examinationType = :examinationType AND SIZE(e.imagesTest) = 0 AND e.createdAt = :createdAt ORDER BY e.id")
    Page<ExaminationDetail> findExaminationTypeAndImagesTestIsEmptyAndCreatedAt(
            @Param("examinationType") LabTest examinationType,
            @Param("createdAt") LocalDate createdAt,
            Pageable pageable);


}
