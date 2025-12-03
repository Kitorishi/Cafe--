package com.example.proyectofinalmoviles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //Referencia al contenedor, buscador y carrito
    private LinearLayout productsContainer;
    private EditText etSearch;
    private ExtendedFloatingActionButton fabCart;

    //Referencia a las tarjetas de categorias
    private MaterialCardView cardBebidas, cardPostres, cardEspecialidades;
    private TextView tvBebidas, tvPostres, tvEspecialidades;

    // ===== LISTA DE DATOS =====
    private List<Product> fullMenu = new ArrayList<>();
    private String currentCategory = "Bebidas"; // Va a ser la Categoria seleccionada por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------ Referencia al contenedor y buscador -----
        productsContainer = findViewById(R.id.productsContainer);
        etSearch = findViewById(R.id.etSearch);

        cardBebidas = findViewById(R.id.cardCatBebidas);
        cardPostres = findViewById(R.id.cardCatPostres);
        cardEspecialidades = findViewById(R.id.cardCatEspecialidades);

        tvBebidas = findViewById(R.id.tvCatBebidas);
        tvPostres = findViewById(R.id.tvCatPostres);
        tvEspecialidades = findViewById(R.id.tvCatEspecialidades);

        fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            // Abrir la pantalla del Carrito
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        //Cargamos los datos
        loadData();

        //Configuraremos Buscador y Categorias
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        View.OnClickListener clickBebidas = v -> changeCategory("Bebidas");
        cardBebidas.setOnClickListener(clickBebidas);
        tvBebidas.setOnClickListener(clickBebidas);

        View.OnClickListener clickPostres = v -> changeCategory("Postres");
        cardPostres.setOnClickListener(clickPostres);
        tvPostres.setOnClickListener(clickPostres);

        View.OnClickListener clickEspecialidades = v -> changeCategory("Especialidades");
        cardEspecialidades.setOnClickListener(clickEspecialidades);
        tvEspecialidades.setOnClickListener(clickEspecialidades);

        filterList("");
        updateCategoryVisuals("Bebidas");
    }

    private void loadData() {

        List<Extra> extrasCafe = new ArrayList<>();
        extrasCafe.add(new Extra("Shot Extra", 12.00));
        extrasCafe.add(new Extra("Leche Almendra", 8.00));
        extrasCafe.add(new Extra("Jarabe Vainilla", 5.00));

        List<Extra> extrasPostre = new ArrayList<>();
        extrasPostre.add(new Extra("Bola de Helado", 15.00));
        extrasPostre.add(new Extra("Chocolate Líquido", 5.00));
        extrasPostre.add(new Extra("Fruta Extra", 10.00));

        List<Extra> extrasVacio = new ArrayList<>();

        // ===== BEBIDAS =====
        Product p1 = new Product("Cappuccino Arábica", "Con espuma cremosa", 45.00, "Bebidas", R.drawable.capuccino, true);
        p1.setExtras(extrasCafe); // Asignamos extras de café
        fullMenu.add(p1);

        Product p2 = new Product("Latte Vainilla", "Suave y aromático", 50.00, "Bebidas", R.drawable.latte, true);
        p2.setExtras(extrasCafe);
        fullMenu.add(p2);

        Product p3 = new Product("Espresso Doble", "Intenso y cargado", 35.00, "Bebidas", R.drawable.espresso, true);
        List<Extra> extrasEspresso = new ArrayList<>();
        extrasEspresso.add(new Extra("Azúcar Mascabado", 2.00));
        p3.setExtras(extrasEspresso);
        fullMenu.add(p3);

        // ===== POSTRES =====
        Product p4 = new Product("Cheesecake Frutos", "Dulce y suave", 60.00, "Postres", R.drawable.cheesecake, false);
        p4.setExtras(extrasPostre); // Asignamos extras de postre
        fullMenu.add(p4);

        Product p5 = new Product("Tiramisú Clásico", "Toque de café y cacao", 65.00, "Postres", R.drawable.tiramisu, false);
        p5.setExtras(extrasPostre);
        fullMenu.add(p5);

        // ===== ESPECIALIDADES =====
        Product p6 = new Product("Prensa Francesa", "Método artesanal (500ml)", 75.00, "Especialidades", R.drawable.prensa_francesa, false);
        p6.setExtras(extrasVacio); // Sin extras
        fullMenu.add(p6);

    }

    private void changeCategory(String category) {
        if (this.currentCategory.equals(category)) return;
        this.currentCategory = category;
        etSearch.setText("");

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            etSearch.clearFocus();
        }

        updateCategoryVisuals(category);
        filterList("");
    }

    private void updateCategoryVisuals(String category){
        int colorPrimary = getThemeColor(com.google.android.material.R.attr.colorOnPrimary);
        int colorOnPrimary = getThemeColor(com.google.android.material.R.attr.colorOnSurface);
        int colorSurfaceHigh = getThemeColor(com.google.android.material.R.attr.colorSurfaceContainerHighest);
        int colorOnSurfaceVar = getThemeColor(com.google.android.material.R.attr.colorOnSurfaceVariant);

        updateCardStyle(cardBebidas, tvBebidas, colorSurfaceHigh, colorOnSurfaceVar, false);
        updateCardStyle(cardPostres, tvPostres, colorSurfaceHigh, colorOnSurfaceVar, false);
        updateCardStyle(cardEspecialidades, tvEspecialidades, colorSurfaceHigh, colorOnSurfaceVar, false);

        if (category.equals("Bebidas")) updateCardStyle(cardBebidas, tvBebidas, colorPrimary, colorOnPrimary, true);
        else if (category.equals("Postres")) updateCardStyle(cardPostres, tvPostres, colorPrimary, colorOnPrimary, true);
        else if (category.equals("Especialidades")) updateCardStyle(cardEspecialidades, tvEspecialidades, colorPrimary, colorOnPrimary, true);

    }

    private void updateCardStyle(MaterialCardView card, TextView text, int bgColor, int textColor, boolean isBold) {
        card.setCardBackgroundColor(bgColor);
        text.setTextColor(textColor);
        text.setTypeface(null, isBold ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }

    private void filterList(String text) {
        productsContainer.removeAllViews();

        for (Product item : fullMenu) {
            boolean categoryMatch = item.getCategory().equals(currentCategory);
            boolean textMatch = item.getName().toLowerCase().contains(text.toLowerCase());

            if (categoryMatch && textMatch) {
                addProductToView(item);
            }
        }
    }

    private void addProductToView(Product product) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_product, productsContainer, false);

        TextView tvName = cardView.findViewById(R.id.tvProductName);
        TextView tvDesc = cardView.findViewById(R.id.tvProductDesc);
        TextView tvPrice = cardView.findViewById(R.id.tvProductPrice);
        ImageView imgProduct = cardView.findViewById(R.id.imgProduct);
        View btnAdd = cardView.findViewById(R.id.btnAdd);

        tvName.setText(product.getName());
        tvDesc.setText(product.getDescription());
        tvPrice.setText(product.getFormattedPrice());
        imgProduct.setImageResource(product.getImageResId());

        btnAdd.setOnClickListener(v -> openDetailActivity(product));
        cardView.setOnClickListener(v -> openDetailActivity(product));

        productsContainer.addView(cardView);
    }

    private void openDetailActivity(Product product){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("PRODUCTO_EXTRA", product);
        startActivity(intent);
    }

    private int getThemeColor(int attr) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

}