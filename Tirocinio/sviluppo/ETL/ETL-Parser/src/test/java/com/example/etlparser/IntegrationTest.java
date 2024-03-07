package com.example.etlparser;

import com.example.etlparser.dao.in.AnalisiDAO;
import com.example.etlparser.dao.in.OspedalizzazioneDAO;
import com.example.etlparser.dao.in.PazienteDAO;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class IntegrationTest extends EtlParserApplicationTests{
    @Autowired
    private AnalisiDAO analisiDAO;
    @Autowired
    private OspedalizzazioneDAO ospedalizzazioneDAO;
    @Autowired
    private PazienteDAO pazienteDAO;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadCsv() throws Exception {
        String fileName = "sampleFile.csv";
        String fileContent = "SNO,MRD No.,D.O.A,D.O.D,AGE,GENDER,RURAL,TYPE OF ADMISSION-EMERGENCY/OPD,month year,DURATION OF STAY,duration of intensive unit stay,OUTCOME,SMOKING,ALCOHOL,DM,HTN,CAD,PRIOR CMP,CKD,HB,TLC,PLATELETS,GLUCOSE,UREA,CREATININE,BNP,RAISED CARDIAC ENZYMES,EF,SEVERE ANAEMIA,ANAEMIA,STABLE ANGINA,ACS,STEMI,ATYPICAL CHEST PAIN,HEART FAILURE,HFREF,HFNEF,VALVULAR,CHB,SSS,AKI,CVA INFRACT,CVA BLEED,AF,VT,PSVT,CONGENITAL,UTI,NEURO CARDIOGENIC SYNCOPE,ORTHOSTATIC,INFECTIVE ENDOCARDITIS,DVT,CARDIOGENIC SHOCK,SHOCK,PULMONARY EMBOLISM,CHEST INFECTION\n" +
                "1,234735,4/1/2017,4/3/2017,81,M,R,E,Apr-17,3,2,DISCHARGE,0,0,1,0,0,0,0,9.5,16.1,337,80,34,0.9,1880,1,35,0,1,0,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
        MockMultipartFile sampleFile =  new MockMultipartFile("file", fileName, "csv", fileContent.getBytes());
        MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders.multipart("/parser/uploadCVS");
        mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());
    }

    @AfterEach
    public void cleanup(){
        pazienteDAO.deleteAll();
        Path finalPath = Path.of("FileUpload0/sampleFile.csv");
        try {
            Files.deleteIfExists(finalPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finalPath = Path.of("FileUpload0");
        try {
            Files.deleteIfExists(finalPath);
        } catch (IOException e2){
            throw new RuntimeException(e2);
        }
    }
}
