package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Naprawa błędu crashowania (EdgeToEdge)
        View mainView = findViewById(android.R.id.content);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Przyciski nawigacji
        findViewById(R.id.btnRoulette).setOnClickListener(v ->
                startActivity(new Intent(this, RouletteActivity.class)));

        findViewById(R.id.btnBlackjack).setOnClickListener(v ->
                startActivity(new Intent(this, BlackjackActivity.class)));

        findViewById(R.id.btnHiLo).setOnClickListener(v ->
                startActivity(new Intent(this, HiLoActivity.class)));
    }
}