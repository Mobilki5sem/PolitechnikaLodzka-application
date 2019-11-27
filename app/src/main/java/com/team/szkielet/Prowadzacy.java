package com.team.szkielet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.team.szkielet.event.Events;
import com.team.szkielet.quiz.QuizActivity;
import com.team.szkielet.quiz.QuizMainActivity;
import com.team.szkielet.rooms.FindRoom;

import java.util.Arrays;


public class Prowadzacy extends AppCompatActivity {

    EditText etName;
    EditText etSurname;
    Button btnSearch;
    WebView wwProw;
    ProgressBar pbProwadzacy;
    String textContent;
    String imageURL;
    String nameSurname;
    String academicDegree;
    TextView textView;
    TextView textViewName;
    TextView textViewSurname;
    TextView textViewDegree;
    ScrollView scrollViewProw;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prowadzacy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Prowadzący");
        //actionBar.setIcon();

        // ustawienia
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        btnSearch = findViewById(R.id.btnSearch);
        wwProw = findViewById(R.id.wwProw);
        pbProwadzacy = findViewById(R.id.pbProwadzacy);
        textView = findViewById(R.id.textView5);
        scrollViewProw = findViewById(R.id.scrollViewProw);
        textViewDegree = findViewById(R.id.textViewDegree);
        textViewSurname = findViewById(R.id.textViewSurname);
        textViewName = findViewById(R.id.textViewName);

        wwProw.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = wwProw.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

// set web view client ---------------------------------------------------------------
        wwProw.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println(wwProw.getUrl());
                wwProw.evaluateJavascript("javascript:window.document.getElementById(\"page-header-wrapper\").style.display = \"none\";", null);
                wwProw.evaluateJavascript("javascript:window.document.getElementById(\"page-footer\").style.display = \"none\";", null);
                //-----------------
                if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/users.php?search=")) {
                    wwProw.evaluateJavascript("javascript: function a() {\n" +
                            "                       if (document.getElementsByTagName(\"a\").length < 12)  {\n" +
                            "                          window.document.getElementsByClassName(\"search-user-container\")[0].style.display = \"none\"\n" +
                            "                          window.document.getElementsByClassName(\"userlist-header\")[0].getElementsByTagName(\"h3\")[0].textContent = \"\"\n" +
                            "                        }\n" +
                            "                       else {\n" +
                            "                         window.document.getElementsByTagName(\"a\")[12].click();\n" +
                            "                         }\n" +
                            "                    }\n" +
                            "                    a();", null);

                    setVisibleState();
                }
                if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/profile.php?id")) {
                    System.out.println("wwProw.getUrl().startsWith(\"https://adm.edu.p.lodz.pl/user/profile.php?id\"");
                    wwProw.evaluateJavascript("javascript: //chowa i return\n" +
                            "function getTextFromProfile() {\n" +
                            "if (document.getElementsByClassName(\"consultations\").length != 0) {\n" +
                            "\treturn document.getElementsByClassName(\"consultations\")[0].textContent;\n" +
                            "}\n" +
                            "return document.getElementsByClassName(\"about-me\")[0].textContent;\n" +
                            "}\n" +
                            "getTextFromProfile();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            textContent = value;
                        }
                    });
                    wwProw.evaluateJavascript("javascript: //zdjecie chlopa\n" +
                            "document.getElementsByClassName(\"profile-image\")[0].getElementsByTagName(\"img\")[0].getAttribute(\"src\")\n", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            imageURL = value;
                        }
                    });
                    wwProw.evaluateJavascript("javascript: //imie nazwisko tytul\n" +
                            "function getTextFromProfile() {\n" +
                            "  var imie = document.getElementsByClassName(\"profile-box\")[0].getElementsByTagName(\"h2\")[0].textContent; " +
                            "var tytul = document.getElementsByClassName(\"profile-box\")[0].getElementsByTagName(\"h4\")[0].textContent; " +
                            "return (imie + ';' + tytul);} getTextFromProfile();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            String valueWithout = value.replace("\"", "");
                            nameSurname = valueWithout.split(";")[0];
                            academicDegree = valueWithout.split(";")[1];
                            textViewDegree.setText(academicDegree);
                            textViewName.setText(nameSurname.split(" ")[0]);
                            textViewSurname.setText(nameSurname.split(" ")[1]);//.replace("-", "\n"));

                        }
                    });
                    Toast.makeText(Prowadzacy.this, "Znaleziono profil", Toast.LENGTH_SHORT).show();
                    parseContent();
                }

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/users.php?search=")) {
                setLoadingState();
                // }
            }
        });

