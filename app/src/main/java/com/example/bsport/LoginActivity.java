package com.example.bsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsport.Model.Users;
import com.example.bsport.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    public static final Pattern  EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    public static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]+$");
    private Button LoginButton;
    public static EditText UserName;
    public static EditText UserPassword;
    private TextView NeedNewAccountLink ,ForgetPasswordLink;
    private DatabaseReference UserRef;
    private TextView AdminLink, NotAdminLink;
    private CheckBox chkBoxRememberMe;
    private String parentDbName = "Users";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference();

        initilizedFields();
        Paper.init(this);
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
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("התחבר כמנהל");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                NeedNewAccountLink.setVisibility(View.INVISIBLE);
                parentDbName = "Admin";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("התחבר");
                NotAdminLink.setVisibility(View.INVISIBLE);
                AdminLink.setVisibility(View.VISIBLE);
                NeedNewAccountLink.setVisibility(View.VISIBLE);
                parentDbName = "Users";
            }
        });

    }


    private void AllowUserToLogin() {

        final String userName = UserName.getText().toString();
        final String password = UserPassword.getText().toString();
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserNameKey, userName);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }
        else {
            Paper.book().write(Prevalent.UserNameKey, userName);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        } //TextUtils.isEmpty(userName)
        if(!Checking_username_notEmpty(userName)){
            Toast.makeText(this,"אנא הכנס שם משתמש",Toast.LENGTH_SHORT).show();
        }
        else if(!Valid_username_check(userName)){
            Toast.makeText(this,"אנא הכנס שם משמש חוקי - אותיות ומספרים בלבד",Toast.LENGTH_SHORT).show();
        }
        else if(!Checking_password_notEmpty(password)){
            Toast.makeText(this,"אנא הכנס סיסמה",Toast.LENGTH_SHORT).show();
        }
//        else if(!Valid_password_check(password)){
//            Toast.makeText(this,"הסיסמה צריכה להיות באורך 6 ומעלה",Toast.LENGTH_SHORT).show();
//        }
        else
        {

            loadingbar.setTitle("Sign In");
            loadingbar.setMessage("Please Wait..");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(parentDbName).child(userName).exists()){
                        Users usersData = dataSnapshot.child(parentDbName).child(userName).getValue(Users.class);
                        if(usersData.getUsername().equals(userName)){
                            if(usersData.getPassword().equals(password)){
                                if(parentDbName.equals("Admin")){
                                    Prevalent.setUserAdminKey("true");
                                    Paper.book().write(Prevalent.UserPasswordKey,userName);
                                    Paper.book().write(Prevalent.UserPasswordKey,password);
                                    loadingbar.dismiss();
                                    SendUserToLMainActivityAdmin();
                                }
                                else if(parentDbName.equals("Users")){
                                    Paper.book().write(Prevalent.UserPasswordKey,userName);
                                    Paper.book().write(Prevalent.UserPasswordKey,password);

                                    Prevalent.setUserAdminKey("false");
                                    loadingbar.dismiss();
                                    SendUserToLMainActivity();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this,"התחברות לא הצליחה, נסה שנית",Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "סיסמה לא נכונה", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "שם משתמש לא נמצא", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "השם משתמש " + userName + " לא נמצא במערכת ", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public static boolean Checking_username_notEmpty(CharSequence  userName) {
        return ! (userName=="") ;
    }
    public static boolean Valid_username_check(String userName) {
        return(USERNAME_PATTERN.matcher(userName).matches());
    }
    public static boolean Checking_password_notEmpty(CharSequence password) {
        return !(password == "");
    }
    public static boolean Valid_password_check(String password) {
        return !(password.length() < 6);
    }

    private void initilizedFields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        UserName = ( EditText) findViewById(R.id.login_userName);
        UserPassword = ( EditText) findViewById(R.id.login_password);
        ForgetPasswordLink = ( TextView) findViewById(R.id.forget_password_link);
        NeedNewAccountLink = ( TextView) findViewById(R.id.Sign_Up_link);
        loadingbar = new ProgressDialog(this);
        AdminLink = (TextView) findViewById(R.id.Im_Admin);
        NotAdminLink = (TextView) findViewById(R.id.Im_not_Admin);
        chkBoxRememberMe = (CheckBox) findViewById(R.id.checkRemember);

    }


    private void SendUserToLMainActivity() {

        Intent MainIntent = new Intent(  LoginActivity.this,HomeActivity.class );
        startActivity(MainIntent);
        finish();
    }
    private void SendUserToLMainActivityAdmin() {
        Intent MainIntent = new Intent(  LoginActivity.this,HomeActivity.class );
        startActivity(MainIntent);
        finish();
    }
    private void SendUserToLRegisterActivity() {
        Intent RegisterIntent = new Intent(  LoginActivity.this,RegisterActivity.class );
        startActivity(RegisterIntent);
    }

}