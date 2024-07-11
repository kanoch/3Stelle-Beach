package com.example.app3stelle.ui.Beverage;

import java.io.Serializable;

public class Drink  extends Beverage implements Serializable {

    private String ingredients;
    private String caraffaPrice;

    public Drink( String drinkName, String drinkPrice,String ingredients,String caraffaPrice) {
        super(drinkName, drinkPrice, "Singolo");
        this.ingredients = ingredients;
        this.caraffaPrice = caraffaPrice;
    }

    public String getIngredients() {
        return ingredients;
    }
    public String getCaraffaPrice(){
        return caraffaPrice;
    }
    @Override
    public String getFinalPrice(){
        return super.getBeverageSize()=="Singolo"? super.getBeveragePrice():caraffaPrice;
    }
}