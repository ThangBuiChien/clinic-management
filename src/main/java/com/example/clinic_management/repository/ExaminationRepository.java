package com.example.clinic_management.repository;

import com.example.clinic_management.enums.LabDepartment;
import com.example.clinic_management.enums.LabTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.ExaminationDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<ExaminationDetail, Long> {

    @Query("SELECT e FROM ExaminationDetail e WHERE e.examinationType = :examinationType AND SIZE(e.imagesTest) = 0")
    Page<ExaminationDetail> findByExaminationTypeAndImagesTestIsEmpty(@Param("examinationType") LabTest examinationType, Pageable pageable);

    @Query("SELECT e FROM ExaminationDetail e WHERE e.examinationType IN :labTests AND SIZE(e.imagesTest) = 0")
    Page<ExaminationDetail> findByDepartmentAndImagesTestIsEmpty(@Param("labTests") Collection<LabTest> labTests, Pageable pageable);

}
