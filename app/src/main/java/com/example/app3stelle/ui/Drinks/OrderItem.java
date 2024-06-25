package com.example.app3stelle.ui.Drinks;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private final Double prezzo;
    private final String descrizione;
    private final String clientName;
    private String userId;

    public OrderItem(Double price,String descr, String clientName,String userId) {
        this.prezzo = price;
        this.descrizione = descr;
        this.clientName = clientName;
        this.userId = userId;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public String getClientName() {
        return clientName;
    }
    public Double getPrezzo() {
        return prezzo;
    }
    public String getUserId(){
        return this.userId;
    }
}