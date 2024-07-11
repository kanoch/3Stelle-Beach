package com.example.app3stelle.ui.Beverage;

import java.io.Serializable;

public class Drink  extends Beverage implements Serializable {

    private final String ingredients;
    private final String caraffaPrice;

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
        return super.getBeverageSize().endsWith("Singolo")? super.getBeveragePrice():caraffaPrice;
    }
}