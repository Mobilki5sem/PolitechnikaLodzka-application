package com.team.szkielet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopUpInPlany extends AppCompatActivity {

    private Button btnOK;
    private TextView ptName;
    private boolean czyZapisano = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_in_plany);

        ptName = findViewById(R.id.ptName);

        btnOK = findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                czyZapisano = true;
                onBackPressed();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.75));
    }

    @Override
    public void onBackPressed() {
        if(czyZapisano) {
            super.onBackPressed();
        }
    }
}
