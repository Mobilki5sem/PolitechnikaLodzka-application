package com.team.szkielet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PopUpInPlany extends AppCompatActivity {

    private Button btnOK, btnCancel;
    private TextView ptName;
    private RadioGroup rgStopien, rgKierunek, rgRodzaj;
    RadioButton rbStopien, rbKierunek, rbRodzaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_in_plany);

        ptName = findViewById(R.id.ptName);
        rgStopien = findViewById(R.id.rgStopien);
        rgKierunek = findViewById(R.id.rgKierunek);
        rgRodzaj = findViewById(R.id.rgRodzaj);

        btnOK = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tab[] = new boolean[4];
                for (int i = 0; i < 4; i++) {
                    tab[i] = false;
                }
                tab[0] = checkIfPressed(rgStopien, "Wybierz stopień studiów");
                tab[1] = checkIfPressed(rgKierunek, "Wybierz kierunek studiów");
                tab[2] = checkIfPressed(rgRodzaj, "Wybierz rodzaj studiów");
                tab[3] = checkIfNameWasPassed();

                if(tab[0] && tab[1] && tab[2] && tab[3]) {
                    Plany.czyMamyZapisaneDane = true;
                    // teraz tak musze sprawdzic w glownym layoucie sprawdzenie
                    // czy mamy jakies dane zapisane i wtedy true. w innym wypadku false

                    try {
                        rbStopien = findViewById(rgStopien.getCheckedRadioButtonId());
                        rbKierunek = findViewById(rgKierunek.getCheckedRadioButtonId());
                        rbRodzaj = findViewById(rgRodzaj.getCheckedRadioButtonId());

                        saveUserPreferences("name", ptName.getText().toString());
                        saveUserPreferences("stopien", rbStopien.getText().toString());
                        saveUserPreferences("kierunek", rbKierunek.getText().toString());
                        saveUserPreferences("rodzaj", rbRodzaj.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(PopUpInPlany.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    onBackPressed();
                }
                else {}
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.75));
    }

    boolean checkIfPressed(RadioGroup rb, String txt) {
        if (rb.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(PopUpInPlany.this, txt, Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
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
}
