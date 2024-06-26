package com.example.app3stelle.ui.history;

import java.io.Serializable;

public class RowElement implements Serializable {

    private String drinkDestination;
    private String drinkList;
    private String drinkPrice;
    private int orderState;

    public RowElement( String drinkDestination, String drinkPrice,String drinkList, int orderState) {
        this.drinkDestination = drinkDestination;
        this.drinkList = drinkList;
        this.drinkPrice = drinkPrice;
        this.orderState = orderState;
    }

    public String getDrinkList() {
        return drinkList;
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
    public void setOrderState(int newState){orderState = newState;}
}
