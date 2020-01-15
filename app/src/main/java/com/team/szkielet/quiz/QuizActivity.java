package com.team.szkielet.quiz;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.szkielet.R;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";

    TextView score_txt;
    TextView question_count_txt;
    TextView question_txt;
    Button btn_confirm_next;
    TextView time_txt;
    RadioGroup radio_group;
    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RelativeLayout relativeLayout;
    RadioButton radioButton4;

    private static final long COUNTDOWN_TIME_START = 30000;
    //Podstawowy kolor dla rb

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd; //kolor dla czasomierza

    private CountDownTimer countDownTimer;
    private long timeLeft; //ile zostalo

    private List<Question> questionList;
    private int questionCounter; //ile pytan pokazalismy
    private int questionTotal; //ile wszystkich pytan
    private Question currentQuestion;
    private int score;
    private boolean answered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        score_txt = findViewById(R.id.score_txt);
        question_count_txt = findViewById(R.id.question_count_txt);
        question_txt = findViewById(R.id.question_txt);
        btn_confirm_next = findViewById(R.id.btn_confirm_next);
        time_txt = findViewById(R.id.time_txt);
        radio_group = findViewById(R.id.radio_group);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        relativeLayout = findViewById(R.id.relative_layout);

        textColorDefaultRb = radioButton.getTextColors();
        textColorDefaultCd = time_txt.getTextColors();

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        //jak wywolujemy pierwszy raz to stworzy to tez baze
        questionList = dbHelper.getAllQuestions();
        questionTotal = 10;
        Collections.shuffle(questionList);
        showNextQuestion(); //pokaz pierwsze pytanie, metoda wykona sie tylko raz, pozniej juz tylko onClickListener
        btn_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!answered) {  //jesli nie bylo jeszcze robione
                    if (radioButton.isChecked() || radioButton2.isChecked() || radioButton3.isChecked() || radioButton4.isChecked()) {
                        checkAnswer(); //jesli cos zaznaczyl to sprawdz odpowiedz
                    } else
                        Toast.makeText(QuizActivity.this, "Choose your Answer", Toast.LENGTH_SHORT).show();
                } else
                    showNextQuestion(); //jesli wezme NEXT lub Confirm to wywola sie ta metoda i pokaze nowe pytanie
            }
        });


    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel(); //zatrzymujemy timer

        RadioButton rbSelected = findViewById(radio_group.getCheckedRadioButtonId());
        int answerNr = radio_group.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            score = score + (int) (timeLeft / 1000);
            score_txt.setText("Score:" + score);
        }

        showSolution();
    }

    private void showSolution() {
        radioButton.setTextColor(Color.RED);
        radioButton2.setTextColor(Color.RED);
        radioButton3.setTextColor(Color.RED);
        radioButton4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                radioButton.setTextColor(Color.GREEN);
                break;
            case 2:
                radioButton2.setTextColor(Color.GREEN);
                break;
            case 3:
                radioButton3.setTextColor(Color.GREEN);
                break;
            case 4:
                radioButton4.setTextColor(Color.GREEN);
                break;
        }
        if (questionCounter < questionTotal) {
            btn_confirm_next.setText("NEXT");
        } else {
            btn_confirm_next.setText("Zakoncz quiz");
        }

    }

    private void showFinish() {
        btn_confirm_next.setText("Finish Quiz");
        question_txt.setTextColor(Color.WHITE);
        btn_confirm_next.setTextSize(35);
        question_txt.setText("BRAWO, DOTRWAŁES DO KONCA!");
        relativeLayout.setBackgroundColor(Color.BLACK);
        score_txt.setTextColor(Color.WHITE);
        radio_group.setVisibility(View.GONE);

        btn_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishQuiz();
            }
        });
    }

    private void showNextQuestion() {
        radioButton.setTextColor(textColorDefaultRb);
        radioButton2.setTextColor(textColorDefaultRb);
        radioButton3.setTextColor(textColorDefaultRb);
        radioButton4.setTextColor(textColorDefaultRb);
        //zeby guziki nie byly wybrane
        radio_group.clearCheck();

        if (questionCounter < questionTotal) {
            currentQuestion = questionList.get(questionCounter);
            question_txt.setText(currentQuestion.getQuestion());
            radioButton.setText(currentQuestion.getOption1());
            radioButton2.setText(currentQuestion.getOption2());
            radioButton3.setText(currentQuestion.getOption3());
            radioButton4.setText(currentQuestion.getOption4());
            questionCounter++;

            question_count_txt.setText("Question: " + questionCounter + "/" + questionTotal);

            answered = false;
            btn_confirm_next.setText("Confirm");

        } else {
            //Jak odpowiiem na wszystkie pytania
            time_txt.setVisibility(View.GONE);
            btn_confirm_next.setText("Wroc do menu quizu.");
            question_txt.setTextColor(Color.WHITE);
            btn_confirm_next.setTextSize(15);
            question_txt.setText("BRAWO, DOTRWAŁES DO KONCA!");
            relativeLayout.setBackgroundColor(Color.BLACK);
            score_txt.setTextColor(Color.WHITE);
            radio_group.setVisibility(View.GONE);

            btn_confirm_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishQuiz();
                }
            });
        }

        timeLeft = COUNTDOWN_TIME_START; //zaczynamy odmierzac od 30
        startCountDown();

    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished; //bedzie przypisywana wartosc ile zostalo
                updateTimeTxt(); //ta metoda wywola sie co sekunde i zaaktualizuje time_txt
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateTimeTxt();
                checkAnswer();
            }
        }.start();
    }

    private void updateTimeTxt() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeTxtFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        time_txt.setText(timeTxtFormatted);

        if (timeLeft < 10000) {
            time_txt.setTextColor(Color.RED);
        } else time_txt.setTextColor(textColorDefaultCd);
    }

    private void finishQuiz() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Toast.makeText(QuizActivity.this, "Przycisk COFNIJ spowodował zamknięcie QUIZu!!! ", Toast.LENGTH_SHORT).show();
    }

}
