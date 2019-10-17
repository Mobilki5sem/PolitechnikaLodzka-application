package com.team.szkielet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    Button btnQuiz, btnChangePersonalInfo;
    TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Politechnika Łódzka FTIMS");
        /*actionBar.setIcon();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/
        btnQuiz = findViewById(R.id.btnQuiz);
        btnChangePersonalInfo = findViewById(R.id.btnChangePersonalInfo);
        tvHello = findViewById(R.id.tvHello);

        Plany.czyMamyZapisaneDane = readingFromSharedPreferences();

        btnChangePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PopUpInPlany.class));
            }
        });
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Creating in progress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        readingFromSharedPreferences();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.plany) {
            Intent intent = new Intent(MainActivity.this, Plany.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.prowadzacy) {
            Intent intent = new Intent(MainActivity.this, Prowadzacy.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.aktualnosci) {
            Intent intent = new Intent(MainActivity.this, Aktualnosci.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.start) {}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        }
        else {
            Toast.makeText(MainActivity.this, "Press back again to quit app", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    boolean readingFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("UserInfo", 0);
        String name = sharedPref.getString("name", "");
        String stopien = sharedPref.getString("stopien", "");
        String kierunek = sharedPref.getString("kierunek", "");
        String rodzaj = sharedPref.getString("rodzaj", "");
        if(name.length()>0) {
            tvHello.setText("Cześć " + name + "!!!\nStopień: " + stopien + "\nKierunek: " + kierunek + "\nRodzaj: " + rodzaj);
            return true;
        }
        return false;
    }
}
