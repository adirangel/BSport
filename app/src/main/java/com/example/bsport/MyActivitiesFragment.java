package com.example.bsport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;


public class MyActivitiesFragment extends Fragment {

    private Button MyActivity;
    private DatabaseReference RootRef,CountActivityRef;
    private static int count=0;
    private RecyclerView recyclerView;
    private View view;
    private ActivityAdapter activityAdapter;
    private AutoCompleteTextView acTextView;
    private String username = Paper.book().read(Prevalent.UserNameKey).toString();
    private ArrayList<String> My_name_activity = new ArrayList<>();
    private ArrayList<String> My_activity_type = new ArrayList<>();
    private ArrayList<String> My_game_date = new ArrayList<>();
    private ArrayList<String> My_location = new ArrayList<>();
    private ArrayList<String> My_number_of_players = new ArrayList<>();
    private ArrayList<String> My_date_created = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_activities, container, false);
        Button MyActivity = (Button) view.findViewById(R.id.new_activity);
        RootRef = FirebaseDatabase.getInstance().getReference();
        CountActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNewActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray facArray = obj.getJSONArray("Facilities");
            // implement for loop for getting users list data
            for (int i = 0; i < facArray.length(); i++) {

                // create a JSONObject for fetching single user data
                JSONObject facDetail = facArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                if(!(locations.contains("רחוב: " + facDetail.getString("street"))) && !facDetail.getString("street").equals(""))
                    locations.add("רחוב: " + facDetail.getString("street"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setLayoutManager(linearLayoutManager);

        MyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View mView = getLayoutInflater().inflate(R.layout.newactiviry, null);
                Button newActivitySub = (Button) mView.findViewById(R.id.submit_activity);
                builder.setView(mView);
                acTextView = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView_activity);
                ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.select_dialog_item,locations);
                acTextView.setThreshold(1);
                acTextView.setAdapter(adapter);
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
                        final AutoCompleteTextView location = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView_activity);

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
                                    userdataMap.put("location",location.getText().toString());

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

        CountActivityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                My_name_activity.clear();
                My_activity_type.clear();
                My_game_date.clear();
                My_location.clear();
                My_number_of_players.clear();
                My_date_created.clear();
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

    private String loadJSONFromAsset() {
        String Context = null;
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("dataBaseSport.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
