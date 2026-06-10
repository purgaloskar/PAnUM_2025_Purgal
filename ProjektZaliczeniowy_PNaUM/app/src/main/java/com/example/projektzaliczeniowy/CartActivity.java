package com.example.projektzaliczeniowy;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private String trescEmaila = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);
        TextView tv = findViewById(R.id.tv_podsumowanie);
        Button btnZamow = findViewById(R.id.btn_zamow);
        Button btnPowrot = findViewById(R.id.btn_powrot); // Nowy przycisk

        Cursor cursor = dbHelper.getKoszyk();
        StringBuilder sb = new StringBuilder();
        double suma = 0;
        if (cursor.moveToFirst()) {
            do {
                String n = cursor.getString(1); int i = cursor.getInt(2); double c = cursor.getDouble(3);
                suma += (i * c); sb.append(n).append(" x").append(i).append(" = ").append(i*c).append(" zł\n");
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (suma > 0) {
            sb.append("\nRazem: ").append(suma).append(" zł");
            trescEmaila = "Dzień dobry,\n\nChciałbym złożyć zamówienie w aplikacji Kafeteria.\n\nPodsumowanie:\n" + sb.toString() + "\n\nPozdrawiam,\nOskar Purgal";
            tv.setText(sb.toString());
        } else {
            tv.setText("Koszyk jest pusty.");
        }

        btnZamow.setOnClickListener(v -> {
            if (trescEmaila.isEmpty()) {
                Toast.makeText(CartActivity.this, "Koszyk jest pusty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Zamówienie z Kafeterii - Oskar Purgal");
            intent.putExtra(Intent.EXTRA_TEXT, trescEmaila);

            try {
                startActivity(Intent.createChooser(intent, "Wyślij zamówienie przez:"));
                dbHelper.wyczyscKoszyk();
            } catch (Exception e) {
                Toast.makeText(CartActivity.this, "Nie znaleziono aplikacji do wysyłki.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPowrot.setOnClickListener(v -> {
            finish();
        });
    }
}