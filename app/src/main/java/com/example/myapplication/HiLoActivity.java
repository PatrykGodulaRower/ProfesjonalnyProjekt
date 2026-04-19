package com.example.myapplication;

import android.os.Bundle;
import android.view.View;import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HiLoActivity extends AppCompatActivity {

    private ImageView imgCard;
    private TextView txtInfo, txtResult;
    private Button btnHigh, btnLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hilo);

        // 1. Bezpieczna obsługa Edge-to-Edge (zapobiega crashom na starcie)
        View mainView = findViewById(android.R.id.content);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // 2. Powiązanie elementów z Twojego pliku XML
        imgCard = findViewById(R.id.imgCard);
        txtInfo = findViewById(R.id.txtInfo);
        txtResult = findViewById(R.id.txtResult);
        btnHigh = findViewById(R.id.btnHigh);
        btnLow = findViewById(R.id.btnLow);

        // 3. Obsługa kliknięć (logika gry)
        btnHigh.setOnClickListener(v -> {
            txtResult.setText("Wybrałeś: WYŻEJ");
            // Tutaj dodaj logikę losowania karty
        });

        btnLow.setOnClickListener(v -> {
            txtResult.setText("Wybrałeś: NIŻEJ");
            // Tutaj dodaj logikę losowania karty
        });
    }
}