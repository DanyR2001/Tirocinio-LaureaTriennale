package com.example.etlparser.dto;

import com.example.etlparser.entity.Gender;
import com.example.etlparser.entity.Ospedalizzazione;
import com.example.etlparser.entity.Paziente;
import com.example.etlparser.entity.Rural;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PazienteDTO extends Paziente {
    public PazienteDTO(Long serialNumber, Integer age, Gender gender, Rural rural, Boolean smoking, Boolean alcohol, Boolean diabetesMellitus, Boolean hypertension, Boolean coronaryArteryDisease, Boolean cardiomyopahty, Boolean chronicKidneyDisease, List<Ospedalizzazione> lista) {
        super(serialNumber, age, gender, rural, smoking, alcohol, diabetesMellitus, hypertension, coronaryArteryDisease, cardiomyopahty, chronicKidneyDisease, lista);
    }

    public Paziente toEntity(){
        return new Paziente(super.getSerialNumber(),super.getAge(),super.getGender(),super.getRural(),super.getSmoking(),super.getAlcohol(),super.getDiabetesMellitus(),super.getHypertension(),super.getCoronaryArteryDisease(),super.getCardiomyopahty(),super.getChronicKidneyDisease(),super.getLista());
    }

}
