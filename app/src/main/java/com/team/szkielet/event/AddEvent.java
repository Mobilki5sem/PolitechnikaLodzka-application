package com.team.szkielet.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team.szkielet.R;

public class AddEvent extends AppCompatActivity {

    private EditText etNazwaWydarzenia, etOpis;
    private RadioGroup rgRodzaj;
    private Button btnAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etNazwaWydarzenia = findViewById(R.id.etNazwaWydarzenia);
        etOpis = findViewById(R.id.etOpis);
        rgRodzaj = findViewById(R.id.rgRodzaj);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(!etNazwaWydarzenia.getText().equals("") && !etOpis.getText().equals("")) {
                    Toast.makeText(AddEvent.this, "Dodanie wydarzenie.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }
}
