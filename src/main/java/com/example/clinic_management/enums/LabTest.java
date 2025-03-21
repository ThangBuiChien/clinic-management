package com.example.clinic_management.enums;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum LabTest {

    WHITE_BLOOD_CELL_COUNT(LabDepartment.HEMATOLOGY),
    RED_BLOOD_CELL_COUNT(LabDepartment.HEMATOLOGY),
    PLATELET_COUNT(LabDepartment.HEMATOLOGY),
    HEMOGLOBIN(LabDepartment.HEMATOLOGY),
    LIVER_FUNCTION(LabDepartment.BIOCHEMISTRY),
    BLOOD_GLUCOSE(LabDepartment.BIOCHEMISTRY),
    LIPID_PROFILE(LabDepartment.BIOCHEMISTRY),
    KIDNEY_FUNCTION(LabDepartment.BIOCHEMISTRY),
    X_RAY(LabDepartment.RADIOLOGY),
    MRI(LabDepartment.RADIOLOGY),
    CT_SCAN(LabDepartment.RADIOLOGY),
    ULTRASOUND(LabDepartment.RADIOLOGY);

    private final LabDepartment department;

    LabTest(LabDepartment department) {
        this.department = department;
    }

    public LabDepartment getDepartment() {
        return department;
    }

    public static Set<LabTest> getTestsByDepartment(LabDepartment department) {
        Set<LabTest> tests = EnumSet.noneOf(LabTest.class);
        for (LabTest test : LabTest.values()) {
            if (test.getDepartment() == department) {
                tests.add(test);
            }
        }
        return tests;
    }

    public static Page<LabTest> getTestsByDepartment(LabDepartment department, Pageable pageable) {
        List<LabTest> tests = EnumSet.allOf(LabTest.class).stream()
                .filter(test -> test.getDepartment() == department)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), tests.size());
        List<LabTest> pagedTests = tests.subList(start, end);

        return new PageImpl<>(pagedTests, pageable, tests.size());
    }
}
