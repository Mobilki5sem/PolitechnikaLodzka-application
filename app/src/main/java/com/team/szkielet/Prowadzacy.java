package com.team.szkielet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prowadzacy);
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
        wwProw.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = wwProw.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

// set web view client ---------------------------------------------------------------
        wwProw.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println(wwProw.getUrl());
                wwProw.evaluateJavascript("javascript:window.document.getElementById(\"page-header\").style.display = \"none\";", null);
                wwProw.evaluateJavascript("javascript:window.document.getElementById(\"above-header\").style.display = \"none\";", null);
                //-----------------
                if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/users.php?search=")) {
                    wwProw.evaluateJavascript("javascript: function a() {\n" +
                            "                       if (document.getElementsByTagName(\"a\").length < 12)  {\n" +
                            "                          window.document.getElementsByClassName(\"search-user-container\")[0].style.display = \"none\"\n" +
                            "                          window.document.getElementById(\"page-header\").style.display = \"none\";\n" +
                            "                          window.document.getElementById(\"above-header\").style.display = \"none\";\n" +
                            "                          window.document.getElementsByClassName(\"userlist-header\")[0].getElementsByTagName(\"h3\")[0].textContent = \"\"\n" +
                            "                        }\n" +
                            "                       else {\n" +
                            "                         window.document.getElementsByTagName(\"a\")[12].click();\n" +
                            "                         }\n" +
                            "                    }\n" +
                            "                    a();", null);
                    /*
                    function a() {
                       if (document.getElementsByTagName("a").length < 12)  {
                          window.document.getElementsByClassName("search-user-container")[0].style.display = "none"
                          window.document.getElementById("page-header").style.display = "none";
                          window.document.getElementById("above-header").style.display = "none";
                          window.document.getElementsByClassName("userlist-header")[0].getElementsByTagName("h3")[0].textContent = ""
                        }
                       else {
                         window.document.getElementsByTagName("a")[12].click();
                         }
                    }
                    a();
                     */
                    pbProwadzacy.setVisibility(View.GONE);
                    wwProw.setVisibility(View.VISIBLE);
                }
                if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/profile.php?id")) {
                    Toast.makeText(Prowadzacy.this, "Znaleziono profil", Toast.LENGTH_SHORT).show();
                    pbProwadzacy.setVisibility(View.GONE);
                    wwProw.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (wwProw.getUrl().startsWith("https://adm.edu.p.lodz.pl/user/users.php?search=")) {
                    pbProwadzacy.setVisibility(View.VISIBLE);
                    wwProw.setVisibility(View.GONE);
                }
            }
        });

//on click listener ----------------------------------------------------------------
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.plany) {
            //Toast.makeText(Prowadzacy.this, "Plany zajęć", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Prowadzacy.this, Plany.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.prowadzacy) {
            /*Toast.makeText(Prowadzacy.this, "Prowadzący", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Prowadzacy.this, Prowadzacy.class);
            startActivity(intent);*/
        } else if (item.getItemId() == R.id.aktualnosci) {
            //Toast.makeText(Prowadzacy.this, "Aktualności", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Prowadzacy.this, Aktualnosci.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.start) {
            //Toast.makeText(Prowadzacy.this, "Menu główne", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Prowadzacy.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Prowadzacy.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }
}
