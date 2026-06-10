package com.example.kolkoikrzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerGames;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerGames = findViewById(R.id.spinnerGames);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {

            int games = Integer.parseInt(
                    spinnerGames.getSelectedItem().toString());

            Intent intent =
                    new Intent(MainActivity.this,
                            GameActivity.class);

            intent.putExtra("games", games);

            startActivity(intent);
        });
    }
}