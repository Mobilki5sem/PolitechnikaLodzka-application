package com.team.szkielet.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.szkielet.MainActivityBetter;
import com.team.szkielet.Plany;
import com.team.szkielet.Prowadzacy;
import com.team.szkielet.R;
import com.team.szkielet.quiz.QuizMainActivity;
import com.team.szkielet.rooms.FindRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Events extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fabtnAdd;
    //private SwipeRefreshLayout idSwipe;
    private RequestQueue mQueue;

    static public ArrayList<Event> eventsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Wydarzenia");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        fabtnAdd = findViewById(R.id.fabtnAdd);
        fabtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Events.this, AddEvent.class));
            }
        });
        addRecyclerView();

//        final Handler ha = new Handler();
//            ha.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //call function
//                    //jsonParseEventList();
//                    readJSONFromURL();
//                    ha.postDelayed(this, 5000);
//                }
//            }, 10000);

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
        });
    }

    @Override
    protected void onResume() {
        //Toast.makeText(Events.this, "onResume", Toast.LENGTH_SHORT).show();
        addRecyclerView();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        //Toast.makeText(Events.this, "onRestart", Toast.LENGTH_SHORT).show();
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
}
