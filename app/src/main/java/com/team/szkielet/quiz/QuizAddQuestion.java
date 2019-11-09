package com.team.szkielet.quiz;

import androidx.appcompat.app.AppCompatActivity;
import com.team.szkielet.R;
import com.team.szkielet.event.AddEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class QuizAddQuestion extends AppCompatActivity {

    public int SPRAWDZ = 0;
    EditText type_question;
    EditText type_answ_a;
    EditText type_answ_b;
    EditText type_answ_c;
    EditText type_answ_d;
    Button btn_send_question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_add_question);

        type_question = findViewById(R.id.type_question); //wpisz pytanie
        type_answ_a = findViewById(R.id.type_answ_a);
        type_answ_b = findViewById(R.id.type_answ_b);
        type_answ_c = findViewById(R.id.type_answ_c);
        type_answ_d = findViewById(R.id.type_answ_d);
        btn_send_question = findViewById(R.id.btn_send_question); // wcisnij aby wyslac pytanie

        btn_send_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type_question.getText().toString().isEmpty() || type_answ_a.getText().toString().isEmpty() || type_answ_b.getText().toString().isEmpty() ||
                        type_answ_c.getText().toString().isEmpty() || type_answ_d.getText().toString().isEmpty()) {
                    Toast.makeText(QuizAddQuestion.this, "Wypelnij wszystkie pola!", Toast.LENGTH_SHORT).show();
                }
                else{
                final String typed_question = type_question.getText().toString();
                final String ansA = type_answ_a.getText().toString();
                final String ansB = type_answ_b.getText().toString();
                final String ansC = type_answ_c.getText().toString();
                final String ansD = type_answ_d.getText().toString();
                Toast.makeText(QuizAddQuestion.this, Integer.toString(SPRAWDZ), Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            sendPost(typed_question, ansA, ansB, ansC, ansD);
                            //Toast.makeText(AddEvent.this, getInformationFromPUT, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(QuizAddQuestion.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(QuizAddQuestion.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }

            }
        });

    }

    public void sendPost(final String question, final String ansA, final String ansB, final String ansC, final String ansD) throws JSONException, IOException {
                    SPRAWDZ++;
                    URL url = new URL("https://api.jsonbin.io/b/5dc5302cc9b247772abc4e2d");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("question", question);
                    jsonParam.put("answerA", ansA);
                    jsonParam.put("answerB", ansB);
                    jsonParam.put("answerC", ansC);
                    jsonParam.put("answerD", ansD);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
    }
}

