package com.example.bsport;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bsport.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;


public class PrivateAreaFragment extends Fragment {
    private String UserAge, UserFullName, User_about_myself,UserEmail;
    private TextView CurrentUser;
    private DatabaseReference RootRef;
    private View theCurrentUser;
    private String username;
    private TextView CurrentUserEmail,CurrentUserAge,CurrentUserDescription;
    private Button ChangePass,ChangePersonalDetails;

    public PrivateAreaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        username = Paper.book().read(Prevalent.UserNameKey).toString();
        // Inflate the layout for this fragment
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        theCurrentUser = inflater.inflate(R.layout.fragment_private_area, container, false);
        ChangePass = (Button) theCurrentUser.findViewById(R.id.ChangePass);
        ChangePersonalDetails = (Button) theCurrentUser.findViewById(R.id.ChangeDetails);
        //TextView CurrentUserEmail;
        //CurrentUserEmail = (TextView)theCurrentUser.findViewById(R.id.UserEmail);
        //CurrentUserEmail.setText(Paper.book().read(Prevalent.));
        CurrentUser = (TextView)theCurrentUser.findViewById(R.id.UserFullName);
        CurrentUserEmail = (TextView)theCurrentUser.findViewById(R.id.UserEmail);
        CurrentUserAge = (TextView)theCurrentUser.findViewById(R.id.UserAge);
        CurrentUserDescription = (TextView)theCurrentUser.findViewById(R.id.User_about_myself);

        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.child("username").getValue().toString().equals(username)) {
                        UserAge = ds.child("age").getValue().toString();
                        UserFullName = ds.child("name").getValue().toString();
                        UserEmail = ds.child("email").getValue().toString();
                        User_about_myself = ds.child("aboutMyself").getValue().toString();
                    }
                }

                CurrentUser.setText(UserFullName);
                CurrentUserAge.setText(UserAge);
                CurrentUserEmail.setText(UserEmail);
                CurrentUserDescription.setText(User_about_myself);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View mView = getLayoutInflater().inflate(R.layout.change_password, null);
                Button newActivitySub = (Button) mView.findViewById(R.id.submit_activity1);
                builder.setView(mView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                newActivitySub.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        final String password1,password2;
                        password1 = ((EditText)mView.findViewById(R.id.change_password1)).getText().toString();
                        password2 = ((EditText)mView.findViewById(R.id.change_password2)).getText().toString();
                        Query Userquery = RootRef.child("Users").orderByChild(username).equalTo(username);
                        if(password1.equals(password2)){

                            Map<String, Object> map = new HashMap<>();
                            map.put("password", password2);
                            RootRef.child(username).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(getActivity(), "הסיסמא הוחלפה בהצלחה !", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else{
                            Toast.makeText(getActivity(), "הסיסמאות אינם שוות, נסה שנית.", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }
        });
        ChangePersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View mView = getLayoutInflater().inflate(R.layout.change_personal_details, null);
                Button newActivitySub = (Button) mView.findViewById(R.id.submit_activity2);
                builder.setView(mView);
                final AlertDialog dialog1 = builder.create();
                dialog1.show();
                newActivitySub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email,about,age;
                        email = ((EditText)mView.findViewById(R.id.change_email)).getText().toString();
                        about = ((EditText)mView.findViewById(R.id.change_about)).getText().toString();
                        age = ((EditText)mView.findViewById(R.id.change_age)).getText().toString();
                        Query Userquery = RootRef.child("Users").orderByChild(username).equalTo(username);
                        if(!email.equals("")){

                            Map<String, Object> map = new HashMap<>();
                            map.put("email", email);
                            RootRef.child(username).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(getActivity(), "החלפת פרטיים אישיים בוצעה בהצלחה", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        if(!about.equals("")){
                            Map<String, Object> map = new HashMap<>();
                            map.put("aboutMyself",about);
                            RootRef.child(username).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(getActivity(), "החלפת פרטיים אישיים בוצעה בהצלחה", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        if(!age.equals("")){
                            Map<String, Object> map = new HashMap<>();
                            map.put("age",age);
                            RootRef.child(username).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(getActivity(), "החלפת פרטיים אישיים בוצעה בהצלחה", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    }
                });

            }
        });

        return theCurrentUser;
    }

}
