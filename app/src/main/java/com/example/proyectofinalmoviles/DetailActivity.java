package com.example.proyectofinalmoviles;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Product product;

    // Elementos UI
    private TextView tvTitle, tvDesc, tvBasePrice, tvExtrasTitle, tvSizeTitle;
    private View imgHeader;
    private RadioGroup rgSize;
    private MaterialButton btnAddToCart;
    private LinearLayout extrasContainer;
    private double currentFinalPrice = 0.0;

    // Lista para agregar extras
    private List<CheckBox> dynamicCheckBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Aqui es donde se recibe el objeto Producto enviado desde el Menu
        product = (Product) getIntent().getSerializableExtra("PRODUCTO_EXTRA");

        //Se vinculan las vistas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        imgHeader = findViewById(R.id.detailImage);
        tvTitle = findViewById(R.id.detailTitle);
        tvDesc = findViewById(R.id.detailDesc);
        tvBasePrice = findViewById(R.id.detailBasePrice);
        tvExtrasTitle = findViewById(R.id.tvExtrasTitle);
        tvSizeTitle = findViewById(R.id.tvSizeTitle);

        rgSize = findViewById(R.id.radioGroupSize);
        extrasContainer = findViewById(R.id.extrasContainer);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        if(product != null){
            tvTitle.setText(product.getName());
            tvDesc.setText(product.getDescription());
            tvBasePrice.setText(product.getFormattedPrice());
            imgHeader.setBackgroundResource(product.getImageResId());

            if (product.hasSizes()) {
                tvSizeTitle.setVisibility(View.VISIBLE);
                rgSize.setVisibility(View.VISIBLE);
            } else {
                tvSizeTitle.setVisibility(View.GONE);
                rgSize.setVisibility(View.GONE);
            }

            // ======= AQUI GENERAMOS LOS EXTRAS =======
            generateExtras();
        }

        rgSize.setOnCheckedChangeListener((group, checkedId) -> calculateTotal());
        calculateTotal();

        btnAddToCart.setOnClickListener(v -> {
            addToCart();
        });
    }

    private void generateExtras() {
        extrasContainer.removeAllViews();
        dynamicCheckBoxes.clear();

        List<Extra> extras = product.getExtras();

        if (extras == null || extras.isEmpty()) {
            tvExtrasTitle.setVisibility(View.GONE);
            extrasContainer.setVisibility(View.GONE);
            return;
        }

        tvExtrasTitle.setVisibility(View.VISIBLE);
        extrasContainer.setVisibility(View.VISIBLE);

        // Por cada extra en la lista del producto, creamos un CheckBox
        for (Extra extra : extras) {
            CheckBox cb = new CheckBox(this);
            cb.setText(extra.getDisplayText()); // Ej: "Leche (+$5.00)"
            cb.setTag(extra); // Guardamos el objeto Extra dentro del checkbox para leer su precio luego
            cb.setTextSize(16);
            cb.setPadding(0, 12, 0, 12);

            // Listener para extras
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());

            extrasContainer.addView(cb);
            dynamicCheckBoxes.add(cb);
        }
    }

    private void calculateTotal() {
        if (product == null) return;

        double total = product.getPrice();

        // Sumar tamaño
        int selectedSizeId = rgSize.getCheckedRadioButtonId();
        if (selectedSizeId == R.id.rbMedium) {
            total += 10.00;
        } else if (selectedSizeId == R.id.rbLarge) {
            total += 15.00;
        }

        // Sumar Extras (Recorremos los checkboxes dinámicos)
        for (CheckBox cb : dynamicCheckBoxes) {
            if (cb.isChecked()) {
                // Recuperamos el objeto Extra que guardamos en el Tag
                Extra extra = (Extra) cb.getTag();
                total += extra.getPrice();
            }
        }
        currentFinalPrice = total;
        btnAddToCart.setText(String.format("Agregar - $%.2f", total));
    }

    private void addToCart() {
        String sizeName = "Estándar";
        if (product.hasSizes()) {
            int selectedId = rgSize.getCheckedRadioButtonId();
            RadioButton rb = findViewById(selectedId);
            if (rb != null) sizeName = rb.getText().toString();
        }

        List<Extra> selectedExtras = new ArrayList<>();
        for (CheckBox cb : dynamicCheckBoxes) {
            if (cb.isChecked()) {
                selectedExtras.add((Extra) cb.getTag());
            }
        }

        CartItem item = new CartItem(product, sizeName, selectedExtras, currentFinalPrice);

        CartManager.getInstance().addItem(item);

        Toast.makeText(this, "Agregado al pedido", Toast.LENGTH_SHORT).show();
        finish();
    }
}