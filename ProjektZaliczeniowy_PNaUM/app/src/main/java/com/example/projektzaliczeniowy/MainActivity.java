package com.example.projektzaliczeniowy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Spinner spinnerProdukty;
    private EditText etIlosc;
    private ArrayList<String> listaProduktowNazwy = new ArrayList<>();
    private ArrayList<Double> listaProduktowCeny = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        spinnerProdukty = findViewById(R.id.spinner_produkty);
        etIlosc = findViewById(R.id.et_ilosc);
        Button btnDodaj = findViewById(R.id.btn_dodaj);
        Button btnIdzDoKoszyka = findViewById(R.id.btn_idz_do_koszyka);

        wczytajDaneZBd();

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iloscStr = etIlosc.getText().toString().trim();
                if (iloscStr.isEmpty() || iloscStr.equals("0")) {
                    Toast.makeText(MainActivity.this, "Wprowadź poprawną ilość!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int pozycja = spinnerProdukty.getSelectedItemPosition();
                String nazwa = listaProduktowNazwy.get(pozycja).split(" - ")[0];
                double cena = listaProduktowCeny.get(pozycja);
                int ilosc = Integer.parseInt(iloscStr);

                dbHelper.dodajDoKoszyka(nazwa, ilosc, cena);
                Toast.makeText(MainActivity.this, "Dodano do koszyka: " + nazwa, Toast.LENGTH_SHORT).show();
                etIlosc.setText("");
            }
        });

        btnIdzDoKoszyka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void wczytajDaneZBd() {
        Cursor cursor = dbHelper.getProdukty();
        if (cursor.moveToFirst()) {
            do {
                String nazwa = cursor.getString(1);
                double cena = cursor.getDouble(2);
                listaProduktowNazwy.add(nazwa + " - " + cena + " zł");
                listaProduktowCeny.add(cena);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProduktowNazwy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProdukty.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_koszyk) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_o_nas) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}