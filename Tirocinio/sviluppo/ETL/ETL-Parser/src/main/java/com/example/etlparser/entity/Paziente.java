package com.example.etlparser.entity;

import com.example.etlparser.dto.PazienteDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="paziente")
public class Paziente {
    @Id
    private  Long serialNumber;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Rural rural;
    private Boolean smoking;
    private Boolean alcohol;
    private Boolean diabetesMellitus;
    private Boolean hypertension;
    private Boolean coronaryArteryDisease;
    private Boolean cardiomyopahty;
    private Boolean chronicKidneyDisease;
    @OneToMany(mappedBy = "paziente")
    private List<Ospedalizzazione> lista;

    public PazienteDTO toDTO(){
        return new PazienteDTO(serialNumber, age, gender, rural, smoking, alcohol, diabetesMellitus, hypertension,
                coronaryArteryDisease, cardiomyopahty, chronicKidneyDisease, lista);
    }

}
