package com.example.etlparser.service.in;

import com.example.etlparser.dto.PazienteDTO;
import com.example.etlparser.entity.Paziente;

public interface ParserService {

    public void uploadPatientHistory(PazienteDTO paziente);
}
