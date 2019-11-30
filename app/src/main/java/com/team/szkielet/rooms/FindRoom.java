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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team.szkielet.R;

import java.util.ArrayList;
import java.util.List;

public class FindRoom extends AppCompatActivity {

    private RadioGroup rgBudynek;
    private RadioButton rbChecked;
    private EditText etSala;
    private Button btnPokazNaMapie, btnZnajdzSale;
    private TextView tvInfo;
    private LinearLayout ivBudynek;
    private Uri uri;
    private String checkedRB = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);

        etSala = findViewById(R.id.etSala);
        etSala.clearFocus();
        btnPokazNaMapie = findViewById(R.id.btnPokazNaMapie);
        btnZnajdzSale = findViewById(R.id.btnZnajdzSale);
        tvInfo = findViewById(R.id.tvInfo);
        ivBudynek = findViewById(R.id.ivBudynek);
        ivBudynek.setBackgroundResource(R.drawable.elko);
        rgBudynek = findViewById(R.id.rgBudynek);

        rgBudynek.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                if(checkedID == R.id.rbB9) {
                    ivBudynek.setBackgroundResource(R.drawable.lodex_nice);
                    uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + "Wólczańska 215, 90-001 Łódź");
                    checkedRB = "B9";
                } else if(checkedID == R.id.rbB14) {
                    ivBudynek.setBackgroundResource(R.drawable.fizyka);
                    uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + "Instytut Fizyki Politechniki Łódzkiej Łódź");
                    checkedRB = "B14";
                } else if(checkedID == R.id.rbB19) {
                    ivBudynek.setBackgroundResource(R.drawable.ctipl);
                    uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + "Centrum Technologii Informatycznych Łódź");
                    checkedRB = "B19";
                }
            }
        });


        btnPokazNaMapie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfConnected()) {
                    if (rgBudynek.getCheckedRadioButtonId() != -1) {
                        int rbID = rgBudynek.getCheckedRadioButtonId();
                        rbChecked = findViewById(rbID);
                        String maps = rbChecked.getText().toString();
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

        btnZnajdzSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etSala.getText().length() > 0) {
                    if(checkedRB == "") {
                        Toast.makeText(FindRoom.this, "Najpierw wybierz budynek", Toast.LENGTH_SHORT).show();
                    } else {
                        tvInfo.setText(checkWhereIsRoom(checkedRB, etSala.getText().toString()));
                    }
                } else {
                    Toast.makeText(FindRoom.this, "Najpierw wpisz salę do wyszukania", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String checkWhereIsRoom(String whichBuilding, String findRoom) {
        String ret = "";
        int nr = Integer.parseInt(findRoom);
        if(whichBuilding == "B9") {
            if(findRoom.charAt(0) == '1' && findRoom.length() == 3) ret = "Sala prawdopodobnie znajduje się na I piętrze w B9";
            else if(findRoom.charAt(0) == '2' && findRoom.length() == 3) ret = "Sala prawdopodobnie znajduje się na II piętrze w B9";
            else if(findRoom.charAt(0) == '3' && findRoom.length() == 3) ret = "Sala prawdopodobnie znajduje się na III piętrze w B9";
            else if(findRoom.charAt(0) == '4' && findRoom.length() == 3){
                if((nr >= 432 && nr <= 435) || nr == 437 || nr == 441 || nr == 445)
                    ret = "Sala z zajęciami znajduje się na IV piętrze w B9";
                else
                    ret = "Sala prawdopodobnie znajduje się na IV piętrze w B9";
            }
            else if(findRoom.charAt(0) == 'F') {
                ret = "No to tutaj mamy jazde bez trzymanki na sale z F";
            }
            else ret = "Nie ma takiej sali w B9";
        } else if(whichBuilding == "B19") {
            if(nr == 201 || nr == 206 || (nr >= 208 && nr <= 211) || nr == 103 || nr == 104 || nr == 107 || nr == 108 || nr == 406 || nr == 407){
                if(findRoom.charAt(0) == '1') ret = "Sala znajduje się na I piętrze w B19";
                else if(findRoom.charAt(0) == '2') ret = "Sala znajduje się na II piętrze w B19";
                else if(findRoom.charAt(0) == '4') ret = "Sala znajduje się na IV piętrze w B19";
            } else ret = "Nie ma takiej sali do zajęć w B19";
        } else if(whichBuilding == "B14") {

        }
        return ret;
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
