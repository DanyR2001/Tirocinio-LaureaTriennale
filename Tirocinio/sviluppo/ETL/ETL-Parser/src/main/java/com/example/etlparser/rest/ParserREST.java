package com.example.etlparser.rest;

import com.example.etlparser.dto.ResultDTO;
import com.example.etlparser.entity.*;
import com.example.etlparser.service.in.ParserService;
import com.example.etlparser.utils.Utils;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.javapoet.ClassName;
import org.springframework.web.bind.annotation.*;
import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping(value="/parser")
public class ParserREST {

    @Autowired
    private ParserService service;

    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());

    @Value("${folderCount}")
    private Integer folderCount;

    @PostMapping(value = "/uploadCVS",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ResultDTO<Integer>> uploadCSV(@RequestPart("file")MultipartFile file) {
        ResultDTO<Integer> res=new ResultDTO<>();
        res.setFailureResponse("Errore generico ",500);
        boolean isFirstLine = true;
        ArrayList<String> defaultHeader = new ArrayList<>(List.of("SNO", "MRD No.", "D.O.A", "D.O.D", "AGE", "GENDER", "RURAL", "TYPE OF ADMISSION-EMERGENCY/OPD", "month year", "DURATION OF STAY", "duration of intensive unit stay", "OUTCOME", "SMOKING", "ALCOHOL", "DM", "HTN", "CAD", "PRIOR CMP", "CKD", "HB", "TLC", "PLATELETS", "GLUCOSE", "UREA", "CREATININE", "BNP", "RAISED CARDIAC ENZYMES", "EF", "SEVERE ANAEMIA", "ANAEMIA", "STABLE ANGINA", "ACS", "STEMI", "ATYPICAL CHEST PAIN", "HEART FAILURE", "HFREF", "HFNEF", "VALVULAR", "CHB", "SSS", "AKI", "CVA INFRACT", "CVA BLEED", "AF", "VT", "PSVT", "CONGENITAL", "UTI", "NEURO CARDIOGENIC SYNCOPE", "ORTHOSTATIC", "INFECTIVE ENDOCARDITIS", "DVT", "CARDIOGENIC SHOCK", "SHOCK", "PULMONARY EMBOLISM", "CHEST INFECTION"));
        if (folderCount==null)
            folderCount=0;
        Path uploadPath = Paths.get("FileUpload"+folderCount);
        folderCount++;
        while (Files.exists(uploadPath)) {
            folderCount++;
            uploadPath = Paths.get("FileUpload"+folderCount);
        }
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            LOGGER.info("Errore creazione nuova directory");
        }
        File csv=null;
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            csv=new File(file.getOriginalFilename());
            file.transferTo(csv);
        } catch (IOException ioe) {
            LOGGER.log(Level.INFO,"errore");
        }
        Integer cont=0;
        if (csv!=null&&Utils.isCSV(csv)){
            String[] nextLine;
            File temp=new File(uploadPath+"/"+csv.getPath());
            try (CSVReader reader = new CSVReader(new FileReader(temp))){
                while ((nextLine = reader.readNext())!= null){
                    if(isFirstLine){
                        isFirstLine = false;
                        ArrayList<String> header = new ArrayList<>(List.of(nextLine));
                        if(!Utils.isEqualHeaderList(header,defaultHeader)){
                            res.setFailureResponse("Colonne non corrispondenti ",500);
                            LOGGER.log(Level.INFO,defaultHeader+"\n"+header);
                            throw new Exception("Colonne non corrispondenti");
                        }
                    }else{
                        String e = "EMPTY";
                        HashMap<String, String> dict = new HashMap<>();
                        dict.put("hb", nextLine[19]);
                        dict.put("tlc", nextLine[20]);
                        dict.put("platelets", nextLine[21]);
                        dict.put("glucose", nextLine[22]);
                        dict.put("urea", nextLine[23]);
                        dict.put("creatinine", nextLine[24]);
                        dict.put("bnp", nextLine[25]);
                        dict.put("raised_cardiac_enzymes", nextLine[26]);
                        dict.put("ef", nextLine[27]);
                        int counter = 0;
                        for(String s:dict.values()){
                            //LOGGER.log(Level.INFO,"dizionario: "+s);
                            if(s == null || s.equals(e)||s.isEmpty()||s.isBlank())
                                counter++;
                        }
                        LOGGER.log(Level.INFO,"test colonne vuote:");
                        if(counter>2) {
                            //LOGGER.log(Level.INFO,"Tupla da scartimo ");
                            LOGGER.log(Level.INFO,"Tupla scartata, troppe colonne vuote nelle analisi");
                            continue;
                        }
                        LOGGER.log(Level.INFO,"test pass ");
                        Analisi analisi = new Analisi();
                        try {
                            analisi.setHaemoglobin(Double.valueOf(dict.get("hb")));
                        }catch (NumberFormatException e1){
                            analisi.setHaemoglobin(null);
                        }
                        try {
                            analisi.setTotalLeukocytesCount(Double.valueOf(dict.get("tlc")));
                        }catch (NumberFormatException e1){
                            analisi.setTotalLeukocytesCount(null);
                        }
                        try {
                            analisi.setPalatelets(Integer.valueOf(dict.get("platelets")));
                        }catch (NumberFormatException e1){
                            analisi.setPalatelets(null);
                        }
                        try {
                            analisi.setGlucose(Integer.valueOf(dict.get("glucose")));
                        }catch (NumberFormatException e1){
                            analisi.setGlucose(null);
                        }
                        try {
                            analisi.setUrea(Integer.valueOf(dict.get("urea")));
                        }catch (NumberFormatException e1){
                            analisi.setUrea(null);
                        }
                        try {
                            analisi.setCreatinine(Double.valueOf(dict.get("creatinine")));
                        }catch (NumberFormatException e1){
                            analisi.setCreatinine(null);
                        }
                        try {
                            analisi.setBTypeNatriureticPeptide(Integer.valueOf(dict.get("bnp")));
                        }catch (NumberFormatException e1){
                            analisi.setBTypeNatriureticPeptide(null);
                        }
                        try {
                            analisi.setRaisedCardiacEnzymes(Utils.boolanValue(dict.get("raised_cardiac_enzymes")));
                        }catch (NumberFormatException e1){
                            analisi.setRaisedCardiacEnzymes(null);
                        }
                        try {
                            analisi.setEjectionFraction(Integer.valueOf(dict.get("ef")));
                        }catch (NumberFormatException e1){
                            analisi.setEjectionFraction(null);
                        }
                        Long serialNumber=null;
                        Integer age=null;
                        try{
                            serialNumber=Long.valueOf(nextLine[1]);
                        }catch (NumberFormatException e3){
                            res.setFailureResponse("Errore lanciata eccezzione"+e3,500);
                            LOGGER.log(Level.INFO,"errore seriale number");
                        }
                        try{
                            age=Integer.valueOf(nextLine[4]);
                        }catch (NumberFormatException e3){
                            LOGGER.log(Level.INFO,"errore era ");
                        }
                        Paziente paziente=null;
                        if(serialNumber!=null)
                            paziente= new Paziente(serialNumber,age,
                                Gender.valueOf(nextLine[5]),Rural.valueOf(nextLine[6]),Utils.boolanValue(nextLine[12]),
                                Utils.boolanValue(nextLine[13]),Utils.boolanValue(nextLine[14]),Utils.boolanValue(nextLine[15]),
                                Utils.boolanValue(nextLine[16]),Utils.boolanValue(nextLine[17]),Utils.boolanValue(nextLine[18]),
                                new ArrayList<>());
                        if (paziente!=null) {
                            //LOGGER.log(Level.INFO,"id " + nextLine[0] + "id " + nextLine[1]);
                            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("M/d/yyyy");
                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/M/yyyy");
                            LocalDate inizio = null;
                            LocalDate INFO = null;
                            try {
                                inizio = LocalDate.parse(nextLine[2], formatter1);
                                INFO = LocalDate.parse(nextLine[3], formatter1);
                            } catch (DateTimeException e2) {
                                res.setFailureResponse("Errore lanciata eccezzione"+e2,500);
                                LOGGER.log(Level.INFO,"errore 1 date");
                            }
                            try {
                                inizio = LocalDate.parse(nextLine[2], formatter2);
                                INFO = LocalDate.parse(nextLine[3], formatter2);
                            } catch (DateTimeException e2) {
                                res.setFailureResponse("Errore lanciata eccezzione"+e2,500);
                                LOGGER.log(Level.INFO,"errore 1 date");
                            }
                            Ospedalizzazione ospedalizzazione = new Ospedalizzazione(Long.valueOf(nextLine[0]), paziente, analisi,
                                    inizio, INFO, TOA.valueOf(nextLine[7]),
                                    Integer.valueOf(nextLine[9]), Integer.valueOf(nextLine[10]), Outcame.valueOf(nextLine[11]),
                                    Utils.boolanValue(nextLine[28]), Utils.boolanValue(nextLine[29]), Utils.boolanValue(nextLine[30]),
                                    Utils.boolanValue(nextLine[31]), Utils.boolanValue(nextLine[32]), Utils.boolanValue(nextLine[33]),
                                    Utils.boolanValue(nextLine[34]), Utils.boolanValue(nextLine[35]), Utils.boolanValue(nextLine[36]),
                                    Utils.boolanValue(nextLine[37]), Utils.boolanValue(nextLine[38]), Utils.boolanValue(nextLine[39]),
                                    Utils.boolanValue(nextLine[40]), Utils.boolanValue(nextLine[41]), Utils.boolanValue(nextLine[42]),
                                    Utils.boolanValue(nextLine[43]), Utils.boolanValue(nextLine[44]), Utils.boolanValue(nextLine[45]),
                                    Utils.boolanValue(nextLine[46]), Utils.boolanValue(nextLine[47]), Utils.boolanValue(nextLine[48]),
                                    Utils.boolanValue(nextLine[49]), Utils.boolanValue(nextLine[50]), Utils.boolanValue(nextLine[51]),
                                    Utils.boolanValue(nextLine[52]), Utils.boolanValue(nextLine[53]), Utils.boolanValue(nextLine[54]),
                                    Utils.boolanValue(nextLine[55]));
                            paziente.getLista().add(ospedalizzazione);
                            LOGGER.log(Level.INFO,"SALVIAMO QUESTO: "+paziente);
                            service.uploadPatientHistory(paziente.toDTO());
                            cont++;
                        }
                        else {
                            LOGGER.log(Level.INFO,"Qualcosa è andato storto");
                        }
                    }
                }
                res.setSuccessTrueResponse("File csv normalizzato correttamente");
                res.setCode(HttpStatus.OK.value());
                res.setData(cont);
            } catch (Exception e) {
                res.setFailureResponse("Errore lanciata eccezzione"+e,500);
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(res.getCode()));
    }

}
