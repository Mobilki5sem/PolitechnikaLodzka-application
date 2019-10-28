package com.team.szkielet.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.team.szkielet.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Events extends AppCompatActivity {

    private RecyclerView rvEvents;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RequestQueue mQueue;
    public ArrayList<Event> eventsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mQueue = Volley.newRequestQueue(this);
        jsonParseEventList();


        /*eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_meeting));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_calendar));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_events));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_meeting));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_calendar));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_events));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_meeting));
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_calendar));*/
        eventsList.add(new Event("Rozpocząecie roku", "No witam serdecznie będzie się działo. \nFTIMS 10.09.2019", "nolink", R.drawable.ic_events));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                rvEvents = findViewById(R.id.rvEvents);
                rvEvents.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(Events.this);
                adapter = new EventAdapter(eventsList);

                rvEvents.setLayoutManager(layoutManager);
                rvEvents.setAdapter(adapter);
            }
        }, 3000);

    }

    private void jsonParseEventList() {
        String url = "https://api.myjson.com/bins/11n9r8";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("events");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                if(employee.getString("image").equals("meeting")) {
                                    eventsList.add(new Event(employee.getString("eventName"),
                                            employee.getString("description"),
                                            employee.getString("linkToEvent"),
                                            R.drawable.ic_meeting));
                                    Toast.makeText(Events.this, "Event " + employee.getString("eventName"), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    eventsList.add(new Event(employee.getString("eventName"),
                                            employee.getString("description"),
                                            employee.getString("linkToEvent"),
                                            R.drawable.ic_calendar));
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
}
