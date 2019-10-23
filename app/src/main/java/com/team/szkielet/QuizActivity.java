package com.team.szkielet;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView score_txt;
    TextView question_count_txt;
    TextView question_txt;
    Button btn_confirm_next;
    TextView time_txt;
    RadioGroup radio_group;
    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;
    //RadioButton radioButton4;
    private List<Question> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quiz");
        score_txt = findViewById(R.id.score_txt);
        question_count_txt = findViewById(R.id.question_count_txt);
        question_txt = findViewById(R.id.question_txt);
        btn_confirm_next = findViewById(R.id.btn_confirm_next);
        time_txt = findViewById(R.id.time_txt);
        radio_group = findViewById(R.id.radio_group);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        //jak wywolujemy pierwszy raz to stworzy to tez baze
        questionList = dbHelper.getAllQuestions();

    }
}
