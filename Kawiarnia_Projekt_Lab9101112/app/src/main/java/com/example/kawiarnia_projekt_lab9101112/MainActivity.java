package com.example.kawiarnia_projekt_lab9101112;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Kawiarnia Aroma");

        Button btnNapoje = findViewById(R.id.btnNapoje);
        Button btnPrzekaski = findViewById(R.id.btnPrzekaski);
        Button btnLokale = findViewById(R.id.btnLokale);

        btnNapoje.setOnClickListener(v -> openCategory(DatabaseHelper.TABLE_NAPOJE));
        btnPrzekaski.setOnClickListener(v -> openCategory(DatabaseHelper.TABLE_PRZEKASKI));
        btnLokale.setOnClickListener(v -> openCategory(DatabaseHelper.TABLE_LOKALE));
    }

    private void openCategory(String categoryTable) {
        Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
        intent.putExtra("CATEGORY_TABLE", categoryTable);
        startActivity(intent);
    }
}