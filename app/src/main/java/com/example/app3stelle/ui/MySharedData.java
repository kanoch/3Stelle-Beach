package com.example.app3stelle.ui;

import com.example.app3stelle.ui.Beverage.Beverage;
import com.example.app3stelle.ui.Beverage.Drink;

import java.util.ArrayList;
import java.util.HashMap;

public class MySharedData {
    private static MySharedData instance;
    private HashMap<String,Double> sharedUmbrellaCartList = new HashMap<>();
    private ArrayList<Beverage> sharedOrderDrinkList = new ArrayList<>();
    private String userId;
    private String userMail;
    private boolean findId = false;

    private MySharedData() {
        // Costruttore privato per impedire l'istanziazione diretta
    }

    public static MySharedData getInstance() {
        if (instance == null) {
            instance = new MySharedData();
        }
        return instance;
    }

    public HashMap<String,Double> getSharedUmbrellaCartList(){
        return this.sharedUmbrellaCartList;
    }
    public ArrayList<Beverage> getSharedOrderDrinkList(){
        return this.sharedOrderDrinkList;
    }
    public double getTotalUmbrellaCart(){
       return sharedUmbrellaCartList.values().stream().mapToDouble(Double::doubleValue).sum();

    }
    public double getTotalCartDrink(){
        return sharedOrderDrinkList.stream()
                .mapToDouble(drink -> Double.parseDouble(drink.getFinalPrice()))
                .sum();

    }
    public String getFullOrderDescription(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sharedOrderDrinkList.size(); i++) {
            stringBuilder.append(sharedOrderDrinkList.get(i).getBeverageSize());
            stringBuilder.append(" ");
            stringBuilder.append(sharedOrderDrinkList.get(i).getBeverageName());
            if (i < sharedOrderDrinkList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
    public String getUserId(){return this.userId;}
    public void setUserId(String id){this.userId = id;}
    public String getUserMail(){return this.userMail;}
    public void setUserMail(String mail){this.userMail = mail;}
    public Boolean getFindId(){return this.findId;}
    public void setFindId(boolean answer){this.findId = answer;}

}
