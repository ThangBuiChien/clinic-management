package com.example.clinic_management.entities;

import java.time.DayOfWeek;
import java.util.Set;

import jakarta.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor extends UserAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Department department;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> workingDays;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    public boolean isWorkingDay(DayOfWeek dayOfWeek) {
        return workingDays.contains(dayOfWeek);
    }
}
