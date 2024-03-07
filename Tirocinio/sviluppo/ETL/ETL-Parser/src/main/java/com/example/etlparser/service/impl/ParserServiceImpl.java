package com.example.etlparser.service.impl;

import com.example.etlparser.dao.in.AnalisiDAO;
import com.example.etlparser.dao.in.OspedalizzazioneDAO;
import com.example.etlparser.dao.in.PazienteDAO;
import com.example.etlparser.dto.PazienteDTO;
import com.example.etlparser.entity.Ospedalizzazione;
import com.example.etlparser.entity.Paziente;
import com.example.etlparser.service.in.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    private PazienteDAO pDAO;
    @Autowired
    private OspedalizzazioneDAO oDAO;
    @Autowired
    private AnalisiDAO aDAO;

    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName() );

    @Override
    public void uploadPatientHistory(PazienteDTO petient) {
        //LOGGER.log(Level.INFO,"up-1 "+petient);
        Paziente paziente=petient.toEntity();
        if (paziente != null){
            //LOGGER.log(Level.INFO,"up-2 "+paziente.getSerialNumber());
            if (paziente.getSerialNumber()!= null&&paziente.getSerialNumber()>0){
                //LOGGER.log(Level.INFO,"up-3 "+paziente.getSerialNumber());
                Optional<Paziente> p=pDAO.findById(paziente.getSerialNumber());
                if (p.isPresent()){
                    //LOGGER.log(Level.INFO,"up-4 "+paziente.getSerialNumber());
                    Paziente paziente1=p.get();
                    if (!paziente1.getLista().equals(paziente.getLista())){
                        //LOGGER.log(Level.INFO,"up-5 "+paziente.getSerialNumber());
                        for (Ospedalizzazione o: paziente.getLista()){
                            //LOGGER.log(Level.INFO,"up-6 "+paziente.getSerialNumber()+" osp "+o.getAdmissionNumber());
                            if (!paziente1.getLista().contains(o)){
                                LOGGER.log(Level.INFO,"Paziente presente, aggioraniqmolo "+ o.getAdmissionNumber() +" - "+o.getPaziente().getSerialNumber());
                                aDAO.save(o.getAnalisi());
                                oDAO.save(o);
                                paziente1.getLista().add(o);
                            }
                            else{
                                LOGGER.log(Level.INFO,"Paziente presente, NON aggioranti i le ops");
                            }
                        }
                        pDAO.save(paziente1);
                        LOGGER.log(Level.INFO,"Paziente presente, aggioranti i dati");
                    }
                    else
                        LOGGER.log(Level.INFO,"Paziente presente, liste uguali niente da aggiugnere");
                }else{
                    //LOGGER.log(Level.INFO,"up-7 "+paziente.getSerialNumber());
                    if (paziente.getLista()!=null){
                        //LOGGER.log(Level.INFO,"up-8 "+paziente.getSerialNumber());
                        if (!paziente.getLista().isEmpty()){
                            //LOGGER.log(Level.INFO,"up-9 "+paziente.getSerialNumber());
                            boolean flag=true;
                            for (int i=0; i<paziente.getLista().size()&&flag;i++){
                                //LOGGER.log(Level.INFO,"up-10 "+paziente.getSerialNumber());
                                if(paziente.getLista().get(i)==null){
                                    flag=false;
                                    //LOGGER.log(Level.INFO,"5.1");
                                }
                                else{
                                    //LOGGER.log(Level.INFO,"up-11 "+paziente.getSerialNumber());
                                    if(paziente.getLista().get(i).getAnalisi()== null){
                                        //LOGGER.log(Level.INFO,"5.2");
                                        flag = false;
                                    }
                                }
                            }
                            if (flag){
                                //LOGGER.log(Level.INFO,"up-11.1");
                                List<Ospedalizzazione> l=paziente.getLista();
                                paziente.setLista(new ArrayList<>());
                                pDAO.save(paziente);
                                for (Ospedalizzazione o:l){
                                    aDAO.save(o.getAnalisi());
                                    oDAO.save(o);
                                }
                                LOGGER.log(Level.INFO,"paziente nuovo, salvato");
                            }else LOGGER.log(Level.INFO,"5");
                        }else LOGGER.log(Level.INFO,"4");
                    }else LOGGER.log(Level.INFO,"3");
                }
            }else LOGGER.log(Level.INFO,"2");
        }
        else LOGGER.log(Level.INFO,"1");
    }
    
}
