package com.example.bsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null)
        {
            SendUserToLMainActivity();
        }
    }

    private void SendUserToLMainActivity() {
        Intent MainIntent = new Intent(  LoginActivity.this,HomeActivity.class );
        startActivity(MainIntent);
    }

}