//on click listener ----------------------------------------------------------------
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("JavascriptInterface")
            @Override
            public void onClick(View v) {
                etName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                etSurname.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String name = etName.getText().toString().trim();
                String surname = etSurname.getText().toString().trim();
                name = name.replace(" ", "");
                surname = surname.replace(" ", "");
                if (name.equals("") || surname.equals("")) {
                    Toast.makeText(Prowadzacy.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                    return;
                }
                String googleSearch;
                setLoadingState();
                googleSearch = "https://adm.edu.p.lodz.pl/user/users.php?search=" + name + "+" + surname;
                wwProw.loadUrl(googleSearch);

            }
        });

    }

    public void parseContent() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {

                wwProw.evaluateJavascript("javascript: x = document.getElementsByClassName(\"profile-image\")[0].getElementsByTagName(\"img\")[0].getAttribute(\"src\")\n" +
                        "window.open(x,\"_self\");", null);
                loadPicture();
                textContent = textContent.replace("\"", "");
                if (textContent.contains("Konsultacje cykliczne")) {
                    textContent = parseTextFromURL(textContent);
                } else {
                    textContent = textContent.replace("\\n", "\n");
                }
                textView.setText(textContent);
            }
        }, 500);
    }

    public void loadPicture() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                // wwProw.evaluateJavascript("javascript: x = document.getElementsByTagName(\"img\")[0].style.width = 200;", null);
                setVisibleState();
            }
        }, 1000);
    }


    private String parseTextFromURL(String text) {
        text = text.replace("\\n\\n\\n\\n\\n", "\\n");
        String tmp = text.substring(0, text.indexOf("Konsultacje cykliczne")).replace("\\n", "\n"); // opis przed tabelka
        try {
            text = text.substring(text.indexOf("Konsultacje cykliczne") + 27 + 55, text.indexOf("Konsultacje jednorazowe")); // ustawienie na dane
        } catch (Exception e) {
            text = text.substring(text.indexOf("Konsultacje cykliczne") + 27 + 55); // ustawienie na dane
        }
        while (text.endsWith("\\n")) {
            text = text.substring(0, text.lastIndexOf("\\n"));
        }
        String[] id = text.replace("\\n", ";").split(";");
        System.out.println(Arrays.toString(id));
        int i = 0;
        int termin = 1;
        while (true) {
            System.out.println("tablica: " + Arrays.toString(id));
            String dzienTygodnia = id[i];
            String poczatek = id[i + 1];
            String koniec = id[i + 2];
            String semestr = id[i + 3];
            String opis = "";
            if (id.length % 7 != 4 && id.length % 7 != 5) // przypadek gdy ktos wstawia enter w opis, np a.n.
            {
                StringBuilder opisTmp = new StringBuilder();
                for (int j = i + 4; j < id.length; j++) {
                    opisTmp.append(id[j]);
                }
                opis = opisTmp.toString();
            } else //pozostali
            {
                try {
                    if (!id[i + 4].equals(""))  // jest jeszcze opis
                    {
                        opis = id[i + 4];
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }

            tmp += "\n\nTermin " + (termin++) + ":\n";
            tmp += dzienTygodnia + " " + poczatek + " - " + koniec + ", semestr: " + semestr + "\n";
            if (!opis.equals(""))
                tmp += "Dodatkowy opis: " + opis;
            /*System.out.println(dzienTygodnia);
            System.out.println(poczatek);
            System.out.println(koniec);
            System.out.println(semestr);
            System.out.println(opis);*/
            if (id.length <= i + 5 || (id.length % 7 != 4 && id.length % 7 != 5)) // nie ma juz nic
                break;
            i += 7;
        }
        return tmp;
    }

    public void setVisibleState() {
        pbProwadzacy.setVisibility(View.GONE);
        wwProw.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        textViewName.setVisibility(View.VISIBLE);
        textViewSurname.setVisibility(View.VISIBLE);
        textViewDegree.setVisibility(View.VISIBLE);
    }

    public void setLoadingState() {
        pbProwadzacy.setVisibility(View.VISIBLE);
        wwProw.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        textViewName.setVisibility(View.GONE);
        textViewSurname.setVisibility(View.GONE);
        textViewDegree.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.plany) {
            Intent intent = new Intent(Prowadzacy.this, Plany.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.wydarzenia) {
            Intent intent = new Intent(Prowadzacy.this, Events.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.start) {
            Intent intent = new Intent(Prowadzacy.this, MainActivityBetter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (item.getItemId() == R.id.quiz) {
            Intent intent = new Intent(Prowadzacy.this, QuizMainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.sale) {
            Intent intent = new Intent(Prowadzacy.this, FindRoom.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Prowadzacy.this, MainActivityBetter.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }
}
