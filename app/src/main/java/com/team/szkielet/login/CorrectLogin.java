package com.team.szkielet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.team.szkielet.MainActivityBetter;
import com.team.szkielet.R;

import org.w3c.dom.Text;

import java.nio.charset.CodingErrorAction;

public class CorrectLogin extends AppCompatActivity {

    TextView tvName, tvEmail, tvID;
    ImageView ivPhoto;
    Button btnSignOut, btnGoMain;
    GoogleSignInClient mGoogleSignInClient;
    public static Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_login);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvID = findViewById(R.id.tvID);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnSignOut = findViewById(R.id.btnSignOut);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CorrectLogin.this);
        if(acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personID = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            person = new Person(personName, personEmail, personID, personPhoto);

            tvName.setText(personName);
            tvEmail.setText(personEmail);
            tvID.setText(personID);
            Glide.with(CorrectLogin.this).load(personPhoto).into(ivPhoto);
        }

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btnGoMain = findViewById(R.id.btnGoMain);
        btnGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CorrectLogin.this, MainActivityBetter.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CorrectLogin.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CorrectLogin.this, SignIn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
                    }
                });
    }
}
