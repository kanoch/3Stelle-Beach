package com.example.app3stelle.ui.history;

import java.io.Serializable;

public class RowElement implements Serializable {

    private String drinkDestination;
    private String drinkList;
    private String drinkPrice;
    private String drinkDate;
    private int orderState;

    public RowElement( String drinkDestination, String drinkPrice,String drinkList, int orderState, String drinkDate) {
        this.drinkDestination = drinkDestination;
        this.drinkList = drinkList;
        this.drinkPrice = drinkPrice;
        this.orderState = orderState;
        this.drinkDate = drinkDate;
    }

    public String getDrinkList() {
        return drinkList;
    }
    public String getDrinkDate() {
        return drinkDate;
    }
    public String getDestination() {
        return drinkDestination;
    }

    public String getDrinkPrice(){
        return drinkPrice;
    }
    public int getOrderState(){
        return orderState;
    }
}
