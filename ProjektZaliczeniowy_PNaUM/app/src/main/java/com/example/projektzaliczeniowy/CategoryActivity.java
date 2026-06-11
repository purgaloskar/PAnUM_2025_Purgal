package com.example.projektzaliczeniowy;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CategoryActivity extends AppCompatActivity {

    private Button btnDrinks, btnSnacks, btnDinners, btnSubmitOrder;
    private ListView lvCart;
    private TextView tvTotalSummary;
    private DatabaseHelper dbHelper;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        View mainView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnDrinks = findViewById(R.id.btnDrinks);
        btnSnacks = findViewById(R.id.btnSnacks);
        btnDinners = findViewById(R.id.btnDinners);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);
        lvCart = findViewById(R.id.lvCart);
        tvTotalSummary = findViewById(R.id.tvTotalSummary);

        dbHelper = new DatabaseHelper(this);

        btnDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity("drinks");
            }
        });

        btnSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity("snacks");
            }
        });

        btnDinners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity("dinners");
            }
        });

        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getCartItems();
                if (cursor != null && cursor.getCount() > 0) {

                    StringBuilder emailBody = new StringBuilder();
                    emailBody.append("Podsumowanie zamówienia z Kafeterii:\n\n");

                    int nameIndex = cursor.getColumnIndex("name");
                    int qtyIndex = cursor.getColumnIndex("quantity");
                    int priceIndex = cursor.getColumnIndex("price");

                    double totalSum = 0.0;

                    while (cursor.moveToNext()) {
                        if (nameIndex != -1 && qtyIndex != -1 && priceIndex != -1) {
                            String name = cursor.getString(nameIndex);
                            int qty = cursor.getInt(qtyIndex);
                            double price = cursor.getDouble(priceIndex);
                            totalSum += price;

                            emailBody.append("- ").append(name)
                                    .append(" x").append(qty)
                                    .append(" (").append(String.format("%.2f", price)).append(" zł)\n");
                        }
                    }

                    emailBody.append("\nRazem do zapłaty: ").append(String.format("%.2f", totalSum)).append(" zł\n");
                    emailBody.append("Dziękujemy za zamówienie!");

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zamowienia@kafeteria.pl"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Nowe zamówienie - CoffeeTime");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody.toString());

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                        dbHelper.clearCart();
                        loadCartData();
                        Toast.makeText(CategoryActivity.this, "Zamówienie zostało przesłane do aplikacji pocztowej!", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.clearCart();
                        loadCartData();
                        Toast.makeText(CategoryActivity.this, "Złożono zamówienie (brak aplikacji pocztowej, symulacja wysyłki).", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(CategoryActivity.this, "Twój koszyk jest pusty!", Toast.LENGTH_SHORT).show();
                }
                if (cursor != null) cursor.close();
            }
        });
    }

    private void openMenuActivity(String category) {
        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        intent.putExtra("CATEGORY_KEY", category);
        startActivity(intent);
    }

    public void loadCartData() {
        Cursor cursor = dbHelper.getCartItems();
        double totalSum = 0.0;

        if (cursor != null) {
            int priceIndex = cursor.getColumnIndex("price");
            while (cursor.moveToNext()) {
                if (priceIndex != -1) {
                    totalSum += cursor.getDouble(priceIndex);
                }
            }
            cursor.moveToFirst();
        }

        tvTotalSummary.setText("Razem do zapłaty: " + String.format("%.2f", totalSum) + " zł");

        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(this, cursor, dbHelper);
            lvCart.setAdapter(cartAdapter);
        } else {
            cartAdapter.changeCursor(cursor);
            cartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartData();
    }
}