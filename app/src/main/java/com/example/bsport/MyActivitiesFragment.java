package com.example.bsport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsport.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;


public class MyActivitiesFragment extends Fragment {
    private static int size;

    private Button MyActivity;
    private DatabaseReference RootRef,CountActivityRef;
    static int count=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_activities, container, false);
        Button MyActivity = (Button) view.findViewById(R.id.new_activity);
        RootRef = FirebaseDatabase.getInstance().getReference();
        CountActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        MyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.layout.fragment_my_activities);

                final View mView = getLayoutInflater().inflate(R.layout.newactiviry, null);
                Button newActivitySub = (Button) mView.findViewById(R.id.submit_activity);
                builder.setView(mView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                newActivitySub.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        final String name = Paper.book().read(Prevalent.UserNameKey).toString();
                        final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());;
                        final EditText type = (EditText) mView.findViewById(R.id.game_type);
                        final EditText description=(EditText)mView.findViewById(R.id.activity_type);
                        final EditText number_of_players = (EditText) mView.findViewById(R.id.number_of_players);
                        final EditText game_date = (EditText) mView.findViewById(R.id.game_date);
                        HashMap<String,Object> userdataMap1 = new HashMap<>();
                        CountActivityRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    count= (int) dataSnapshot.getChildrenCount()+1;
                                }
                                else{
                                    count=1;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HashMap<String,Object> userdataMap = new HashMap<>();
                                    userdataMap.put("created_by", name);
                                    userdataMap.put("date_created", date);
                                    userdataMap.put("description", description.getText().toString());
                                    userdataMap.put("numbers_of_players",number_of_players.getText().toString());
                                    userdataMap.put("type", type.getText().toString());
                                    userdataMap.put("game_date", game_date.getText().toString());
                                    userdataMap.put("id",count);

                                    RootRef.child("Activities").child(String.valueOf(count)).updateChildren(userdataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        //Toast.makeText(HomeActivity.this,"Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                                       // loadingBar.dismiss();
                                                        //Intent intent = new Intent(  RegisterActivity.this,LoginActivity.class );
                                                        //startActivity(intent);
                                                        System.out.println("good");
                                                    }
                                                    else{
                                                        System.out.println("bad");
                                                    }
                                                }
                                            });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        dialog.cancel();
                    }

                });

            }
        });
        return view;
    }

}
