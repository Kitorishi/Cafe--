package com.example.proyectofinalmoviles;

import java.io.Serializable;

public class Extra implements Serializable {
    private String name;
    private double price;

    public Extra(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public String getName() {return name;}
    public double getPrice() {return price;}

    public String getDisplayText() {
        return String.format("%s (+$%.2f)", name, price);
    }
}
