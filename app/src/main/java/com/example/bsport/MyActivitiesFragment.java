package com.example.bsport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private Button MyActivity;
    private DatabaseReference RootRef,CountActivityRef;
    static int count=0,size=0;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View view;
    private ActivityAdapter activityAdapter;

    private String username = Paper.book().read(Prevalent.UserNameKey).toString();
    ArrayList<String> My_name_activity = new ArrayList<>();
    ArrayList<String> My_activity_type = new ArrayList<>();
    ArrayList<String> My_game_date = new ArrayList<>();
    ArrayList<String> My_location = new ArrayList<>();
    ArrayList<String> My_number_of_players = new ArrayList<>();
    ArrayList<String> My_date_created = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_activities, container, false);
        Button MyActivity = (Button) view.findViewById(R.id.new_activity);
        RootRef = FirebaseDatabase.getInstance().getReference();
        CountActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNewActivity);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        MyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.layout.fragment_my_activities);

                final View mView = getLayoutInflater().inflate(R.layout.newactiviry, null);
                Button newActivitySub = (Button) mView.findViewById(R.id.submit_activity);
                builder.setView(mView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                newActivitySub.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        final String name = username;
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

                                                        Toast.makeText(getActivity(), "הפעילות נוספה בהצלחה", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(getActivity(), "הפעילות לא התווספה בהצלחה- אנא נסה שנית", Toast.LENGTH_SHORT).show();
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

        CountActivityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("created_by").getValue().toString().equals(username))
                    {
                        My_name_activity.add("שם הפעילות: "+ds.child("description").getValue().toString());
                        My_activity_type.add("סוג הפעילות: "+ds.child("type").getValue().toString());
                        My_game_date.add("תאריך הפעילות: "+ ds.child("game_date").getValue().toString());
                        My_location.add("מיקום הפעילות: "+ds.child("location").getValue().toString());
                        My_number_of_players.add("מספר השחקנים: "+ds.child("numbers_of_players").getValue().toString());
                        My_date_created.add("תאריך יצירת הפעילות: "+ds.child("date_created").getValue().toString());
                    }


                }
                activityAdapter = new ActivityAdapter(My_name_activity,My_activity_type,My_game_date,My_location,My_number_of_players,My_date_created);
                recyclerView.setAdapter(activityAdapter); // set the Adapter to RecyclerView

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
