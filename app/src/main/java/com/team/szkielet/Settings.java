package com.team.szkielet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.team.szkielet.login.SignIn;

import java.util.Set;

public class Settings extends AppCompatActivity {

    Button btnStudent, btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnStudent = findViewById(R.id.btnStudent);
        btnGoogle = findViewById(R.id.btnGoogle);

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, PopUpInPlany.class);
                startActivity(intent);
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}
