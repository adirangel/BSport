package com.example.bsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolBar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("BSport");
    }

    @Override
    protected void onStart() {
            super.onStart();
            if (currentUser == null)
            {
                SendUserToLoginActivity();
            }
    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent( HomeActivity.this , LoginActivity.class);
        startActivity(LoginIntent);
    }
}
