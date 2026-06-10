package com.example.laboratorium4;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    EditText etSekundy;
    TextView tvMinutnik;

    Button btnStartTimer;
    Button btnPauzaTimer;
    Button btnResetTimer;
    Button btnStoper;

    CountDownTimer countDownTimer;

    long pozostalyCzas = 0;
    boolean dziala = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        etSekundy = findViewById(R.id.etSekundy);
        tvMinutnik = findViewById(R.id.tvMinutnik);

        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnPauzaTimer = findViewById(R.id.btnPauzaTimer);
        btnResetTimer = findViewById(R.id.btnResetTimer);
        btnStoper = findViewById(R.id.btnStoper);

        btnStartTimer.setOnClickListener(v -> {

            if (!dziala) {

                if (pozostalyCzas == 0) {

                    String tekst =
                            etSekundy.getText().toString();

                    if (tekst.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Podaj liczbę sekund",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    pozostalyCzas =
                            Long.parseLong(tekst) * 1000;
                }

                uruchomMinutnik();
            }
        });

        btnPauzaTimer.setOnClickListener(v -> {

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            dziala = false;
        });

        btnResetTimer.setOnClickListener(v -> {

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            pozostalyCzas = 0;
            dziala = false;

            tvMinutnik.setText("00:00");
        });

        btnStoper.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TimerActivity.this,
                            MainActivity.class);

            startActivity(intent);
        });
    }

    private void uruchomMinutnik() {

        dziala = true;

        countDownTimer =
                new CountDownTimer(pozostalyCzas, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        pozostalyCzas = millisUntilFinished;

                        int sek =
                                (int) (millisUntilFinished / 1000);

                        int min = sek / 60;
                        sek = sek % 60;

                        tvMinutnik.setText(
                                String.format(
                                        "%02d:%02d",
                                        min,
                                        sek
                                )
                        );
                    }

                    @Override
                    public void onFinish() {

                        dziala = false;
                        pozostalyCzas = 0;

                        tvMinutnik.setText("00:00");

                        Toast.makeText(
                                TimerActivity.this,
                                "Czas minął!",
                                Toast.LENGTH_LONG
                        ).show();

                        ToneGenerator toneGen =
                                new ToneGenerator(
                                        AudioManager.STREAM_MUSIC,
                                        100);

                        toneGen.startTone(
                                ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE,
                                500
                        );
                    }
                };

        countDownTimer.start();
    }
}