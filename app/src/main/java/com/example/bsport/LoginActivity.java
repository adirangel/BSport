package com.example.bsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    public static final Pattern  EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    private Button LoginButton;
    public static EditText UserEmail;
    public static EditText UserPassword;
    private TextView NeedNewAccountLink ,ForgetPasswordLink;
    private DatabaseReference UserRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        initilizedFields();
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLRegisterActivity();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
    }

    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"אנא הכנס אימייל",Toast.LENGTH_SHORT).show();
        }

        else if(!EMAIL_PATTERN.matcher(email).matches()){
            Toast.makeText(this,"אנא הכנס אימייל תיקני",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"אנא הכנס סיסמה",Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6){
            Toast.makeText(this,"הסיסמה צריכה להיות באורך 6 ומעלה",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Sign In");
            loadingbar.setMessage("Please Wait..");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mAuth.signInWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                UserRef.child(currentUserId).child("device_token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    SendUserToLMainActivity();
                                                    Toast.makeText(LoginActivity.this,"Logged in Successful.",Toast.LENGTH_SHORT).show();
                                                    loadingbar.dismiss();
                                                }
                                            }
                                        });
                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this,"Error : "+ message,Toast.LENGTH_LONG).show();
                                loadingbar.dismiss();

                            }
                        }
                    });

        }
    }

    private void initilizedFields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        UserEmail = ( EditText) findViewById(R.id.login_email);
        UserPassword = ( EditText) findViewById(R.id.login_password);
        ForgetPasswordLink = ( TextView) findViewById(R.id.forget_password_link);
        NeedNewAccountLink = ( TextView) findViewById(R.id.Sign_Up_link);
        loadingbar = new ProgressDialog(this);

    }


    private void SendUserToLMainActivity() {
        Intent MainIntent = new Intent(  LoginActivity.this,HomeActivity.class );
        startActivity(MainIntent);
        finish();
    }
    private void SendUserToLRegisterActivity() {
        Intent RegisterIntent = new Intent(  LoginActivity.this,RegisterActivity.class );
        startActivity(RegisterIntent);
    }
}