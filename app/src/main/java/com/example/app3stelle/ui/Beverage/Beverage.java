package com.example.app3stelle.ui.Beverage;

public class Beverage {

    private final String beverageName;
    private String beverageSize;
    private final String beveragePrice;

    public Beverage( String beverageName, String beveragePrice, String beverageSize) {
        this.beverageName = beverageName;
        this.beveragePrice = beveragePrice;
        this.beverageSize = beverageSize;
    }

    public String getBeverageName() {
        return beverageName;
    }
    public String getBeverageSize() {
        return beverageSize;
    }
    public String getBeveragePrice(){
        return beveragePrice;
    }
    public void setBeverageSize(String beverageSize){this.beverageSize = beverageSize;}
    public String getFinalPrice(){
        return beveragePrice;
    }
}

