package com.example.bsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private Button LoginButton, PhoneLoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccountLink ,ForgetPasswordLink;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);

        initilizedFields();
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLRegisterActivity();
            }
        });
    }

    private void initilizedFields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        LoginButton = (Button) findViewById(R.id.phone_login_button);
        UserEmail = ( EditText) findViewById(R.id.login_email);
        UserPassword = ( EditText) findViewById(R.id.login_password);
        ForgetPasswordLink = ( TextView) findViewById(R.id.forget_password_link);
        NeedNewAccountLink = ( TextView) findViewById(R.id.Sign_Up_link);
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
    private void SendUserToLRegisterActivity() {
        Intent RegisterIntent = new Intent(  LoginActivity.this,HomeActivity.class );
        startActivity(RegisterIntent);
    }


}
