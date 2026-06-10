package com.example.kolkoikrzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private Button[] cells = new Button[9];
    private String[] board = new String[9];

    private String currentPlayer = "X";

    private TextView txtStats;

    private GameManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_acitivty);

        int totalGames =
                getIntent().getIntExtra("games", 5);

        manager = new GameManager(totalGames);

        txtStats = findViewById(R.id.txtStats);

        cells[0] = findViewById(R.id.btn0);
        cells[1] = findViewById(R.id.btn1);
        cells[2] = findViewById(R.id.btn2);
        cells[3] = findViewById(R.id.btn3);
        cells[4] = findViewById(R.id.btn4);
        cells[5] = findViewById(R.id.btn5);
        cells[6] = findViewById(R.id.btn6);
        cells[7] = findViewById(R.id.btn7);
        cells[8] = findViewById(R.id.btn8);

        Button btnNewMatch =
                findViewById(R.id.btnNewMatch);

        btnNewMatch.setOnClickListener(v -> {

            Intent intent =
                    new Intent(GameActivity.this,
                            MainActivity.class);

            startActivity(intent);
            finish();
        });

        startNewBoard();

        for (int i = 0; i < 9; i++) {

            final int index = i;

            cells[i].setOnClickListener(v -> {

                if (!board[index].equals(""))
                    return;

                board[index] = currentPlayer;
                cells[index].setText(currentPlayer);

                if (checkWinner()) {

                    if (currentPlayer.equals("X")) {
                        manager.addXWin();
                    } else {
                        manager.addOWin();
                    }

                    nextRound();
                }
                else if (isBoardFull()) {

                    manager.addDraw();

                    nextRound();
                }
                else {

                    currentPlayer =
                            currentPlayer.equals("X")
                                    ? "O"
                                    : "X";

                    updateStats();
                }
            });
        }
    }

    private void nextRound() {

        if (manager.getPlayedGames()
                >= manager.getTotalGames()) {

            Intent intent =
                    new Intent(this,
                            SummaryActivity.class);

            intent.putExtra("manager", manager);

            startActivity(intent);
            finish();

            return;
        }

        startNewBoard();
    }

    private void startNewBoard() {

        currentPlayer = "X";

        for (int i = 0; i < 9; i++) {

            board[i] = "";

            cells[i].setText("");
            cells[i].setEnabled(true);
        }

        updateStats();
    }

    private void updateStats() {

        txtStats.setText(

                "Rozegrane: "
                        + manager.getPlayedGames()

                        + " | Pozostało: "
                        + manager.getRemainingGames()

                        + "\n"

                        + "Punkty X: "
                        + manager.getXPoints()

                        + " | Punkty O: "
                        + manager.getOPoints()

                        + "\n"

                        + "Tura: "
                        + currentPlayer
        );
    }

    private boolean isBoardFull() {

        for (String s : board) {

            if (s.equals(""))
                return false;
        }

        return true;
    }

    private boolean checkWinner() {

        int[][] wins = {

                {0,1,2},
                {3,4,5},
                {6,7,8},

                {0,3,6},
                {1,4,7},
                {2,5,8},

                {0,4,8},
                {2,4,6}
        };

        for (int[] win : wins) {

            String a = board[win[0]];
            String b = board[win[1]];
            String c = board[win[2]];

            if (!a.equals("")
                    && a.equals(b)
                    && b.equals(c)) {

                return true;
            }
        }

        return false;
    }
}