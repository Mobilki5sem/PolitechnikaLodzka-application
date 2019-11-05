package com.team.szkielet.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team.szkielet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    private EditText etNazwaWydarzenia, etOpis, etLink;
    private CheckBox cbMamWydarzenie;
    private RadioGroup rgRodzaj;
    private Button btnAddEvent;
    private RadioButton rbChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etNazwaWydarzenia = findViewById(R.id.etNazwaWydarzenia);
        etOpis = findViewById(R.id.etOpis);
        etLink = findViewById(R.id.etLink);
        etLink.setVisibility(View.GONE);
        cbMamWydarzenie = findViewById(R.id.cbMamWydarzenie);
        cbMamWydarzenie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbMamWydarzenie.isChecked()) {
                    etLink.setVisibility(View.VISIBLE);
                } else {
                    etLink.setVisibility(View.GONE);
                }
            }
        });
        rgRodzaj = findViewById(R.id.rgRodzaj);
        btnAddEvent = findViewById(R.id.btnAddEvent);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] tab = checkIfAllWasFilled();

               /* if(rgRodzaj.getCheckedRadioButtonId() != -1)
                    Toast.makeText(AddEvent.this, whichImageUseToDescribeEvent(), Toast.LENGTH_LONG).show();*/
                if (tab[0] && tab[1] && tab[2] && tab[3]) {
                    int idObrazka = whichImageUseToDescribeEvent();
                    if(!cbMamWydarzenie.isChecked()){
                        Events.eventsList.add(new Event(
                                etNazwaWydarzenia.getText().toString(),
                                etOpis.getText().toString(),
                                "noLink",
                                idObrazka));
                    }
                    else {
                        Events.eventsList.add(new Event(
                                etNazwaWydarzenia.getText().toString(),
                                etOpis.getText().toString(),
                                etLink.getText().toString(),
                                idObrazka));
                    }

                        Toast.makeText(AddEvent.this, "Udało ci się dodać nowe wydarzenie!", Toast.LENGTH_LONG).show();
                        onBackPressed();

                        new Thread(new Runnable() {
                        public void run() {
                            try {
                                jsonPUT();
                                //Toast.makeText(AddEvent.this, getInformationFromPUT, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(AddEvent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(AddEvent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();
                }
                else {
                    Toast.makeText(AddEvent.this, "Wypełnij wszystkie potrzebne pola", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int whichImageUseToDescribeEvent() {
        if(rbChecked.getText().toString().equals("Spotkanie"))
            return R.drawable.ic_meeting_lol;
        else if(rbChecked.getText().toString().equals("Impreza"))
            return R.drawable.ic_confetti;
        else if(rbChecked.getText().toString().equals("Prezentacja"))
            return R.drawable.ic_classroom;
        else if(rbChecked.getText().toString().equals("Wydarzenie PŁ"))
            return R.drawable.ic_school;
        else {
            //default
            return R.drawable.ic_meeting;
        }
    }

    private boolean[] checkIfAllWasFilled() {
        boolean tab[] = {false, false, false, false};
        if (rgRodzaj.getCheckedRadioButtonId() != -1) {
            tab[2] = true;
            int rbID = rgRodzaj.getCheckedRadioButtonId();
            rbChecked = findViewById(rbID);
        }


        if(!etNazwaWydarzenia.getText().toString().equals(""))
            tab[0] = true;
        if(!etOpis.getText().toString().equals(""))
            tab[1] = true;
        if(cbMamWydarzenie.isChecked()) {
            if(!etLink.getText().toString().equals("")){
                tab[3] = true;
            }
        } else {
            tab[3] = true;
        }

        return tab;
    }

    private void jsonPUT() throws JSONException, IOException {
        JSONArray array = new JSONArray();
        ArrayList<Event> list = Events.eventsList;
        for(int i = 0; i < list.size(); i ++) {
            JSONObject postData = new JSONObject();
            postData.put("eventName", list.get(i).getEventName());
            postData.put("description", list.get(i).getDescription());
            postData.put("linkToEvent", list.get(i).getLinkToEvent());
            postData.put("image", list.get(i).getImage());
            array.put(postData);
        }
        JSONObject start = new JSONObject();
        start.put("events", array);

        URL url = new URL("https://api.jsonbin.io/b/5db729fbc24f785e64f6226c");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                out, "UTF-8"));
        writer.write(start.toString());
        writer.flush();
        System.err.println(connection.getResponseCode());
    }
}
