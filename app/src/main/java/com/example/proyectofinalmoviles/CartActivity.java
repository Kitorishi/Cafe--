package com.example.proyectofinalmoviles;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;

public class CartActivity extends AppCompatActivity {
    private LinearLayout cartItemsContainer;
    private TextView tvTotalOrder;
    private MaterialButton btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        cartItemsContainer = findViewById(R.id.cartItemsContainer);
        tvTotalOrder = findViewById(R.id.tvTotalOrder);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        loadCartItems();

        btnPlaceOrder.setOnClickListener(v -> {
            if(CartManager.getInstance().getItems().isEmpty()) {
                Toast.makeText(this, "Tu carrito esta vacio", Toast.LENGTH_SHORT).show();
            }else {
                showSuccessAnimation();
            }
        });
    }

    private void showSuccessAnimation() {
        // Crear el Dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_success);

        // Hacemos el fondo transparente para que se vean las esquinas redondeadas del CardView
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCancelable(false); // Evitar que lo cierren tocando afuera
        dialog.show();

        // Limpiar el carrito
        CartManager.getInstance().clearCart();

        // Temporizador: Esperar 2 segundos y volver al inicio
        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            goToHome();
        }, 2500);
    }

    private void goToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Cerramos esta actividad actual
    }

    private void loadCartItems() {
        cartItemsContainer.removeAllViews();

        for (CartItem item : CartManager.getInstance().getItems()) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_cart, cartItemsContainer, false);
            View img = itemView.findViewById(R.id.cartItemImage);
            TextView name = itemView.findViewById(R.id.cartItemName);
            TextView details = itemView.findViewById(R.id.cartItemDetails);
            TextView price = itemView.findViewById(R.id.cartItemPrice);
            View btnRemove = itemView.findViewById(R.id.btnRemoveItem);

            img.setBackgroundResource(item.getProduct().getImageResId());
            name.setText(item.getProduct().getName());
            details.setText(item.getDetailString());

            price.setText(String.format("$%.2f", item.getFinalPrice()));

            btnRemove.setOnClickListener(v -> {
               CartManager.getInstance().removeItem(item);
               Toast.makeText(CartActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
               loadCartItems();
            });

            cartItemsContainer.addView(itemView);
        }
        double total = CartManager.getInstance().getTotalOrderPrice();
        tvTotalOrder.setText(String.format("$%.2f", total));

        //Lo deshabilitamos si esta vacio el carrito
        btnPlaceOrder.setEnabled(total > 0);
        if (total == 0) btnPlaceOrder.setText("Carrito Vacío");
        else btnPlaceOrder.setText("Enviar Pedido");
    }
}