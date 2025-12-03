package com.example.proyectofinalmoviles;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(CartItem item) {
        cartItems.add(item);
    }
    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    public List<CartItem> getItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotalOrderPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getFinalPrice();
        }
        return total;
    }
}
