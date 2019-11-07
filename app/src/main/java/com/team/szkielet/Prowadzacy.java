package com.team.szkielet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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


public class Prowadzacy extends AppCompatActivity {

    EditText etName;
    EditText etSurname;
    Button btnSearch;
    WebView wwProw;
    ProgressBar pbProwadzacy;
    String textContent;
    String imageURL;
    TextView textView;
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

                    pbProwadzacy.setVisibility(View.GONE);
                    wwProw.setVisibility(View.VISIBLE);
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
                    Toast.makeText(Prowadzacy.this, "Znaleziono profil", Toast.LENGTH_SHORT).show();
                    parseContent();
                }

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/users.php?search=")) {
                    pbProwadzacy.setVisibility(View.VISIBLE);
                    wwProw.setVisibility(View.GONE);
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
                pbProwadzacy.setVisibility(View.VISIBLE);
                wwProw.setVisibility(View.GONE);
                if (name.equalsIgnoreCase("Jakub") && surname.equalsIgnoreCase("Wąchała")) {
                    googleSearch = "https://m.lm.pl/media/foto/40321_8280.jpg";
                    pbProwadzacy.setVisibility(View.GONE);
                    wwProw.setVisibility(View.VISIBLE);
                } else {
                    googleSearch = "https://adm.edu.p.lodz.pl/user/users.php?search=" + name + "+" + surname;
                }
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
                pbProwadzacy.setVisibility(View.GONE);
                wwProw.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }


    private String parseTextFromURL(String text) {
        text = text.replace("\\n\\n\\n\\n\\n", "\\n");
        while (text.endsWith("\\n")) {
            text = text.substring(0, text.lastIndexOf("\\n"));
        }
        String tmp = text.substring(0, text.indexOf("Konsultacje cykliczne")).replace("\\n", "\n");
        int counter = countendl(text);
        System.out.println(counter + "TEXT:     ============================================" + text);
        for (int i = 0; i < counter; i++) {
            int startIndex = text.indexOf("\\n\\n\\n\\n") + 8;
            System.out.println("i = " + i + " " + startIndex);
            String[] id = text.substring(startIndex).replace("\\n", ";").split(";");
            String dzienTygodnia = id[0];
            String poczatek = id[1];
            String koniec = id[2];
            String semestr = id[3];
            String opis;
            if (text.substring(text.indexOf(id[3]) + id[3].length()).contains("\\n\\n\\n\\n"))
                opis = text.substring(text.indexOf(id[3]) + id[3].length(), text.lastIndexOf("\\n\\n\\n\\n")).replace("\\n", "\n");
            else
                opis = text.substring(text.indexOf(id[3]) + id[3].length()).replace("\\n", "\n");
            tmp += "\n";
            tmp += "Termin " + (i + 1) + ":\n\n";
            tmp += "Dzień tygodnia: " + dzienTygodnia + "\n";
            tmp += "Początek: " + poczatek + "\n";
            tmp += "Koniec: " + koniec + "\n";
            tmp += "Semestr: " + semestr + "\n";
            tmp += "Opis: " + opis + "\n";
            text = text.substring(text.indexOf("\\n\\n\\n\\n") + 8);
            System.out.println("i = " + i + " -----------------------------" + text);
        }
       // tmp += "\n\n\n\n\n\n";

        return tmp;
    }

    private int countendl(String input) {
        int index = input.indexOf("\\n\\n\\n\\n");
        int count = 0;
        while (index != -1) {
            count++;
            input = input.substring(index + 1);
            index = input.indexOf("\\n\\n\\n\\n");
        }
        return count;
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
        } else if (item.getItemId() == R.id.prowadzacy) {
        } else if (item.getItemId() == R.id.aktualnosci) {
            Intent intent = new Intent(Prowadzacy.this, Aktualnosci.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.start) {
            Intent intent = new Intent(Prowadzacy.this, MainActivityBetter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
