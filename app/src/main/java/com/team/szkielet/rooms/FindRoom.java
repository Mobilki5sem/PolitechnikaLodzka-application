package com.team.szkielet.rooms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
        tvInfo.setVisibility(View.GONE);
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
                        tvInfo.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvInfo.setVisibility(View.GONE);
                            }
                        }, 6000);
                    }
                } else {
                    Toast.makeText(FindRoom.this, "Najpierw wpisz salę do wyszukania", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String checkWhereIsRoom(String whichBuilding, String findRoom) {
        tvInfo.setTextColor(Color.BLACK);
        String ret = "";
        boolean boolCheck = true;
        int nr = 0;

        for(int i=0; i<findRoom.length();i++) {
            Boolean flag = Character.isDigit(findRoom.charAt(i));
            if(!flag)
                boolCheck = false;
        }

        if(boolCheck) {
            nr = Integer.parseInt(findRoom);
            //Toast.makeText(FindRoom.this, "Same cyferki", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(FindRoom.this, "Jest jakaś literka", Toast.LENGTH_SHORT).show();
        }

        if(whichBuilding.equals("B9")) {
            if(boolCheck) {
                if(findRoom.charAt(0) == '1' && findRoom.length() == 3){
                    if(nr == 160 || nr == 171) ret = "Sala znajduje się na I piętrze w B9 \nInstytut Matematyki";
                    else ret = "Sala prawdopodobnie znajduje się na I piętrze w B9";
                }
                else if(findRoom.charAt(0) == '2' && findRoom.length() == 3) ret = "Sala prawdopodobnie znajduje się na II piętrze w B9";
                else if(findRoom.charAt(0) == '3' && findRoom.length() == 3) ret = "Sala prawdopodobnie znajduje się na III piętrze w B9";
                else if(findRoom.charAt(0) == '4' && findRoom.length() == 3){
                    if((nr >= 432 && nr <= 435) || nr == 437 || nr == 441 || nr == 445)
                        ret = "Sala z zajęciami znajduje się na IV piętrze w B9 \nInstytut Informatyki";
                    else
                        ret = "Sala prawdopodobnie znajduje się na IV piętrze w B9";
                } else if(nr == 51 || nr == 52) ret = "Sala znajduje się na parterze w B9 \nInstytut Matematyki";
                else {
                    tvInfo.setTextColor(Color.RED);
                    ret = "Nie ma takiej sali w B9";
                }
            }
            else if(findRoom.equals("53a")) ret = "Sala znajduje się na parterze w B9 \nInstytut Matematyki";
            else if(findRoom.charAt(0) == 'F') {
                if(findRoom.equals("F2") || findRoom.equals("F3") || findRoom.equals("F4") || findRoom.equals("F5") || findRoom.equals("F6") ||
                        findRoom.equals("F6a") || findRoom.equals("F7") || findRoom.equals("F8") || findRoom.equals("F9") || findRoom.equals("F10")) {
                    if(findRoom.equals("F2") || findRoom.equals("F3") || findRoom.equals("F4") || findRoom.equals("F5") || findRoom.equals("F6") || findRoom.equals("F6a")){
                        ret = "Audytorium znajduje się na parterze w B9.";
                    } else {
                        ret = "Audytorium znajduje się na III piętrze w B9.";
                    }
                } else {
                    tvInfo.setTextColor(Color.RED);
                    ret = "Nie ma takiego audytorium w B9";
                }

            } else {
                tvInfo.setTextColor(Color.RED);
                ret = "Nie ma takiej sali w B9";
            }
        } else if(whichBuilding.equals("B19")) {
            if(nr == 201 || nr == 206 || (nr >= 208 && nr <= 211) || nr == 103 || nr == 104 || nr == 107 || nr == 108 || nr == 406 || nr == 407){
                if(findRoom.charAt(0) == '1') ret = "Sala znajduje się na I piętrze w CTI";
                else if(findRoom.charAt(0) == '2') ret = "Sala znajduje się na II piętrze w CTI";
                else if(findRoom.charAt(0) == '4') ret = "Sala znajduje się na IV piętrze w CTI";
                else {
                    tvInfo.setTextColor(Color.RED);
                    ret = "Nie ma takiej sali do zajęć w B14";
                }
            } else {
                tvInfo.setTextColor(Color.RED);
                ret = "Nie ma takiej sali do zajęć w CTI";
            }
        } else if(whichBuilding.equals("B14")) {
            if(findRoom.equals("0.4") || findRoom.contains("major")) ret ="Aula major znajduje się na parterze w B14";
            else if(findRoom.equals("0.8") || findRoom.contains("minor")) ret ="Aula minor znajduje się na parterze w B14";
            else if(findRoom.equals("0.17") || findRoom.contains("magica") || findRoom.contains("Magica")) ret ="Aula Magica znajduje się na parterze w B14";
            else if(findRoom.equals("0.27") || findRoom.equals("1.04") || findRoom.equals("1.05") || findRoom.equals("1.12")
                    || findRoom.equals("3.02") || findRoom.equals("3.03")) {
                if(findRoom.charAt(0) == '1') ret = "Audytorium projektowe / komputerowe znajduje się na \nI piętrze w B14 \nInstytut Fizyki";
                else if(findRoom.charAt(0) == '3') ret = "Audytorium projektowe / komputerowe znajduje się na \nIII piętrze w B14 \nInstytut Fizyki";
                else ret = "Audytorium projektowe / komputerowe znajduje się na parterze w B14 \nInstytut Fizyki";
            }
            else if((findRoom.charAt(0) == '1' && findRoom.charAt(1) == '.')) ret = "Sala prawdopodobnie znajduje się na I piętrze w B14";
            else if((findRoom.charAt(0) == '2' && findRoom.charAt(1) == '.')) ret = "Sala prawdopodobnie znajduje się na II piętrze w B14";
            else if(findRoom.equals("F1")) ret = "Audytorium znajduje się na parterze w B14";
            else {
                tvInfo.setTextColor(Color.RED);
                ret = "Nie ma takiej sali do zajęć w B14";
            }
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
