package com.example.app3stelle.ui.Drinks;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private final Double prezzo;
    private final String descrizione;
    private final String destinazione;
    private String userMail;

    public OrderItem(Double price,String descr, String destination,String mail) {
        this.prezzo = price;
        this.descrizione = descr;
        this.destinazione = destination;
        this.userMail = mail;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public String getDestinazione() {
        return destinazione;
    }
    public Double getPrezzo() {
        return prezzo;
    }
    public String getMail(){
        return this.userMail;
    }
}