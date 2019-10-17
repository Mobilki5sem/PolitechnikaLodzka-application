package com.team.szkielet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Plany extends AppCompatActivity {

    private Button btnDownload;
    //DownloadManager downloadManager;
    boolean czyMamyZapisaneDane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plany);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Plany zajęć");
        //actionBar.setIcon();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if(!czyMamyZapisaneDane) {
            Intent intent = new Intent(Plany.this, PopUpInPlany.class);
            startActivity(intent);
        }

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://drive.google.com/uc?export=download&id=0B6U4g2ILG3VWQ0c5STRNRVRGalU");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);*/

                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/0B6U4g2ILG3VWQ0c5STRNRVRGalU/view?usp=sharing"));
                //startActivity(browserIntent);

                WebView webView = (WebView) findViewById(R.id.wvPDF);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("https://drive.google.com/file/d/17rS8PT2h1yhER5ZdunkjJJaaWazPX2fo/view?usp=sharing");
                //webView.loadUrl("https://ftims.edu.p.lodz.pl/mod/resource/view.php?id=44697");
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
        if(item.getItemId() == R.id.plany) {}
        else if(item.getItemId() == R.id.prowadzacy) {
            Intent intent = new Intent(Plany.this, Prowadzacy.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.aktualnosci) {
            Intent intent = new Intent(Plany.this, Aktualnosci.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.start) {
            Intent intent = new Intent(Plany.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Plany.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }
}
