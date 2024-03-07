package com.example.etlparser.dao.in;

import com.example.etlparser.entity.Ospedalizzazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OspedalizzazioneDAO extends JpaRepository<Ospedalizzazione,Long> {
}
