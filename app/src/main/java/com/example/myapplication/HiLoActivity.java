package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Random;

public class HiLoActivity extends AppCompatActivity {

    private ImageView imgCard, imgPrevCard1, imgPrevCard2;
    private TextView txtInfo, txtResult, txtStage;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnCashOut;

    private int card1Val, card2Val, card3Val, card4Val;
    private int card1Idx, card2Idx, card3Idx, card4Idx;

    private int currentStage = 1; // 1 to 4
    private double currentMultiplier = 1.0;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hilo);

        // UI Inicjalizacja
        imgCard = findViewById(R.id.imgCard);
        imgPrevCard1 = findViewById(R.id.imgPrevCard1); // Dodaj te ID do XML!
        imgPrevCard2 = findViewById(R.id.imgPrevCard2);

        txtInfo = findViewById(R.id.txtInfo);
        txtResult = findViewById(R.id.txtResult);
        txtStage = findViewById(R.id.txtStage); // Opcjonalne pole na numer rundy

        btnOption1 = findViewById(R.id.btnHigh); // Używamy istniejących nazw ID
        btnOption2 = findViewById(R.id.btnLow);
        btnOption3 = findViewById(R.id.btnOption3); // Dodaj w XML
        btnOption4 = findViewById(R.id.btnOption4); // Dodaj w XML
        btnCashOut = findViewById(R.id.btnCashOut); // Dodaj w XML

        resetGame();

        btnCashOut.setOnClickListener(v -> showWinDialog("CASH OUT!"));
    }

    private void resetGame() {
        currentStage = 1;
        currentMultiplier = 1.0;
        imgPrevCard1.setVisibility(View.INVISIBLE);
        imgPrevCard2.setVisibility(View.INVISIBLE);
        startStage1();
    }

    // --- RUNDA 1: CZERWONE / CZARNE (x2) ---
    private void startStage1() {
        currentStage = 1;
        btnOption3.setVisibility(View.GONE);
        btnOption4.setVisibility(View.GONE);
        btnCashOut.setVisibility(View.GONE);

        txtInfo.setText("RUNDA 1: Wybierz kolor karty");
        btnOption1.setText("CZERWONE");
        btnOption2.setText("CZARNE");

        btnOption1.setOnClickListener(v -> resolveStage1(true)); // true = red
        btnOption2.setOnClickListener(v -> resolveStage1(false));
    }

    private void resolveStage1(boolean choseRed) {
        card1Idx = drawIndex();
        card1Val = getVal(card1Idx);
        updateMainCard(card1Idx);

        boolean isRed = (card1Idx >= 1 && card1Idx <= 13) || (card1Idx >= 27 && card1Idx <= 39);
        // Zakładam: a1-13 Kier(Red), a14-26 Trefl(Black), a27-39 Karo(Red), a40-52 Pik(Black)

        if (choseRed == isRed) {
            currentMultiplier = 2.0;
            showContinueDialog("Trafiłeś kolor!", this::startStage2);
        } else {
            showGameOverDialog();
        }
    }

    // --- RUNDA 2: WYŻEJ / NIŻEJ (x4) ---
    private void startStage2() {
        currentStage = 2;
        imgPrevCard1.setVisibility(View.VISIBLE);
        imgPrevCard1.setImageResource(getResId(card1Idx));

        txtInfo.setText("RUNDA 2: Następna wyższa czy niższa niż " + getCardName(card1Val) + "?");
        btnOption1.setText("WYŻEJ");
        btnOption2.setText("NIŻEJ");
        btnCashOut.setVisibility(View.VISIBLE);

        btnOption1.setOnClickListener(v -> resolveStage2(true));
        btnOption2.setOnClickListener(v -> resolveStage2(false));
    }

    private void resolveStage2(boolean choseHigh) {
        card2Idx = drawIndex();
        card2Val = getVal(card2Idx);
        updateMainCard(card2Idx);

        if (card2Val == card1Val) {
            startStage2(); // Remis - losuj jeszcze raz
            return;
        }

        if ((choseHigh && card2Val > card1Val) || (!choseHigh && card2Val < card1Val)) {
            currentMultiplier = 4.0;
            showContinueDialog("Trafiłeś!", this::startStage3);
        } else {
            showGameOverDialog();
        }
    }

    // --- RUNDA 3: W PRZEDZIALE (x8) ---
    private void startStage3() {
        currentStage = 3;
        imgPrevCard2.setVisibility(View.VISIBLE);
        imgPrevCard2.setImageResource(getResId(card2Idx));

        int min = Math.min(card1Val, card2Val);
        int max = Math.max(card1Val, card2Val);

        txtInfo.setText("RUNDA 3: Czy kolejna karta będzie MIĘDZY " + min + " a " + max + "?");
        btnOption1.setText("TAK (W ŚRODKU)");
        btnOption2.setText("NIE (POZA)");

        btnOption1.setOnClickListener(v -> resolveStage3(true));
        btnOption2.setOnClickListener(v -> resolveStage3(false));
    }

    private void resolveStage3(boolean choseInside) {
        card3Idx = drawIndex();
        card3Val = getVal(card3Idx);
        updateMainCard(card3Idx);

        int min = Math.min(card1Val, card2Val);
        int max = Math.max(card1Val, card2Val);
        boolean isInside = (card3Val >= min && card3Val <= max);

        if (choseInside == isInside) {
            currentMultiplier = 8.0;
            showContinueDialog("Niesamowite! Runda 3 zaliczona.", this::startStage4);
        } else {
            showGameOverDialog();
        }
    }

    // --- RUNDA 4: SYMBOL (JACKPOT x20) ---
    private void startStage4() {
        currentStage = 4;
        btnOption3.setVisibility(View.VISIBLE);
        btnOption4.setVisibility(View.VISIBLE);

        txtInfo.setText("OSTATNIA RUNDA: Jaki będzie symbol?");
        btnOption1.setText("KIER ♥");
        btnOption2.setText("TREFL ♣");
        btnOption3.setText("KARO ♦");
        btnOption4.setText("PIK ♠");

        btnOption1.setOnClickListener(v -> resolveStage4('a')); // a = 1-13
        btnOption2.setOnClickListener(v -> resolveStage4('b')); // b = 14-26
        btnOption3.setOnClickListener(v -> resolveStage4('c')); // c = 27-39
        btnOption4.setOnClickListener(v -> resolveStage4('d')); // d = 40-52
    }

    private void resolveStage4(char suit) {
        card4Idx = drawIndex();
        updateMainCard(card4Idx);

        boolean win = false;
        if (suit == 'a' && card4Idx <= 13) win = true;
        else if (suit == 'b' && card4Idx > 13 && card4Idx <= 26) win = true;
        else if (suit == 'c' && card4Idx > 26 && card4Idx <= 39) win = true;
        else if (suit == 'd' && card4Idx > 39) win = true;

        if (win) {
            currentMultiplier = 20.0;
            showWinDialog("JACKPOT!!! Mnożnik x20!");
        } else {
            showGameOverDialog();
        }
    }

    // --- POMOCNICZE ---

    private int drawIndex() { return random.nextInt(52) + 1; }

    private int getVal(int idx) {
        int v = (idx - 1) % 13 + 1;
        return (v == 1) ? 14 : v; // As najwyższy
    }

    private int getResId(int idx) {
        return getResources().getIdentifier("a" + idx, "drawable", getPackageName());
    }

    private void updateMainCard(int idx) {
        imgCard.setImageResource(getResId(idx));
        txtResult.setText("Aktualny mnożnik: x" + currentMultiplier);
    }

    private void showContinueDialog(String msg, Runnable nextStep) {
        new AlertDialog.Builder(this)
                .setTitle("WYGRANA!")
                .setMessage(msg + "\nObecny mnożnik: x" + currentMultiplier + "\nCo robisz?")
                .setPositiveButton("GRAJ DALEJ", (d, w) -> nextStep.run())
                .setNegativeButton("ZABIERZ KASĘ", (d, w) -> showWinDialog("Wypłacono!"))
                .setCancelable(false).show();
    }

    private void showWinDialog(String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("Twój końcowy wynik: x" + currentMultiplier)
                .setPositiveButton("MENU", (d, w) -> finish())
                .setCancelable(false).show();
    }

    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("PRZEGRANA 💀")
                .setMessage("Straciłeś wszystko!")
                .setPositiveButton("RESTART", (d, w) -> resetGame())
                .setNegativeButton("WYJŚCIE", (d, w) -> finish())
                .setCancelable(false).show();
    }

    private String getCardName(int val) {
        if (val == 14) return "As";
        if (val == 11) return "Walet";
        if (val == 12) return "Dama";
        if (val == 13) return "Król";
        return String.valueOf(val);
    }
}