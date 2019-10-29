package com.team.szkielet.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.szkielet.MainActivityBetter;
import com.team.szkielet.Plany;
import com.team.szkielet.Prowadzacy;
import com.team.szkielet.R;

import java.util.ArrayList;

public class Events extends AppCompatActivity {

    private RecyclerView rvEvents;
    private RecyclerView.Adapter adapter;
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

        rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Events.this);
        adapter = new EventAdapter(eventsList);

        rvEvents.setLayoutManager(layoutManager);
        rvEvents.setAdapter(adapter);

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
        else if(item.getItemId() == R.id.aktualnosci) {
        }
        else if(item.getItemId() == R.id.start) {
            Intent intent = new Intent(Events.this, MainActivityBetter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
