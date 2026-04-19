package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class HiLoActivity extends AppCompatActivity {

    ImageView imgCard;
    TextView txtInfo, txtResult;
    Button btnHigh, btnLow;

    ArrayList<Integer> deck = new ArrayList<>();

    int currentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilo);

        imgCard = findViewById(R.id.imgCard);
        txtInfo = findViewById(R.id.txtInfo);
        txtResult = findViewById(R.id.txtResult);
        btnHigh = findViewById(R.id.btnHigh);
        btnLow = findViewById(R.id.btnLow);

        setupDeck();
        drawCard();

        btnHigh.setOnClickListener(v -> play(true));
        btnLow.setOnClickListener(v -> play(false));
    }

    void setupDeck() {
        deck.clear();
        for (int i = 1; i <= 52; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);
    }

    void drawCard() {
        currentCard = deck.remove(0);
        showCard(currentCard);
    }

    void play(boolean high) {

        if (deck.isEmpty()) setupDeck();

        int nextCard = deck.remove(0);

        int currentValue = getValue(currentCard);
        int nextValue = getValue(nextCard);

        boolean win;

        if (high) {
            win = nextValue > currentValue;
        } else {
            win = nextValue < currentValue;
        }

        if (nextValue == currentValue) {
            win = false;
        }

        showCard(nextCard);

        if (win) {
            txtResult.setText("WYGRANA!");
        } else {
            txtResult.setText("PRZEGRANA! | Mnożnik: 2x");
        }

        currentCard = nextCard;
    }

    void showCard(int card) {
        String name = "card_" + card;
        int res = getResources().getIdentifier(name, "drawable", getPackageName());
        imgCard.setImageResource(res);

        txtInfo.setText("Karta: " + cardName(card));
    }

    int getValue(int card) {
        int v = card % 13;
        if (v == 0) v = 13;
        if (v == 1) return 14;
        return v;
    }

    String cardName(int card) {
        int v = card % 13;
        if (v == 0) v = 13;

        switch (v) {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return String.valueOf(v);
        }
    }
}