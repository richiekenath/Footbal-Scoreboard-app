package com.example.scoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private int count1 = 0;
    private int count2 = 0;
    private TextView showcount1;
    private TextView showcount2;

    private Button startStopButton, halftimeButton, fulltimeButton;

    private TextView clockTextView, goalCount1TextView, goalCount2TextView, team1TextView, team2TextView;

    private CountDownTimer timer;

    private boolean isTimerRunning = false;

    private long timeRemaining = 0;
    private Handler handler;
    private Runnable runnable;
    private boolean isRunning = false;
    private long startTime = 0;
    private long elapsedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showcount1 = (TextView) findViewById(R.id.goalcount1);
        showcount2 = (TextView) findViewById(R.id.goalcount2);
        Button b1, b2;
        b1 = (Button) findViewById(R.id.goal1);
        b2 = (Button) findViewById(R.id.goal2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count1++;
                if (showcount1 != null) {
                    showcount1.setText(Integer.toString(count1));
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count2++;
                if (showcount2 != null) {
                    showcount2.setText(Integer.toString(count2));
                }
            }
        });

        // Find the views
        startStopButton = findViewById(R.id.startstop);
        halftimeButton = findViewById(R.id.halftime);
        fulltimeButton = findViewById(R.id.fulltime);
        clockTextView = findViewById(R.id.clock);
        goalCount1TextView = findViewById(R.id.goalcount1);
        goalCount2TextView = findViewById(R.id.goalcount2);
        team1TextView = findViewById(R.id.team1);
        team2TextView = findViewById(R.id.team2);

        fulltimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopClock();
                showResult();
            }
        });


        // Set OnClickListener for start/stop button
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    stopClock();
                } else {
                    startClock();
                }
            }
        });

        // Set OnClickListener for halftime button
        halftimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHalftime();
            }
        });

        // Set OnClickListener for fulltime button
        fulltimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopClock();
                showResult();
            }
        });

        // Initialize the handler
        handler = new Handler();
    }

    private void startClock() {
        startTime = System.currentTimeMillis() - elapsedTime;
        isRunning = true;
        updateClockText();
        runClock();
        startStopButton.setText("STOP");
    }

    private void stopClock() {
        isRunning = false;
        handler.removeCallbacks(runnable);
        elapsedTime = System.currentTimeMillis() - startTime;
        startStopButton.setText("START");
    }

    private void runClock() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    updateClockText();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    private void updateClockText() {
        long currentTime = System.currentTimeMillis() - startTime;
        long seconds = (currentTime / 1000) % 60;
        long minutes = (currentTime / (1000 * 60)) % 60;

        String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        clockTextView.setText(time);
    }

    private void setHalftime() {
        elapsedTime = 2700000; // 45 minutes in milliseconds
        startTime = System.currentTimeMillis() - elapsedTime;
        isRunning = true;
        updateClockText();
        TextView periodTextView = findViewById(R.id.periodcount);
        periodTextView.setText("2");
    }

    private void showResult() {
        int goalCount1 = Integer.parseInt(goalCount1TextView.getText().toString());
        int goalCount2 = Integer.parseInt(goalCount2TextView.getText().toString());

        String team1 = team1TextView.getText().toString();
        String team2 = team2TextView.getText().toString();

        if (goalCount1 > goalCount2) {
            String message = team1 + " won the match!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (goalCount1 < goalCount2) {
            String message = team2 + " won the match!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            String message = "It's a draw!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
