package com.team.szkielet.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private EditText etNazwaWydarzenia, etOpis, etLink;
    private CheckBox cbMamWydarzenie;
    private RadioGroup rgRodzaj;
    private Button btnAddEvent, btnCheckLink;
    private RadioButton rbChecked;
    private CalendarView calendar;

    private boolean ifLinkWasChecked = false;
    private int Mday, Mmonth, Myear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        calendar = findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                //Toast.makeText(AddEvent.this, "" + day + "." + month + 1 + "." + year, Toast.LENGTH_LONG).show();
                Mday = day;
                Mmonth = month + 1;
                Myear = year;
            }
        });

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
                    btnCheckLink.setVisibility(View.VISIBLE);
                } else {
                    etLink.setVisibility(View.GONE);
                    btnCheckLink.setVisibility(View.GONE);
                }
            }
        });
        rgRodzaj = findViewById(R.id.rgRodzaj);
        btnCheckLink = findViewById(R.id.btnCheckLink);
        btnCheckLink.setVisibility(View.GONE);
        btnCheckLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlReally = etLink.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    i.setData(Uri.parse(urlReally));
                    startActivity(i);
                    Toast.makeText(AddEvent.this, "Link jest prawidłowy.", Toast.LENGTH_LONG).show();
                    ifLinkWasChecked = true;
                } catch (Exception ex) {
                    Toast.makeText(AddEvent.this, "Link jest nieprawidłowy.", Toast.LENGTH_LONG).show();
                    ifLinkWasChecked = false;
                }


            }
        });

        btnAddEvent = findViewById(R.id.btnAddEvent);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] tab = checkIfAllWasFilled();

               /* if(rgRodzaj.getCheckedRadioButtonId() != -1)
                    Toast.makeText(AddEvent.this, whichImageUseToDescribeEvent(), Toast.LENGTH_LONG).show();*/
                if (tab[0] && tab[1] && tab[2] && tab[3] && tab[4]) {
                    String idObrazka = whichImageUseToDescribeEvent();
                    if(!cbMamWydarzenie.isChecked()){
                        Events.eventsList.add(new Event(
                                etNazwaWydarzenia.getText().toString(),
                                etOpis.getText().toString(),
                                "noLink",
                                idObrazka,
                                Mday,
                                Mmonth,
                                Myear));
                    }
                    else {
                        Events.eventsList.add(new Event(
                                etNazwaWydarzenia.getText().toString(),
                                etOpis.getText().toString(),
                                etLink.getText().toString(),
                                idObrazka,
                                Mday,
                                Mmonth,
                                Myear));
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
            }
        });
    }

    private String whichImageUseToDescribeEvent() {
        return rbChecked.getText().toString();
    }

    private boolean[] checkIfAllWasFilled() {
        Toast toast;
        //0 - nazwaWydarzeni, 1 - Opis, 2 - rodzajWydarzenia, 3 - link, 4 - data
        boolean tab[] = {false, false, false, false, false};

        if(!etNazwaWydarzenia.getText().toString().equals(""))
            tab[0] = true;
        else {
            Toast.makeText(AddEvent.this, "Dodaj nazwę wydarzenia.", Toast.LENGTH_SHORT).show();
        }
        if(!etOpis.getText().toString().equals(""))
            tab[1] = true;
        else {
            Toast.makeText(AddEvent.this, "Dodaj opis wydarzenia.", Toast.LENGTH_SHORT).show();
        }
        if (rgRodzaj.getCheckedRadioButtonId() != -1) {
            tab[2] = true;
            int rbID = rgRodzaj.getCheckedRadioButtonId();
            rbChecked = findViewById(rbID);
        } else {
            Toast.makeText(AddEvent.this, "Wybierz rodzaj wydarzenia.", Toast.LENGTH_SHORT).show();
        }
        if(cbMamWydarzenie.isChecked()) {
            if(!etLink.getText().toString().equals("")){
                if(ifLinkWasChecked)
                    tab[3] = true;
                else {
                    toast = Toast.makeText(AddEvent.this, "Sprawdź poprawność linku powyższym przyciskiem lub zrezygnuj z niego.", Toast.LENGTH_LONG);
                    ((TextView)((LinearLayout)toast.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.show();
                }

            } else {
                toast = Toast.makeText(AddEvent.this, "Sprawdź poprawność linku powyższym przyciskiem lub zrezygnuj z niego.", Toast.LENGTH_LONG);
                ((TextView)((LinearLayout)toast.getView()).getChildAt(0))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
            }
        } else {
            tab[3] = true;
        }
        Date date = Calendar.getInstance().getTime();
        String daya          = (String) DateFormat.format("dd",   date);
        String monthNumber  = (String) DateFormat.format("MM",   date);
        String yeara         = (String) DateFormat.format("yyyy", date);
        if(Integer.parseInt(yeara) == Myear) {
            if(Integer.parseInt(monthNumber) == Mmonth) {
                if(Integer.parseInt(daya) < Mday) {
                    tab[4] = true;
                } else {
                    Toast.makeText(AddEvent.this, "Dodaj prawidłową datę.", Toast.LENGTH_SHORT).show();
                }
            } else if(Integer.parseInt(monthNumber) < Mmonth) {
                tab[4] = true;
            } else {
                Toast.makeText(AddEvent.this, "Dodaj prawidłową datę.", Toast.LENGTH_SHORT).show();
            }
        } else if (Integer.parseInt(yeara) < Myear) {
            tab[4] = true;
        } else {
            Toast.makeText(AddEvent.this, "Dodaj prawidłową datę.", Toast.LENGTH_SHORT).show();
        }

        return tab;
    }

    public static void jsonPUT() throws JSONException, IOException {
        JSONArray array = new JSONArray();
        ArrayList<Event> list = Events.eventsList;
        for(int i = 0; i < list.size(); i ++) {
            JSONObject postData = new JSONObject();
            postData.put("eventName", list.get(i).getEventName());
            postData.put("description", list.get(i).getDescription());
            postData.put("linkToEvent", list.get(i).getLinkToEvent());
            postData.put("image", list.get(i).getImage());
            postData.put("day", list.get(i).getDay());
            postData.put("month", list.get(i).getMonth());
            postData.put("year", list.get(i).getYear());

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

    public static boolean exists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
