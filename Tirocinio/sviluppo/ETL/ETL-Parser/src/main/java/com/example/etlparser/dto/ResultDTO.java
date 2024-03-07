package com.example.etlparser.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(Include.NON_ABSENT)
public class ResultDTO<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8903038957866003289L;

    protected boolean success;

    protected int code;

    protected String descrizione;

    protected T data;

    private LocalDateTime dateTime;

    /**
     * Costruttore
     */
    public ResultDTO(){
        clear();
    }

    /**
     * Metodo utile a resettare i campi dell'oggetto ResultDTO
     */
    public void clear(){
        this.success = false;
        this.code = 0;
        this.data = null;
        this.descrizione = null;
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Metodo per ottenere l'esito dell'operazione
     * @return true nel caso in cui l'operazione e' andata a buon fine, false altrimenti
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Metodo utile a valorizzare i campi dell'oggetto ResultDTO nel caso in cui
     * l'operazione va a buon fine ma non si è ottenuto cip' che si voleva.
     * Ad esempio quando si vuole recuperare uno o più record e questi non vengono trovati.
     * @param message stringa contenente il messaggio da visualizzare
     */
    public void setSuccessFalseResponse(String message){
        this.descrizione = message;
        this.dateTime = LocalDateTime.now();
        this.success = Boolean.FALSE;
        this.code = HttpStatus.SC_OK;
    }

    /**
     * Metodo utile a valorizzare i campi dell'oggetto ResultDTO nel caso in cui
     * l'operazione va a buon fine.
     * @param message stringa contenente il messaggio da visualizzare
     */
    public void setSuccessTrueResponse(String message){
        this.descrizione = message;
        this.dateTime = LocalDateTime.now();
        this.success = Boolean.TRUE;
        this.code = HttpStatus.SC_OK;
    }

    /**
     * Metodo utile a valorizzare i campi dell'oggetto ResultDTO nel caso in cui
     * l'operazione non vada a buon fine
     * @param message stringa contenente il messaggio da visualizzare
     * @param code intero contenente il codice HTTP
     */
    public void setFailureResponse(String message, int code){
        this.descrizione = message;
        this.dateTime = LocalDateTime.now();
        this.success = Boolean.FALSE;
        this.code = code;
        this.data = null;
    }
}
