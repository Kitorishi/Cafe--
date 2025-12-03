package com.example.proyectofinalmoviles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String name;
    private String description;
    private double price;
    private String category;
    private int imageResId;
    private boolean hasSize;
    //Lista de extras disponibles para ESTE producto
    private List<Extra> availableExtras;

    // ======== CONSTRUCTOR ========
    public Product(String name, String description, double price, String category, int colorResId, boolean hasSize){
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageResId = colorResId;
        this.hasSize = hasSize;
        this.availableExtras = new ArrayList<>();  // Inicializamos la lista de extras
    }

    public void setExtras(List<Extra> extras) {
        this.availableExtras = extras;
    }
    public List<Extra> getExtras() {
        return availableExtras;
    }

    public String getName() {return name;}
    public String getDescription() {return description;}
    public double getPrice() {return price;}
    public String getFormattedPrice() { return String.format("$%.2f", price); }
    public String getCategory() {return category;}
    public int getImageResId() {return imageResId;}
    public boolean hasSizes() { return hasSize; }
}
