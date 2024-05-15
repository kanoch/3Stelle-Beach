package com.example.app3stelle.ui;

import java.io.Serializable;

public class Prenotazione implements Serializable {

    private String numeroLettino;
    private String dataPrenotazione;
    private String periodReservation;
    private String prezzo;
    private String userMail;

    public Prenotazione( String numeroLettino, String prezzo,String dataPrenotazione,String period,String mail) {
        this.numeroLettino = numeroLettino;
        this.dataPrenotazione = dataPrenotazione;
        this.prezzo = prezzo;
        this.periodReservation = period;
        this.userMail = mail;
    }

    public String getNumeroLettino() {
        return numeroLettino;
    }

    public String getDataPrenotazione() {
        return dataPrenotazione;
    }
    public String getPeriod() {
        return periodReservation;
    }
    public Double getPrezzo(){
    return Double.valueOf(prezzo);
    }
    public String getMail(){
        return this.userMail;
    }
}
