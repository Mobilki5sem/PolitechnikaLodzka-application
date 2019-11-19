package com.team.szkielet.rooms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team.szkielet.R;

public class FindRoom extends AppCompatActivity {

    private RadioGroup rgBudynek;
    private RadioButton rbChecked;
    private EditText etSala;
    private Button btnPokazNaMapie;
    private TextView tvInfo;
    private ImageView ivBudynek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);

        rgBudynek = findViewById(R.id.rgBudynek);
        etSala = findViewById(R.id.etSala);
        etSala.clearFocus();
        btnPokazNaMapie = findViewById(R.id.btnPokazNaMapie);
        tvInfo = findViewById(R.id.tvInfo);
        ivBudynek = findViewById(R.id.ivBudynek);

        btnPokazNaMapie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfConnected()) {
                    if (rgBudynek.getCheckedRadioButtonId() != -1) {
                        int rbID = rgBudynek.getCheckedRadioButtonId();
                        rbChecked = findViewById(rbID);
                        String maps = rbChecked.getText().toString();
                        Uri uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + maps);
                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                        // Make the Intent explicit by setting the Google Maps package
                        mapIntent.setPackage("com.google.android.apps.maps");
                        // Attempt to start an activity that can handle the Intent
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(FindRoom.this, "Wybierz budynek!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FindRoom.this, "Sprawdź swoje połączenie z internetem.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkIfConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else
            return false;
    }
}
