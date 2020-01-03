package com.team.szkielet.quiz;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.team.szkielet.R;
import com.team.szkielet.event.AddEvent;
import com.team.szkielet.event.Events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

public class QuizAddQuestion extends AppCompatActivity {

    private RequestQueue mQueue;
    static public ArrayList<Question> questionsList = new ArrayList<>();
    public int SPRAWDZ = 0;
    EditText type_question;
    EditText type_answ_a;
    EditText type_answ_b;
    EditText type_answ_c;
    EditText type_answ_d;
    Button btn_send_question;
    //
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_add_question);

        readJSONFromURL(); //pobieram z jsona dane
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
                    //dataGET();

                } else {
                    final String typed_question = type_question.getText().toString();
                    final String ansA = type_answ_a.getText().toString();
                    final String ansB = type_answ_b.getText().toString();
                    final String ansC = type_answ_c.getText().toString();
                    final String ansD = type_answ_d.getText().toString();
                    //Toast.makeText(QuizAddQuestion.this, Integer.toString(SPRAWDZ), Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                sendPUT(typed_question, ansA, ansB, ansC, ansD);
                            } catch (JSONException e) {
                                Toast.makeText(QuizAddQuestion.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(QuizAddQuestion.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();
                    afterSendQuestion();
                }
            }
        });
    }

    public void sendPUT(final String question, final String ansA, final String ansB, final String ansC, final String ansD) throws JSONException, IOException {
        //SPRAWDZ++;
        JSONArray jsonarray = new JSONArray();

        URL url = new URL("https://api.jsonbin.io/b/5dc5302cc9b247772abc4e2d");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //dodaje to nowe pytanie do listy
        questionsList.add(new Question(question, ansA, ansB, ansC, ansD, 0));
        for (int i = 0; i < questionsList.size(); i++) {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("question", questionsList.get(i).getQuestion());
            jsonParam.put("answerA", questionsList.get(i).getOption1());
            jsonParam.put("answerB", questionsList.get(i).getOption2());
            jsonParam.put("answerC", questionsList.get(i).getOption3());
            jsonParam.put("answerD", questionsList.get(i).getOption4());
            jsonarray.put(jsonParam);
        }
        //
        JSONObject jsonCyk = new JSONObject();
        jsonCyk.put("questionsList", jsonarray);

        Log.i("JSON", jsonCyk.toString());
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
        os.writeBytes(jsonCyk.toString());
        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
        Log.i("MSG", conn.getResponseMessage());

        conn.disconnect();
    }

    //pobieramy z jsonbin
    private void jsonParse() {
        String url = "https://api.jsonbin.io/b/5dc5302cc9b247772abc4e2d/latest";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("questionsList");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonQuestionObject = jsonArray.getJSONObject(i);

                                questionsList.add(new Question(jsonQuestionObject.getString("question"),
                                        jsonQuestionObject.getString("answerA"),
                                        jsonQuestionObject.getString("answerB"),
                                        jsonQuestionObject.getString("answerC"),
                                        jsonQuestionObject.getString("answerD"), 0));


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

    private void readJSONFromURL() {
        questionsList.clear();
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    @Override
    protected void onRestart() {
        readJSONFromURL();
        super.onRestart();
    }

    private void afterSendQuestion() {
        finish();
        Intent intent = new Intent(QuizAddQuestion.this, QuizMainActivity.class);
        startActivity(intent);
        Toast.makeText(QuizAddQuestion.this, "Dziękujemy, pomyślnie przesłałeś swoją propozycję pytania!", Toast.LENGTH_SHORT).show();
    }

}