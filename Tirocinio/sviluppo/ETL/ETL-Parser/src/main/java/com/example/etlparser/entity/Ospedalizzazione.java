package com.example.etlparser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ospedalizzazione {

    @Id
    private Long admissionNumber;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="serial_number")
    private Paziente paziente;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Analisi analisi;
    private LocalDate dateAdmission;
    private LocalDate dateDischarge;
    @Enumerated(EnumType.STRING)
    private TOA typeOfAdmissionEmergencyOPD;
    private Integer durationOfStay;
    private Integer durationOfIntensiveUnitStay;
    @Enumerated(EnumType.STRING)
    private Outcame outcame;
    private Boolean severeAnemia;
    private Boolean anemia;
    private Boolean stableAngina;
    private Boolean acuteCoronarySyndrome;
    private Boolean stElevationMyocardialInfraction;
    private Boolean atypicalChestPain;
    private Boolean heartFailure;
    private Boolean heartFailureWithReducedFraction;
    private Boolean heartFailureWithNormalFraction;
    private Boolean valvularHeartDisease;
    private Boolean completeHeartBlock;
    private Boolean sickSinusSyndrome;
    private Boolean acuteKidneyInjury;
    private Boolean cerebrovascularAccidentInfract;
    private Boolean cerebrovascularAccidentBleed;
    private Boolean atrialFibrilation;
    private Boolean ventricularTachycardia;
    private Boolean paroxysmalSupraVentricularTachycardia;
    private Boolean congenitalHeartDisease;
    private Boolean urinaryTractInfection;
    private Boolean neuroCardiogenicSyncope;
    private Boolean orthostatic;
    private Boolean infectiveEndocarditis;
    private Boolean deepVenousThrombosis;
    private Boolean cardiogenicShock;
    private Boolean shock;
    private Boolean pulmonaryShock;
    private Boolean chestInfection;

}
