package com.example.etlparser.dao.in;

import com.example.etlparser.entity.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PazienteDAO extends JpaRepository<Paziente,Long> {
}
