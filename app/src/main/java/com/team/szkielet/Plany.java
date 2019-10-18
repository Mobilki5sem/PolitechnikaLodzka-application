package com.team.szkielet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Plany extends AppCompatActivity {

    private Button btnDownload;
    static boolean czyMamyZapisaneDane = false;
    String name, stopien, kierunek, rodzaj, rok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plany);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Plany zajęć");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        readFromSharedPreferences();

        if(!czyMamyZapisaneDane) {
            Intent intent = new Intent(Plany.this, PopUpInPlany.class);
            startActivity(intent);
        }

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WebView webView = findViewById(R.id.wvPDF);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(getLinkToPlan());
                //webView.loadUrl("https://ftims.edu.p.lodz.pl/mod/resource/view.php?id=44697");
            }
        });
    }

    String getLinkToPlan() {
        if(rodzaj.equals("Stacjonarne")) {
            if(stopien.equals("II")) {
                return "https://drive.google.com/file/d/1F5m5atJRbbU-8QxLg-3ohZNChVhxmeIh/view?usp=sharing";
            }
            else if(stopien.equals("III")) {
                return "https://drive.google.com/file/d/1XpanxDZRoLGclwNDZ9SItMFZoyaPFPYu/view?usp=sharing";
            }
            else if(stopien.equals("I")) {
                if(rok.equals("1")) {
                    return "https://drive.google.com/file/d/1i3jrN4CU4qn_a4C8jTlMd8bvplaaXErJ/view?usp=sharing";
                }
                else if(rok.equals(("2"))) {
                    return "https://drive.google.com/file/d/1G31HId4nRJGukgR592kdCZsHd-wO2loz/view?usp=sharing";
                }
                else if(rok.equals("3")) {
                    return "https://drive.google.com/file/d/1kI85gMGQQb2RL-VCNRY--gljygMNfO8e/view?usp=sharing";
                }
                else if(rok.equals("4")) {
                    return "https://drive.google.com/file/d/1kXLPoS2KwdeGD2hl-ge8b6LGihI5iy5C/view?usp=sharing";
                }
                else
                    return "error";
            }
            else
                return "error";
        }
        else { //niestacjonarne
            if(stopien.equals("II")) {
                if(rok.equals("1"))
                    return "https://drive.google.com/file/d/1RzBuP_yAHJ1yS_fw_y9_auV9dz03hRf1/view?usp=sharing";
                else
                    return "https://drive.google.com/file/d/1dkSSJRHPlCaR2fUMhPVhn0-8zwysiGnu/view?usp=sharing";
            }
            else if(stopien.equals("I")) {
                if(rok.equals("1")) {
                    return "https://drive.google.com/file/d/1zugoxAbHMbhe7v0y8Jvp5XOBSSKO1I7X/view?usp=sharing";
                }
                else if(rok.equals(("2"))) {
                    return "https://drive.google.com/file/d/1p5Rauz_iTh0WW_Z_rbYS1KDgwtzmcnox/view?usp=sharing";
                }
                else if(rok.equals("3")) {
                    return "https://drive.google.com/file/d/1ZSchIpk2RKaah_SoIciW2nXf3PaD4IWo/view?usp=sharing";
                }
                else if(rok.equals("4")) {
                    return "https://drive.google.com/file/d/1F_8NssnZSqhcrp3LH-nz5bcPtEC4xSJz/view?usp=sharing";
                }
                else
                    return "error";
            }
            else
                return "error";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.plany) {}
        else if(item.getItemId() == R.id.prowadzacy) {
            Intent intent = new Intent(Plany.this, Prowadzacy.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.aktualnosci) {
            Intent intent = new Intent(Plany.this, Aktualnosci.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.start) {
            Intent intent = new Intent(Plany.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Plany.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }

    void readFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("UserInfo", 0);
        name = sharedPref.getString("name", "");
        stopien = sharedPref.getString("stopien", "");
        kierunek = sharedPref.getString("kierunek", "");
        rodzaj = sharedPref.getString("rodzaj", "");
        rok = sharedPref.getString("rok", "");
    }
}
