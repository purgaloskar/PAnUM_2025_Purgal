package com.example.projektkwij;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spTyp, spZrodlo, spCel;
    EditText etWartosc;
    TextView tvWynik;
    Button btnSwap;

    Bundle savedState;

    String[] typy = {
            "Systemy liczbowe",
            "Waluty",
            "Długość",
            "Pole powierzchni"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedState = savedInstanceState;

        spTyp = findViewById(R.id.spTyp);
        spZrodlo = findViewById(R.id.spZrodlo);
        spCel = findViewById(R.id.spCel);
        etWartosc = findViewById(R.id.etWartosc);
        tvWynik = findViewById(R.id.tvWynik);
        btnSwap = findViewById(R.id.btnSwap);

        ArrayAdapter<String> adapterTyp = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                typy
        );

        adapterTyp.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spTyp.setAdapter(adapterTyp);

        spTyp.post(() -> {

            ustawJednostki();

            if (savedState != null) {

                spTyp.setSelection(savedState.getInt("typ"));

                ustawJednostki();

                spZrodlo.setSelection(savedState.getInt("z"));
                spCel.setSelection(savedState.getInt("c"));

                etWartosc.setText(savedState.getString("val"));

                przelicz();
            } else {
                przelicz();
            }
        });

        spTyp.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                ustawJednostki();
                przelicz();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        spZrodlo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                przelicz();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        spCel.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                przelicz();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        etWartosc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                przelicz();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSwap.setOnClickListener(v -> {
            int z = spZrodlo.getSelectedItemPosition();
            int c = spCel.getSelectedItemPosition();

            spZrodlo.setSelection(c);
            spCel.setSelection(z);

            przelicz();
        });
    }

    private void ustawJednostki() {

        if (spTyp.getSelectedItem() == null) return;

        String typ = spTyp.getSelectedItem().toString();
        String[] dane;

        if (typ.equals("Waluty")) {
            dane = new String[]{"PLN", "USD", "EUR", "GBP", "CHF"};
        } else if (typ.equals("Długość")) {
            dane = new String[]{"mm", "cm", "m", "km", "in", "ft", "yd"};
        } else if (typ.equals("Pole powierzchni")) {
            dane = new String[]{"mm²", "cm²", "m²", "km²", "ar", "ha"};
        } else {
            dane = new String[]{"BIN", "BASE4", "DEC", "OCT", "HEX"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dane
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spZrodlo.setAdapter(adapter);
        spCel.setAdapter(adapter);
    }

    private void przelicz() {

        if (spTyp.getSelectedItem() == null ||
                spZrodlo.getSelectedItem() == null ||
                spCel.getSelectedItem() == null) {
            return;
        }

        String tekst = etWartosc.getText().toString().trim();

        if (tekst.isEmpty()) {
            tvWynik.setText("---");
            return;
        }

        String typ = spTyp.getSelectedItem().toString();

        // SYSTEMY LICZBOWE
        if (typ.equals("Systemy liczbowe")) {

            try {
                tvWynik.setText(konwertujSystem(tekst));
            } catch (Exception e) {
                tvWynik.setText("Błąd");
            }

            return;
        }

        // POZOSTAŁE KONWERSJE NUMERYCZNE
        double value;

        try {
            value = Double.parseDouble(tekst);
        } catch (Exception e) {
            tvWynik.setText("Błąd");
            return;
        }

        double wynik;

        if (typ.equals("Długość")) {

            try {
                wynik = konwertujDlugosc(value);
                tvWynik.setText(String.format("%.4f", wynik));
            } catch (Exception e) {
                tvWynik.setText("Błąd");
            }

        } else if (typ.equals("Waluty")) {

            try {
                wynik = konwertujWalute(value);
                tvWynik.setText(String.format("%.4f", wynik));
            } catch (Exception e) {
                tvWynik.setText("Błąd");
            }

        } else if (typ.equals("Pole powierzchni")) {

            try {
                wynik = konwertujPole(value);
                tvWynik.setText(String.format("%.4f", wynik));
            } catch (Exception e) {
                tvWynik.setText("Błąd");
            }
        }
    }

    private double konwertujDlugosc(double w) {

        String z = spZrodlo.getSelectedItem().toString();
        String c = spCel.getSelectedItem().toString();

        double m = 0;

        if (z.equals("mm")) m = w / 1000;
        else if (z.equals("cm")) m = w / 100;
        else if (z.equals("m")) m = w;
        else if (z.equals("km")) m = w * 1000;
        else if (z.equals("in")) m = w * 0.0254;
        else if (z.equals("ft")) m = w * 0.3048;
        else if (z.equals("yd")) m = w * 0.9144;

        if (c.equals("mm")) return m * 1000;
        if (c.equals("cm")) return m * 100;
        if (c.equals("m")) return m;
        if (c.equals("km")) return m / 1000;
        if (c.equals("in")) return m / 0.0254;
        if (c.equals("ft")) return m / 0.3048;
        if (c.equals("yd")) return m / 0.9144;

        return 0;
    }

    private double konwertujWalute(double w) {

        double[] kurs = {1, 4, 4.3, 5, 4.6};

        int z = spZrodlo.getSelectedItemPosition();
        int c = spCel.getSelectedItemPosition();

        if (z < 0 || c < 0) {
            return 0;
        }

        double pln = w * kurs[z];

        return pln / kurs[c];
    }

    private double konwertujPole(double w) {

        double[] k = {0.000001, 0.0001, 1, 1000000, 100, 10000};

        int z = spZrodlo.getSelectedItemPosition();
        int c = spCel.getSelectedItemPosition();

        if (z < 0 || c < 0) {
            return 0;
        }

        double m2 = w * k[z];

        return m2 / k[c];
    }

    private String konwertujSystem(String liczba) {

        String z = spZrodlo.getSelectedItem().toString();
        String c = spCel.getSelectedItem().toString();

        int bz = 10;
        int bc = 10;

        if (z.equals("BIN")) bz = 2;
        else if (z.equals("BASE4")) bz = 4;
        else if (z.equals("OCT")) bz = 8;
        else if (z.equals("HEX")) bz = 16;

        if (c.equals("BIN")) bc = 2;
        else if (c.equals("BASE4")) bc = 4;
        else if (c.equals("OCT")) bc = 8;
        else if (c.equals("HEX")) bc = 16;

        int val = Integer.parseInt(liczba, bz);

        return Integer.toString(val, bc).toUpperCase();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("val", etWartosc.getText().toString());
        outState.putInt("typ", spTyp.getSelectedItemPosition());
        outState.putInt("z", spZrodlo.getSelectedItemPosition());
        outState.putInt("c", spCel.getSelectedItemPosition());
    }
}