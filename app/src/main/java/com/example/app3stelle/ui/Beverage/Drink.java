package com.example.app3stelle.ui.Beverage;

import java.io.Serializable;

public class Drink implements Serializable {

    private String drinkName;
    private String ingredients;
    private String drinkPrice;
    private String caraffaPrice;
    private String drinkSize;

    public Drink( String drinkName, String drinkPrice,String ingredients,String caraffaPrice) {
        this.drinkName = drinkName;
        this.ingredients = ingredients;
        this.drinkPrice = drinkPrice;
        this.caraffaPrice = caraffaPrice;
        this.drinkSize = "Singolo";
    }

    public String getDrinkName() {
        return drinkName;
    }
    public String getIngredients() {
        return ingredients;
    }
    public String getDrinkPrice(){
        return drinkPrice;
    }
    public String getCaraffaPrice(){
        return caraffaPrice;
    }
    public String getDrinkSize(){
        return drinkSize;
    }
    public void setDrinkSize(String drinksize){this.drinkSize = drinksize;}
    public String getFinalPrice(){
        return drinkSize=="Singolo"? drinkPrice:caraffaPrice;
    }
}