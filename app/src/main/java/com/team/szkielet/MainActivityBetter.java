package com.team.szkielet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.team.szkielet.event.Event;
import com.team.szkielet.event.Events;
import com.team.szkielet.event.Skarga;
import com.team.szkielet.login.SignIn;
import com.team.szkielet.quiz.QuizMainActivity;
import com.team.szkielet.rooms.FindRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MainActivityBetter extends AppCompatActivity {

    CardView cvPlanZajec, cvProwadzacy, cvWydarzenia, cvQuiz, cvSale, cvUstawienia;
    private long backPressedTime;
    TextView tvHello, tvAgain;
    private RequestQueue mQueue;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_better);


        //Tutaj wczytuje dane z json odnośnie eventów
        readJSONFromURL();

        tvHello = findViewById(R.id.tvHello);
        tvAgain = findViewById(R.id.tvAgain);

        Plany.czyMamyZapisaneDane = readingFromSharedPreferences();

        cvPlanZajec = findViewById(R.id.cvPlanZajec);
        cvPlanZajec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent;
                if (Plany.czyMamyZapisaneDane) {
                    intent = new Intent(MainActivityBetter.this, Plany.class);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            startActivity(intent);
                        }
                    }, 300);
                } else {
                    intent = new Intent(MainActivityBetter.this, PopUpInPlany.class);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            startActivity(intent);
                        }
                    }, 300);
                }
            }
        });

        cvProwadzacy = findViewById(R.id.cvProwadzacy);
        cvProwadzacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivityBetter.this, Prowadzacy.class);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }, 300);

            }
        });

        cvWydarzenia = findViewById(R.id.cvWydarzenia);
        cvWydarzenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(new Intent(MainActivityBetter.this, Events.class));
                    }
                }, 300);
            }
        });

        cvQuiz = findViewById(R.id.cvQuiz);
        cvQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivityBetter.this, "Witamy w Quizie", Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(MainActivityBetter.this, QuizMainActivity.class);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }, 300);
            }
        });

        cvSale = findViewById(R.id.cvSale);
        //buttonEffect(cvSale);
        cvSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(new Intent(MainActivityBetter.this, FindRoom.class));
                    }
                }, 300);
            }
        });

        cvUstawienia = findViewById(R.id.cvUstawienia);
        cvUstawienia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivityBetter.this, Settings.class);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }, 300);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivityBetter.this);
        //if (acct != null) {
//            String name = acct.getDisplayName();
            //String email = acct.getEmail();
            tvHello.setText("Cześć !!!");
            /*Toast toast = Toast.makeText(MainActivityBetter.this, "Jesteś zalogowany na " + email, Toast.LENGTH_SHORT);
            ((TextView)((LinearLayout)toast.getView()).getChildAt(0))
                    .setGravity(Gravity.CENTER_HORIZONTAL);
            toast.show();*/
      //  } else {
      //      Toast.makeText(MainActivityBetter.this, "Musisz się zalogować!", Toast.LENGTH_SHORT).show();
      //      Intent intent = new Intent(MainActivityBetter.this, SignIn.class);
      //      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      //      startActivity(intent);
      //  }
    }

    @Override
    protected void onRestart() {
        readingFromSharedPreferences();
        readJSONFromURL();
        super.onRestart();
    }

    private void readJSONFromURL() {
        Events.eventsList.clear();
        mQueue = Volley.newRequestQueue(this);
        jsonParseEventList();
        jsonParselistaSkarg();
        //Toast.makeText(MainActivityBetter.this, "done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(MainActivityBetter.this, "Press back again to quit app", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    boolean readingFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("UserInfo", 0);
        String name = sharedPref.getString("name", "");
        /*String stopien = sharedPref.getString("stopien", "");
        String kierunek = sharedPref.getString("kierunek", "");
        String rodzaj = sharedPref.getString("rodzaj", "");*/
        String rok = sharedPref.getString("rok", "");
        if (name.length() > 0 && !rok.equals("0")) {
            //tvHello.setText("Cześć " + name + "!!!\nStopień: " + stopien + " Kierunek: " + kierunek + "\nRodzaj: " + rodzaj + " Rok: " + rok);
            //tvHello.setText("Cześć " + name + "!!!");
            tvAgain.setText("Here we go again...");
            PopUpInPlany.wasSaved = true;
            return true;
        } else if (name.length() > 0 && rok.equals("0")) {
            //tvHello.setText("Cześć " + name + "!!!\nStopień: " + stopien + " Kierunek: " + kierunek + "\nRodzaj: " + rodzaj);
            //tvHello.setText("Cześć " + name + "!!!");
            tvAgain.setText("Here we go again...");
            PopUpInPlany.wasSaved = true;
            return true;
        }
        PopUpInPlany.wasSaved = false;
        return false;
    }

    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            boolean wasClicked = false;

            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0FCE1D9, PorterDuff.Mode.SRC_ATOP);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                v.setAlpha(0.7f);
                                v.invalidate();
                            }
                        }, 200);

                        wasClicked = true;
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (wasClicked) {
                            v.getBackground().setColorFilter(0xe0FCE1D9, PorterDuff.Mode.SRC_ATOP);
                            v.setAlpha(0.7f);
                            v.invalidate();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                v.getBackground().clearColorFilter();
                                v.setAlpha(1f);
                                v.invalidate();
                                wasClicked = false;
                            }
                        }, 350);

                        break;
                    }
                }
                return false;
            }
        });
    }

    private void jsonParseEventList() {
        String url = "https://api.jsonbin.io/b/5db729fbc24f785e64f6226c/latest";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("events");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                if (dateIsOK(employee)) {
                                    Events.eventsList.add(new Event(employee.getString("eventName"),
                                            employee.getString("description"),
                                            employee.getString("linkToEvent"),
                                            employee.getString("image"),
                                            employee.getInt("day"),
                                            employee.getInt("month"),
                                            employee.getInt("year"),
                                            employee.getString("userEmail")));
                                }
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

    private void jsonParselistaSkarg() {
        String url = "https://api.jsonbin.io/b/5e2afbd4593fd741856f8e2f/latest";
        Events.listaSkarg.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("skargi");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject skarga = jsonArray.getJSONObject(i);
                                Events.listaSkarg.add(new Skarga(skarga.getString("eventName"), skarga.getString("mail")));
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

    private boolean dateIsOK(JSONObject employee) throws JSONException {
        Date date = Calendar.getInstance().getTime();
        String daya = (String) DateFormat.format("dd", date);
        String monthNumber = (String) DateFormat.format("MM", date);
        String yeara = (String) DateFormat.format("yyyy", date);
        if (Integer.parseInt(yeara) == employee.getInt("year")) {
            if (Integer.parseInt(monthNumber) == employee.getInt("month")) {
                if (Integer.parseInt(daya) < employee.getInt("day") + 2) {
                    return true;
                } else {
                    return false;
                }
            } else if (Integer.parseInt(monthNumber) < employee.getInt("month")) {
                return true;
            } else {
                return false;
            }
        } else if (Integer.parseInt(yeara) < employee.getInt("year")) {
            return true;
        } else {
            return false;
        }
    }
}
