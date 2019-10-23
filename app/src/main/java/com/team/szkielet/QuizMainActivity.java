package com.team.szkielet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class QuizMainActivity extends AppCompatActivity {

    TextView start_txt;
    Button btn_start_quiz;
    TextView highscore_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quiz");
        start_txt = findViewById(R.id.start_txt);
        btn_start_quiz = findViewById(R.id.btn_start_quiz);
        highscore_txt = findViewById(R.id.highscore_txt);


        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

    }

    private void startQuiz(){
        Intent intent = new Intent(QuizMainActivity.this,QuizActivity.class);
        startActivity(intent);
    }
}
