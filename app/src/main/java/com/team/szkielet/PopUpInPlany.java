package com.team.szkielet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PopUpInPlany extends AppCompatActivity {

    private Button btnOK, btnCancel;
    private TextView ptName, tvRok, tvStopien;
    private RadioGroup rgStopien, rgKierunek, rgRodzaj, rgRok;
    RadioButton rbStopien, rbKierunek, rbRodzaj, rbRok, rbIrok, rbIIrok, rbIIIrok, rbIVrok, rbIstopien, rbIIstopien, rbIIIstopien;
    private static int stopien = 0;
    boolean tab[] = new boolean[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_in_plany);

        for (int i = 0; i < 5; i++) {
            tab[i] = false;
        }

        ptName = findViewById(R.id.ptName);

        tvStopien = findViewById(R.id.tvStopien);
        rgStopien = findViewById(R.id.rgStopien);
        rbIstopien = findViewById(R.id.rbIstopien);
        rbIIstopien = findViewById(R.id.rbIIstopien);
        rbIIIstopien = findViewById(R.id.rbIIIstopien);
        tvStopien.setVisibility(View.GONE);
        rbIstopien.setVisibility(View.GONE);
        rbIIstopien.setVisibility(View.GONE);
        rbIIIstopien.setVisibility(View.GONE);
        rgStopien.setVisibility(View.GONE);

        rgKierunek = findViewById(R.id.rgKierunek);
        rgRodzaj = findViewById(R.id.rgRodzaj);
        rgRok = findViewById(R.id.rgRok);
        rgRok.setVisibility(View.GONE);

        tvRok = findViewById(R.id.tvRok);
        rbIrok = findViewById(R.id.rbIrok);
        rbIIrok = findViewById(R.id.rbIIrok);
        rbIIIrok = findViewById(R.id.rbIIIrok);
        rbIVrok = findViewById(R.id.rbIVrok);
        tvRok.setVisibility(View.GONE);
        rbIrok.setVisibility(View.GONE);
        rbIIrok.setVisibility(View.GONE);
        rbIIIrok.setVisibility(View.GONE);
        rbIVrok.setVisibility(View.GONE);

        rgRodzaj.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbRodzaj = findViewById(rgRodzaj.getCheckedRadioButtonId());
                if(rbRodzaj.getText().toString().equals("Niestacjonarne")) {
                    tvStopien.setVisibility(View.VISIBLE);
                    rbIstopien.setVisibility(View.VISIBLE);
                    rbIIstopien.setVisibility(View.VISIBLE);
                    rbIIIstopien.setVisibility(View.GONE);
                    rgStopien.setVisibility(View.VISIBLE);
                    tvRok.setVisibility(View.GONE);
                    rgRok.setVisibility(View.GONE);
                    checkrgSTOPIEN();
                    checkrgROK();

                }
                else {
                    tvStopien.setVisibility(View.VISIBLE);
                    rbIstopien.setVisibility(View.VISIBLE);
                    rbIIstopien.setVisibility(View.VISIBLE);
                    rbIIIstopien.setVisibility(View.VISIBLE);
                    rgStopien.setVisibility(View.VISIBLE);
                    tvRok.setVisibility(View.GONE);
                    rgRok.setVisibility(View.GONE);
                    checkrgSTOPIEN();
                    checkrgROK();
                }
            }
        });

        rgStopien.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                zmianaDlaStopnia();
                }
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnOK = findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tab[3] = checkIfNameWasPassed();
                tab[1] = checkIfPressed(rgKierunek, "Wybierz kierunek studiów");
                tab[2] = checkIfPressed(rgRodzaj, "Wybierz rodzaj studiów");
                tab[0] = checkIfPressed(rgStopien, "Wybierz stopień studiów");
                if(stopien == 1 || stopien == 2)
                    tab[4] = checkIfPressed(rgRok, "Wybierz rok studiów");
                else if (stopien == 3)
                    tab[4] = true;

                //Toast.makeText(PopUpInPlany.this, "" + tab[0] + tab[1] + tab[2] + tab[3] + tab[4], Toast.LENGTH_SHORT).show();

                if(tab[0] && tab[1] && tab[2] && tab[3] && tab[4]) {
                    Plany.czyMamyZapisaneDane = true;
                    try {
                        rbStopien = findViewById(rgStopien.getCheckedRadioButtonId());
                        rbKierunek = findViewById(rgKierunek.getCheckedRadioButtonId());
                        rbRodzaj = findViewById(rgRodzaj.getCheckedRadioButtonId());
                        if(stopien == 1 || stopien == 2) {
                            rbRok = findViewById(rgRok.getCheckedRadioButtonId());
                            saveUserPreferences("rok", rbRok.getText().toString());
                        }
                        else {
                            saveUserPreferences("rok", "0");
                        }
                        saveUserPreferences("name", ptName.getText().toString());
                        saveUserPreferences("stopien", rbStopien.getText().toString());
                        saveUserPreferences("kierunek", rbKierunek.getText().toString());
                        saveUserPreferences("rodzaj", rbRodzaj.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(PopUpInPlany.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    onBackPressed();
                }
                else {
                    for (int i = 0; i < 5; i++) {
                        tab[i] = false;
                    }
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.75));
    }

    void checkrgSTOPIEN() {
        if(!(rgStopien.getCheckedRadioButtonId() == -1)){
            rgStopien.setOnCheckedChangeListener(null);
            rgStopien.clearCheck();
            rgStopien.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    zmianaDlaStopnia();
                }
            });
        }
    }

    void checkrgROK() {
        if(!(rgRok.getCheckedRadioButtonId() == -1)){
            rgRok.setOnCheckedChangeListener(null);
            rgRok.clearCheck();
            rgRok.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                }
            });
        }
    }

    void zmianaDlaStopnia() {
        rbStopien = findViewById(rgStopien.getCheckedRadioButtonId());
        if(rbStopien.getText().toString().equals("I")) {
            tvRok.setVisibility(View.VISIBLE);
            rbIrok.setVisibility(View.VISIBLE);
            rbIIrok.setVisibility(View.VISIBLE);
            rbIIIrok.setVisibility(View.VISIBLE);
            rbIVrok.setVisibility(View.VISIBLE);
            rgRok.setVisibility(View.VISIBLE);
            stopien = 1;
            checkrgROK();
        }
        else if(rbStopien.getText().toString().equals("II")) {
            tvRok.setVisibility(View.VISIBLE);
            rbIrok.setVisibility(View.VISIBLE);
            rbIIrok.setVisibility(View.VISIBLE);
            rbIIIrok.setVisibility(View.GONE);
            rbIVrok.setVisibility(View.GONE);
            rgRok.setVisibility(View.VISIBLE);
            stopien = 2;
            checkrgROK();
        }
        else {
            tvRok.setVisibility(View.GONE);
            rbIrok.setVisibility(View.GONE);
            rbIIrok.setVisibility(View.GONE);
            rbIIIrok.setVisibility(View.GONE);
            rbIVrok.setVisibility(View.GONE);
            rgRok.setVisibility(View.GONE);
            stopien = 3;
            checkrgROK();
        }
    }

    boolean checkIfPressed(RadioGroup rb, String txt) {
        if (rb.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(PopUpInPlany.this, txt, Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    boolean checkIfNameWasPassed() {
        if(ptName.length() > 0){
            return true;
        }else {
            Toast.makeText(PopUpInPlany.this, "Wpisz swoje imie/nick", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void saveUserPreferences(String key, String value) {
        SharedPreferences sharedPref = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
