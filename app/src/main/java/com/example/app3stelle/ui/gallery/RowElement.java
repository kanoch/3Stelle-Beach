package com.example.app3stelle.ui.gallery;

import java.io.Serializable;

public class RowElement implements Serializable {

    private String drinkDestination;
    private String drinkList;
    private String drinkPrice;

    public RowElement( String drinkDestination, String drinkPrice,String drinkList) {
        this.drinkDestination = drinkDestination;
        this.drinkList = drinkList;
        this.drinkPrice = drinkPrice;
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
}
