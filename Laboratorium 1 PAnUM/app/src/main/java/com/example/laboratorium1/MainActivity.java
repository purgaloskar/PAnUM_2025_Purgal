package com.example.laboratorium1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etLiczba;
    Button btnKonwertuj;
    TextView tvWynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLiczba = findViewById(R.id.etLiczba);
        btnKonwertuj = findViewById(R.id.btnKonwertuj);
        tvWynik = findViewById(R.id.tvWynik);

        btnKonwertuj.setOnClickListener(v -> {

            String tekst = etLiczba.getText().toString();

            if (tekst.isEmpty()) {
                Toast.makeText(this,
                        "Podaj liczbę",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int liczba = Integer.parseInt(tekst);

            if (liczba < 1 || liczba > 3999) {
                Toast.makeText(this,
                        "Zakres 1-3999",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            tvWynik.setText(arabskieNaRzymskie(liczba));
        });
    }

    private String arabskieNaRzymskie(int liczba) {

        int[] wartosci = {
                1000, 900, 500, 400,
                100, 90, 50, 40,
                10, 9, 5, 4, 1
        };

        String[] symbole = {
                "M", "CM", "D", "CD",
                "C", "XC", "L", "XL",
                "X", "IX", "V", "IV", "I"
        };

        StringBuilder wynik = new StringBuilder();

        for (int i = 0; i < wartosci.length; i++) {

            while (liczba >= wartosci[i]) {

                wynik.append(symbole[i]);
                liczba -= wartosci[i];
            }
        }

        return wynik.toString();
    }
}