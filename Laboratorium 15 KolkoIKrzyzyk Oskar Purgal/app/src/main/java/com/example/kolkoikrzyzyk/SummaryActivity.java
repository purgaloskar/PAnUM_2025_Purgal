package com.example.kolkoikrzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        GameManager manager =
                (GameManager) getIntent()
                        .getSerializableExtra("manager");

        TextView txtSummary =
                findViewById(R.id.txtSummary);

        Button btnRestart =
                findViewById(R.id.btnRestart);

        String winner;

        if (manager.getXPoints()
                > manager.getOPoints()) {

            winner = "Zwycięzca meczu: X";
        }
        else if (manager.getOPoints()
                > manager.getXPoints()) {

            winner = "Zwycięzca meczu: O";
        }
        else {

            winner = "REMIS";
        }

        txtSummary.setText(

                "Wygrane X: "
                        + manager.getXWins()

                        + "\n"

                        + "Wygrane O: "
                        + manager.getOWins()

                        + "\n"

                        + "Remisy: "
                        + manager.getDraws()

                        + "\n\n"

                        + "Punkty X: "
                        + manager.getXPoints()

                        + "\n"

                        + "Punkty O: "
                        + manager.getOPoints()

                        + "\n\n"

                        + winner
        );

        btnRestart.setOnClickListener(v -> {

            Intent intent =
                    new Intent(this,
                            MainActivity.class);

            startActivity(intent);
            finish();
        });
    }
}