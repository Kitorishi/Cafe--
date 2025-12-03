package com.example.proyectofinalmoviles;

import java.util.List;

public class CartItem {
    private Product product;
    private String selectedSize;
    private List<Extra> selectedExtras;
    private double finalPrice;

    // ====== CONSTRUCTOR ======
    public CartItem(Product product, String selectedSize, List<Extra> selectedExtras, double finalPrice) {
        this.product = product;
        this.selectedSize = selectedSize;
        this.selectedExtras = selectedExtras;
        this.finalPrice = finalPrice;
    }

    public Product getProduct() {return product;}
    public String getSelectedSize() {return selectedSize;}
    public List<Extra> getSelectedExtras() {return selectedExtras;}
    public double getFinalPrice() {return finalPrice;}

    public String getDetailString(){
        StringBuilder sb = new StringBuilder();
        if(product.hasSizes()){
            sb.append(selectedSize);
        } else {
            sb.append("Tamaño Unico");
        }
        if(!selectedExtras.isEmpty()){
            sb.append("\n+ Extras:");
            for(Extra e : selectedExtras){
                sb.append(e.getName()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Eliminar la última coma y espacio
        }
        return sb.toString();
    }
}
