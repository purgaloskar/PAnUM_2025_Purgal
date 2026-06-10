package com.example.laboratorium3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etTempo, etDystansTempo;
    EditText etPredkosc, etDystansPredkosc;
    EditText etDystansCel, etCzasCel;

    Button btnTempo, btnPredkosc, btnCel;

    TextView tvTempoWynik, tvPredkoscWynik, tvCelWynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTempo = findViewById(R.id.etTempo);
        etDystansTempo = findViewById(R.id.etDystansTempo);

        etPredkosc = findViewById(R.id.etPredkosc);
        etDystansPredkosc = findViewById(R.id.etDystansPredkosc);

        etDystansCel = findViewById(R.id.etDystansCel);
        etCzasCel = findViewById(R.id.etCzasCel);

        btnTempo = findViewById(R.id.btnTempo);
        btnPredkosc = findViewById(R.id.btnPredkosc);
        btnCel = findViewById(R.id.btnCel);

        tvTempoWynik = findViewById(R.id.tvTempoWynik);
        tvPredkoscWynik = findViewById(R.id.tvPredkoscWynik);
        tvCelWynik = findViewById(R.id.tvCelWynik);

        btnTempo.setOnClickListener(v -> obliczZTempa());

        btnPredkosc.setOnClickListener(v -> obliczZPredkosci());

        btnCel.setOnClickListener(v -> obliczCel());
    }

    private void obliczZTempa() {

        if (etTempo.getText().toString().isEmpty() ||
                etDystansTempo.getText().toString().isEmpty()) {

            Toast.makeText(this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
            return;
        }

        double tempo = Double.parseDouble(etTempo.getText().toString());
        double dystans = Double.parseDouble(etDystansTempo.getText().toString());

        double predkosc = 60.0 / tempo;

        double maraton = tempo * 42.195;
        double polmaraton = tempo * 21.0975;
        double czasDystansu = tempo * dystans;

        tvTempoWynik.setText(
                "Prędkość: " + String.format("%.2f", predkosc) + " km/h\n\n" +
                        "Maraton: " + formatujCzas(maraton) + "\n" +
                        "Półmaraton: " + formatujCzas(polmaraton) + "\n" +
                        "Dystans " + dystans + " km: " +
                        formatujCzas(czasDystansu)
        );
    }

    private void obliczZPredkosci() {

        if (etPredkosc.getText().toString().isEmpty() ||
                etDystansPredkosc.getText().toString().isEmpty()) {

            Toast.makeText(this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
            return;
        }

        double predkosc =
                Double.parseDouble(etPredkosc.getText().toString());

        double dystans =
                Double.parseDouble(etDystansPredkosc.getText().toString());

        double tempo = 60.0 / predkosc;

        double maraton = tempo * 42.195;
        double polmaraton = tempo * 21.0975;
        double czasDystansu = tempo * dystans;

        tvPredkoscWynik.setText(
                "Tempo: " + String.format("%.2f", tempo) + " min/km\n\n" +
                        "Maraton: " + formatujCzas(maraton) + "\n" +
                        "Półmaraton: " + formatujCzas(polmaraton) + "\n" +
                        "Dystans " + dystans + " km: " +
                        formatujCzas(czasDystansu)
        );
    }

    private void obliczCel() {

        if (etDystansCel.getText().toString().isEmpty() ||
                etCzasCel.getText().toString().isEmpty()) {

            Toast.makeText(this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
            return;
        }

        double dystans =
                Double.parseDouble(etDystansCel.getText().toString());

        double czas =
                Double.parseDouble(etCzasCel.getText().toString());

        double tempo = czas / dystans;
        double predkosc = 60.0 / tempo;

        tvCelWynik.setText(
                "Wymagane tempo: " +
                        String.format("%.2f", tempo) +
                        " min/km\n\n" +
                        "Wymagana prędkość: " +
                        String.format("%.2f", predkosc) +
                        " km/h"
        );
    }

    private String formatujCzas(double minuty) {

        int sekundy = (int) (minuty * 60);

        int godz = sekundy / 3600;
        int min = (sekundy % 3600) / 60;
        int sek = sekundy % 60;

        return godz + " h " + min + " min " + sek + " s";
    }
}