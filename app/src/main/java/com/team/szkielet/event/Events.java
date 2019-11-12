package com.team.szkielet.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.szkielet.MainActivityBetter;
import com.team.szkielet.Plany;
import com.team.szkielet.Prowadzacy;
import com.team.szkielet.R;
import com.team.szkielet.quiz.QuizMainActivity;
import com.team.szkielet.rooms.FindRoom;

import java.util.ArrayList;
import java.util.Collections;

public class Events extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fabtnAdd;

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
                if(!urlReally.equals("noLink")){
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
        if(item.getItemId() == R.id.plany) {
            Intent intent = new Intent(Events.this, Plany.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.prowadzacy) {
            Intent intent = new Intent(Events.this, Prowadzacy.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.wydarzenia) {
        }
        else if(item.getItemId() == R.id.start) {
            Intent intent = new Intent(Events.this, MainActivityBetter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.quiz) {
            Intent intent = new Intent(Events.this, QuizMainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.sale) {
            Intent intent = new Intent(Events.this, FindRoom.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
