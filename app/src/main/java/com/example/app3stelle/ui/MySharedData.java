package com.example.app3stelle.ui;

import com.example.app3stelle.ui.Drinks.Drink;

import java.util.ArrayList;
import java.util.HashMap;

public class MySharedData {
    private static MySharedData instance;
    private HashMap<String,Double> sharedUmbrellaCartList = new HashMap<>();
    private ArrayList<Drink> sharedDrinkMap = new ArrayList<>();
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
    public ArrayList<Drink> getSharedDrinkMap(){
        return this.sharedDrinkMap;
    }
    public double getTotalUmbrellaCart(){
       return sharedUmbrellaCartList.values().stream().mapToDouble(Double::doubleValue).sum();

    }
    public double getTotalCartDrink(){
        return sharedDrinkMap.stream()
                .mapToDouble(drink -> Double.parseDouble(drink.getFinalPrice()))
                .sum();

    }
    public String getFullOrderDescription(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sharedDrinkMap.size(); i++) {
            stringBuilder.append(sharedDrinkMap.get(i).getDrinkName());
            if (i < sharedDrinkMap.size() - 1) {
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
