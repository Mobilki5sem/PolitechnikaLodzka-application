package com.team.szkielet.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.szkielet.MainActivityBetter;
import com.team.szkielet.Plany;
import com.team.szkielet.Prowadzacy;
import com.team.szkielet.R;
import com.team.szkielet.login.SignIn;
import com.team.szkielet.quiz.QuizMainActivity;
import com.team.szkielet.rooms.FindRoom;

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
import java.util.Collections;
import java.util.Date;

public class Events extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fabtnAdd;
    private RequestQueue mQueue;
    static public ArrayList<Event> eventsList = new ArrayList<>();
    static public ArrayList<Skarga> listaSkarg = new ArrayList<>();
    GoogleSignInClient mGoogleSignInClient;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Events.this);
        if(acct != null) {
            userEmail = acct.getEmail();
        } else {
            Toast.makeText(Events.this, "Musisz się zalogować na konto google aby móc przeglądać nadchodzące wydarzenia!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Events.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


        fabtnAdd = findViewById(R.id.fabtnAdd);
        fabtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Events.this, AddEvent.class));
            }
        });
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;
        if(connected){
            addRecyclerView();
        } else {
            Toast.makeText(Events.this, "Musisz połączyć się z internetem!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    private void readJSONFromURL() {
        Events.eventsList.clear();
        mQueue = Volley.newRequestQueue(this);
        jsonParseEventList();
    }

    private void addRecyclerView() {
        rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Events.this);
        Collections.sort(eventsList);
        adapter = new EventAdapter(eventsList);

        rvEvents.setLayoutManager(layoutManager);
        rvEvents.setAdapter(adapter);

        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String urlReally = eventsList.get(position).getLinkToEvent();
                if (!urlReally.equals("noLink")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try {
                        i.setData(Uri.parse(urlReally));
                        startActivity(i);
                    } catch (Exception ex) {
                        Toast.makeText(Events.this, "Link jest nieprawidłowy.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Events.this, "Nie ma linku do wydarzenia.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClicked(final int position) {
                final String eventName = eventsList.get(position).getEventName();
                AlertDialog.Builder builder = new AlertDialog.Builder(Events.this);
                builder.setCancelable(true);
                builder.setTitle("Potwierdzenie");
                builder.setMessage("Czy aby na pewno chcesz zgłosić wydarzenie \"" + eventName + "\"? Uważasz, że jest nieodpowiednie, bądź obraża innych? Kliknij przycisk 'ZGŁOŚ'.");
                builder.setPositiveButton("Zgłoś",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!checkIfYouDontHaveSkargaOnThisEvent(eventName)) {
                                    listaSkarg.add(new Skarga(eventName,userEmail));
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                jsonPUTSkargi();
                                            } catch (JSONException e) {
                                                Toast.makeText(Events.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            } catch (IOException e) {
                                                Toast.makeText(Events.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }).start();
                                    Toast.makeText(Events.this, "Dziękujemy za informację o problematycznym wydarzeniu.", Toast.LENGTH_SHORT).show();
                                    checkHowManySkargiEventHave(position, eventName);
                                } else {
                                    Toast.makeText(Events.this, "To wydarzenie zostało już przez Ciebie zgłoszone!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Events.this, "Zgłoszenie zostało anulowane.", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    @Override
    protected void onResume() {
        addRecyclerView();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        addRecyclerView();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Events.this, MainActivityBetter.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.plany) {
            Intent intent = new Intent(Events.this, Plany.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.prowadzacy) {
            Intent intent = new Intent(Events.this, Prowadzacy.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.wydarzenia) {
        } else if (item.getItemId() == R.id.start) {
            Intent intent = new Intent(Events.this, MainActivityBetter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (item.getItemId() == R.id.quiz) {
            Intent intent = new Intent(Events.this, QuizMainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.sale) {
            Intent intent = new Intent(Events.this, FindRoom.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void jsonParseEventList() {
        String url = "https://api.jsonbin.io/b/5db729fbc24f785e64f6226c/latest";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("events");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                if(dateIsOK(employee)) {
                                    Events.eventsList.add(new Event(employee.getString("eventName"),
                                            employee.getString("description"),
                                            employee.getString("linkToEvent"),
                                            employee.getString("image"),
                                            employee.getInt("day"),
                                            employee.getInt("month"),
                                            employee.getInt("year"),
                                            employee.getString("userEmail")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private boolean dateIsOK(JSONObject employee) throws JSONException {
        Date date = Calendar.getInstance().getTime();
        String daya          = (String) DateFormat.format("dd",   date);
        String monthNumber  = (String) DateFormat.format("MM",   date);
        String yeara         = (String) DateFormat.format("yyyy", date);
        if(Integer.parseInt(yeara) == employee.getInt("year")) {
            if(Integer.parseInt(monthNumber) == employee.getInt("month")) {
                if(Integer.parseInt(daya) < employee.getInt("day") + 2) {
                    return true;
                } else {
                    return false;
                }
            } else if(Integer.parseInt(monthNumber) < employee.getInt("month")) {
                return true;
            } else {
                return false;
            }
        } else if (Integer.parseInt(yeara) < employee.getInt("year")) {
            return true;
        } else {
            return false;
        }
    }

    public static void jsonPUTSkargi() throws JSONException, IOException {
        JSONArray array = new JSONArray();
        ArrayList<Skarga> list = Events.listaSkarg;
        for (int i = 0; i < list.size(); i++) {
            JSONObject postData = new JSONObject();
            postData.put("eventName", list.get(i).getEventName());
            postData.put("mail", list.get(i).getMail());
            array.put(postData);
        }
        JSONObject start = new JSONObject();
        start.put("skargi", array);

        URL url = new URL("https://api.jsonbin.io/b/5e2afbd4593fd741856f8e2f");
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

    public boolean checkIfYouDontHaveSkargaOnThisEvent(String eventSkarga){
        for(int i = 0; i < listaSkarg.size(); i++) {
            if(listaSkarg.get(i).getMail().equals(userEmail)) {
                if(listaSkarg.get(i).getEventName().equals(eventSkarga)){
                    return true;
                }
            }
        }
        return false;
    }

    public void checkHowManySkargiEventHave(int position, String eventSkarga) {
        int howManySkargi = 0;
        for(int i = 0; i < listaSkarg.size(); i++) {
            if (listaSkarg.get(i).getEventName().equals(eventSkarga)) {
                howManySkargi++;
            }
        }
        if(howManySkargi > 10) {
            //usuwamy z EventList event o pozycji position
            eventsList.remove(position);
            //wysyłamy PUT z aktualizacją na binJSON
            new Thread(new Runnable() {
                public void run() {
                    try {
                        jsonPUT();
                    } catch (JSONException e) {
                        Toast.makeText(Events.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(Events.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
            Toast.makeText(Events.this, "Wydarzenie zostanie usunięte!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Events.this, "Aby wydarzenie zostało usunięte potrzeba jeszcze " + (10 - howManySkargi) + " zgłoszeń.", Toast.LENGTH_LONG).show();
        }
    }

    public static void jsonPUT() throws JSONException, IOException {
        JSONArray array = new JSONArray();
        ArrayList<Event> list = Events.eventsList;
        for (int i = 0; i < list.size(); i++) {
            JSONObject postData = new JSONObject();
            postData.put("eventName", list.get(i).getEventName());
            postData.put("description", list.get(i).getDescription());
            postData.put("linkToEvent", list.get(i).getLinkToEvent());
            postData.put("image", list.get(i).getImage());
            postData.put("day", list.get(i).getDay());
            postData.put("month", list.get(i).getMonth());
            postData.put("year", list.get(i).getYear());
            postData.put("userEmail", list.get(i).getUserEmail());

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