package com.example.laboratorium4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvCzas;
    Button btnStart, btnPauza, btnReset, btnMinutnik;

    Handler handler = new Handler();

    long czas = 0;
    boolean uruchomiony = false;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (uruchomiony) {

                czas += 10;

                int setne = (int) ((czas / 10) % 100);
                int sekundy = (int) ((czas / 1000) % 60);
                int minuty = (int) ((czas / 60000));

                String wynik = String.format(
                        "%02d:%02d:%02d",
                        minuty,
                        sekundy,
                        setne
                );

                tvCzas.setText(wynik);

                handler.postDelayed(this, 10);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCzas = findViewById(R.id.tvCzas);

        btnStart = findViewById(R.id.btnStart);
        btnPauza = findViewById(R.id.btnPauza);
        btnReset = findViewById(R.id.btnReset);
        btnMinutnik = findViewById(R.id.btnMinutnik);

        btnStart.setOnClickListener(v -> {

            if (!uruchomiony) {
                uruchomiony = true;
                handler.post(runnable);
            }
        });

        btnPauza.setOnClickListener(v -> {
            uruchomiony = false;
        });

        btnReset.setOnClickListener(v -> {

            uruchomiony = false;
            czas = 0;

            tvCzas.setText("00:00:00");
        });

        btnMinutnik.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            TimerActivity.class);

            startActivity(intent);
        });
    }
}