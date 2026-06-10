package com.example.laboratorium2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etArabskie, etRzymskie;
    Button btnArabskie, btnRzymskie;
    TextView tvRzymskiWynik, tvArabskiWynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etArabskie = findViewById(R.id.etArabskie);
        etRzymskie = findViewById(R.id.etRzymskie);

        btnArabskie = findViewById(R.id.btnArabskie);
        btnRzymskie = findViewById(R.id.btnRzymskie);

        tvRzymskiWynik = findViewById(R.id.tvRzymskiWynik);
        tvArabskiWynik = findViewById(R.id.tvArabskiWynik);

        btnArabskie.setOnClickListener(v -> {

            String tekst = etArabskie.getText().toString();

            if (tekst.isEmpty()) {
                Toast.makeText(this, "Podaj liczbę", Toast.LENGTH_SHORT).show();
                return;
            }

            int liczba = Integer.parseInt(tekst);

            if (liczba < 1 || liczba > 3999) {
                Toast.makeText(this, "Zakres 1-3999", Toast.LENGTH_SHORT).show();
                return;
            }

            tvRzymskiWynik.setText(arabskieNaRzymskie(liczba));
        });

        btnRzymskie.setOnClickListener(v -> {

            String tekst = etRzymskie.getText().toString().toUpperCase();

            if (tekst.isEmpty()) {
                Toast.makeText(this, "Podaj liczbę rzymską", Toast.LENGTH_SHORT).show();
                return;
            }

            tvArabskiWynik.setText(
                    String.valueOf(rzymskieNaArabskie(tekst))
            );
        });
    }

    private String arabskieNaRzymskie(int liczba) {

        int[] wartosci = {
                1000,900,500,400,
                100,90,50,40,
                10,9,5,4,1
        };

        String[] symbole = {
                "M","CM","D","CD",
                "C","XC","L","XL",
                "X","IX","V","IV","I"
        };

        StringBuilder wynik = new StringBuilder();

        for(int i=0;i<wartosci.length;i++) {
            while(liczba >= wartosci[i]) {
                wynik.append(symbole[i]);
                liczba -= wartosci[i];
            }
        }

        return wynik.toString();
    }

    private int rzymskieNaArabskie(String tekst) {

        int suma = 0;
        int poprzednia = 0;

        for(int i = tekst.length()-1; i >= 0; i--) {

            int wartosc = wartoscRzymska(tekst.charAt(i));

            if(wartosc < poprzednia)
                suma -= wartosc;
            else
                suma += wartosc;

            poprzednia = wartosc;
        }

        return suma;
    }

    private int wartoscRzymska(char znak) {

        switch (znak) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }
}