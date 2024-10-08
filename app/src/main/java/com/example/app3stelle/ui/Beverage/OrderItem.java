package com.example.app3stelle.ui.Beverage;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private final Double prezzo;
    private final String descrizione;
    private final String clientName;
    private final String date;
    private int orderState;
    private final String userId;

    public OrderItem(Double price,String descr, String clientName,String userId, String date) {
        this.prezzo = price;
        this.descrizione = descr;
        this.clientName = clientName;
        this.userId = userId;
        this.date = date;
        orderState = 0;
    }
    public OrderItem(Double price,String descr, String clientName,int orderState) {
        this.prezzo = price;
        this.descrizione = descr;
        this.clientName = clientName;
        this.orderState = orderState;
        userId=null;
        date=null;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public String getDate() {
        return date;
    }
    public String getClientName() {
        return clientName;
    }
    public Double getPrezzo() {
        return prezzo;
    }
    public int getState() {
        return orderState;
    }
    public void setState(int orderState) {this.orderState=orderState;}
    public String getUserId(){
        return this.userId;
    }
}