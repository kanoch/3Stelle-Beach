package com.example.app3stelle.ui.Menu;

public class Piatto {
    private String nome;
    private String price;

    public Piatto(String nome,String price) {
        this.nome = nome;
        this.price= price;
    }

    public String getNome() {
        return nome;
    }
    public String getPrice(){
        return price;
    }
}
