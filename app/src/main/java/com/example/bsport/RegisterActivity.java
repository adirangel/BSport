package com.example.bsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    public static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    public static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]+$");

    private Button CreateAccountButton;
    public static EditText UserEmail, UserName,UserPassword, Name,UserAge,UserAboutMyself; ;
    private TextView AlreadyHaveAccountLink;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();
        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        final String username,password,name,age,sport1,sport2,aboutMyself;
        final String email = UserEmail.getText().toString();
        username = UserName.getText().toString();
        password = UserPassword.getText().toString();
        name = Name.getText().toString();
        age = UserAge.getText().toString();
        /*sport1 = UserSport1.toString();
        sport2 = UserSport2.toString();*/
        aboutMyself = UserAboutMyself.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"אנא הכנס אימייל",Toast.LENGTH_SHORT).show();
        }
        else if(!EMAIL_PATTERN.matcher(email).matches()){
            Toast.makeText(this,"אנא הכנס אימייל תיקני",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"אנא הכנס שם משתמש בעל אותיות ומספרים בלבד",Toast.LENGTH_SHORT).show();
        }
        else if(!USERNAME_PATTERN.matcher(username).matches()){
            Toast.makeText(this,"אנא הכנס שם משתמש בעל אותיות ומספרים בלבד",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"אנא הכנס סיסמה",Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6){
            Toast.makeText(this,"אנא הכנס סיסמה ארוכה מ6 תווים",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we are creating new account for you...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!(dataSnapshot.child("Users").child(username).exists())){

                            HashMap<String,Object> userdataMap = new HashMap<>();
                            userdataMap.put("email", email);
                            userdataMap.put("username",username);
                            userdataMap.put("password", password);
                            userdataMap.put("name", name);
                            userdataMap.put("age", age);
                        /*userdataMap.put("sport1", sport1);
                        userdataMap.put("sport2", sport2);*/
                            userdataMap.put("aboutMyself", aboutMyself);

                            RootRef.child("Users").child(username).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this,"Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(  RegisterActivity.this,LoginActivity.class );
                                            startActivity(intent);
                                        }
                                        else{
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Network Error: Please try again after some time..", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"This " + email + " already exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this,"Please try again usign another email.",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void InitializeFields() {
        CreateAccountButton=(Button)findViewById(R.id.Register_button);
        UserEmail = (EditText)findViewById(R.id.register_email);
        UserName = (EditText)findViewById(R.id.register_UserName);
        UserPassword = (EditText)findViewById(R.id.register_Password);
        Name = (EditText)findViewById(R.id.register_name);
        UserAge = (EditText)findViewById(R.id.register_age);
        Spinner userSport1 = (Spinner) findViewById(R.id.register_sport1);
        Spinner userSport2 = (Spinner) findViewById(R.id.register_sport2);
        UserAboutMyself = (EditText)findViewById(R.id.register_about_myself);
        AlreadyHaveAccountLink = (TextView)findViewById(R.id.already_have_account_link);
        loadingBar = new ProgressDialog(this);
    }
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
}
