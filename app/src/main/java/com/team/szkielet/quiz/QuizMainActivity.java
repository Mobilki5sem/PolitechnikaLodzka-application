package com.team.szkielet.quiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.team.szkielet.MainActivityBetter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.team.szkielet.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class QuizMainActivity extends AppCompatActivity {
    private int counterForhighscoreFromGoogle = 0;
    private String userEmail;
    GoogleSignInClient mGoogleSignInClient;

    TextView yourPlace_txt;
    TextView start_txt;
    Button btn_start_quiz;
    TextView highscore_txt;
    Button btn_add_quest;
    private static final int REQUEST_CODE_QUIZ = 1;

    private RequestQueue mQueue;
    static public ArrayList<Highscore> highscoreList = new ArrayList<>();

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(QuizMainActivity.this);
        userEmail = acct.getEmail();

        readJSONFromURL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main_activity);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Quiz");
        start_txt = findViewById(R.id.start_txt);
        btn_start_quiz = findViewById(R.id.btn_start_quiz);
        highscore_txt = findViewById(R.id.highscore_txt);
        yourPlace_txt = findViewById(R.id.yourPlace);
        btn_add_quest = findViewById(R.id.btn_add_quest);
        //loadHighscore();
        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkIfHighscoreUpdateNeeded(userEmail);
                        startQuiz();
                    }
                }, 500);
            }
        });
        btn_add_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizMainActivity.this, QuizAddQuestion.class);
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yourPlace_txt.setTextColor(Color.RED);
                //yourPlace_txt.setText("YOUR SCORE OR HIGHER GOT " + showYourRanking() + "%" + " OF PLAYERS");
                yourPlace_txt.setText("YOUR RANKING: " + showYourRanking());
            }
        }, 2000);

    }

    private void startQuiz() {
        Intent intent = new Intent(QuizMainActivity.this, QuizActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    //  private void loadHighscore() {
    //     SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    //       highscore =
//                //prefs.getInt(KEY_HIGHSCORE, 0);
    //      highscore_txt.setText("Highscore: " + highscore);
    //   }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        highscore_txt.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }

    //wyslij
    public void sendPUT(int highscore) throws JSONException, IOException {
        JSONArray jsonarray = new JSONArray();

        URL url = new URL("https://api.jsonbin.io/b/5e051657e3eeeb70eb972f3d");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        highscoreList.add(new Highscore(highscore, userEmail));
        for (int i = 0; i < highscoreList.size(); i++) {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("highscore", highscoreList.get(i).getHighscore());
            jsonParam.put("email", highscoreList.get(i).getEmail());
            jsonarray.put(jsonParam);
        }

        JSONObject jsonCyk = new JSONObject();
        jsonCyk.put("highscoreList", jsonarray);

        Log.i("JSON", jsonCyk.toString());
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        os.writeBytes(jsonCyk.toString());
        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
        Log.i("MSG", conn.getResponseMessage());

        conn.disconnect();
    }

    private void jsonParse() {
        String url = "https://api.jsonbin.io/b/5e051657e3eeeb70eb972f3d/latest";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            highscoreList.clear();
                            JSONArray jsonArray = response.getJSONArray("highscoreList");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonQuestionObject = jsonArray.getJSONObject(i);

                                highscoreList.add(new Highscore(jsonQuestionObject.getInt("highscore"),
                                        jsonQuestionObject.getString("email")));
                            }
                            counterForhighscoreFromGoogle++;
                            if (counterForhighscoreFromGoogle == 1) setHighscoreFromGoogle();
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
        highscoreList.clear();
        mQueue = Volley.newRequestQueue(this);
        jsonParse();

    }

    @Override
    protected void onRestart() {
        readJSONFromURL();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfHighscoreUpdateNeeded(userEmail);
            }
        }, 2000);
        super.onRestart();
    }

    public void checkIfHighscoreUpdateNeeded(String email) {
        int cnt = 0;
        for (int i = 0; i < highscoreList.size(); i++) {
            if (highscoreList.get(i).getEmail().equals(email)) cnt++;
        }


        for (int i = 0; i < highscoreList.size(); i++) {
            if (highscoreList.get(i).getEmail().equals(email)) {
                if (highscoreList.get(i).getHighscore() < highscore) {
                    //wtedy usun stare z listy
                    highscoreList.remove(highscoreList.get(i));
                    sendData();
                }
            }
        }
        //jesli jeszcze nie ma w bazie
        if (cnt == 0) sendData();
    }

    public void sendData() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    sendPUT(highscore);
                } catch (JSONException e) {
                    Toast.makeText(QuizMainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(QuizMainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    public String showYourRanking() {
        int numberOfUsers = highscoreList.size();
        int counter = 1;

        for (int i = 0; i < highscoreList.size(); i++) {
            if (highscoreList.get(i).getHighscore() > highscore && !(highscoreList.get(i).getEmail().equals(userEmail))) {
                counter++;
            }
        }
        //double place = (counter / numberOfUsers) * 100;
        //String place2 = String.valueOf(place);
        String place = counter + "/" + numberOfUsers;
        return place;

    }

    public void setHighscoreFromGoogle() {
        for (int i = 0; i < highscoreList.size(); i++) {
            if (highscoreList.get(i).getEmail().equals(userEmail)) {
                highscore = highscoreList.get(i).getHighscore();
                highscore_txt.setText("Highscore: " + highscore);
            }
        }
    }

    @Override
    protected void onResume() {
        readJSONFromURL();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfHighscoreUpdateNeeded(userEmail);
            }
        }, 2000);
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //Intent intent = new Intent(QuizMainActivity.this, MainActivityBetter.class);
        //startActivity(intent);
    }
}
