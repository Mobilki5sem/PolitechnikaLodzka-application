package com.team.szkielet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Aktualnosci extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktualnosci);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Aktualności");
        //actionBar.setIcon();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.plany) {
            //Toast.makeText(Aktualnosci.this, "Plany zajęć", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Aktualnosci.this, Plany.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.prowadzacy) {
            //Toast.makeText(Aktualnosci.this, "Prowadzący", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Aktualnosci.this, Prowadzacy.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.aktualnosci) {
            /*Toast.makeText(Aktualnosci.this, "Aktualności", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Aktualnosci.this, Aktualnosci.class);
            startActivity(intent);*/
        }
        else if(item.getItemId() == R.id.start) {
            //Toast.makeText(Aktualnosci.this, "Menu główne", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Aktualnosci.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Aktualnosci.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }
}
