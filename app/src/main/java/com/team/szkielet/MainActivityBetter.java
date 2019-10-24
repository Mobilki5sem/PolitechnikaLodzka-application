package com.team.szkielet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.team.szkielet.quiz.QuizMainActivity;

public class MainActivityBetter extends AppCompatActivity {

    CardView cvPlanZajec, cvProwadzacy, cvWydarzenia, cvQuiz, cvSale, cvUstawienia;
    private long backPressedTime;
    TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_better);

        tvHello = findViewById(R.id.tvHello);

        Plany.czyMamyZapisaneDane = readingFromSharedPreferences();

        cvPlanZajec = findViewById(R.id.cvPlanZajec);
        cvPlanZajec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(Plany.czyMamyZapisaneDane){
                    intent = new Intent(MainActivityBetter.this, Plany.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(MainActivityBetter.this, PopUpInPlany.class);
                    startActivity(intent);
                }
            }
        });

        cvProwadzacy = findViewById(R.id.cvProwadzacy);
        cvProwadzacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityBetter.this, Prowadzacy.class);
                startActivity(intent);
            }
        });

        cvWydarzenia = findViewById(R.id.cvWydarzenia);
        cvWydarzenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityBetter.this, Aktualnosci.class);
                startActivity(intent);
            }
        });

        cvQuiz = findViewById(R.id.cvQuiz);
        cvQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivityBetter.this, "Witamy w Quizie", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivityBetter.this, QuizMainActivity.class));
            }
        });

        cvSale = findViewById(R.id.cvSale);
        cvSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cvUstawienia = findViewById(R.id.cvUstawienia);
        cvUstawienia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityBetter.this, PopUpInPlany.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        readingFromSharedPreferences();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        }
        else {
            Toast.makeText(MainActivityBetter.this, "Press back again to quit app", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    boolean readingFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("UserInfo", 0);
        String name = sharedPref.getString("name", "");
        String stopien = sharedPref.getString("stopien", "");
        String kierunek = sharedPref.getString("kierunek", "");
        String rodzaj = sharedPref.getString("rodzaj", "");
        String rok = sharedPref.getString("rok", "");
        if(name.length()>0 && !rok.equals("0")) {
            tvHello.setText("Cześć " + name + "!!!\nStopień: " + stopien + " Kierunek: " + kierunek + "\nRodzaj: " + rodzaj + " Rok: " + rok);
            PopUpInPlany.wasSaved = true;
            return true;
        }
        else if(name.length()>0 && rok.equals("0")) {
            tvHello.setText("Cześć " + name + "!!!\nStopień: " + stopien + " Kierunek: " + kierunek + "\nRodzaj: " + rodzaj);
            PopUpInPlany.wasSaved = true;
            return true;
        }
        PopUpInPlany.wasSaved = false;
        return false;
    }
}
