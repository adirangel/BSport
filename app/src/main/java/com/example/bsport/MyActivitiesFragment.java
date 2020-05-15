package com.example.bsport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;


public class MyActivitiesFragment extends Fragment {

    private DatabaseReference RootRef,CountActivityRef;
    private static int count=0;
    private RecyclerView recyclerView;
    private View view;
    private ActivityAdapter activityAdapter;
    private String username = Paper.book().read(Prevalent.UserNameKey).toString();
    private ArrayList<String> My_name_activity = new ArrayList<>();
    private ArrayList<String> My_activity_type = new ArrayList<>();
    private ArrayList<String> My_game_date = new ArrayList<>();
    private ArrayList<String> My_location = new ArrayList<>();
    private ArrayList<String> My_number_of_players = new ArrayList<>();
    private ArrayList<String> My_date_created = new ArrayList<>();
    private ArrayList<String> My_id = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_activities, container, false);
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


        CountActivityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                My_name_activity.clear();
                My_activity_type.clear();
                My_game_date.clear();
                My_location.clear();
                My_number_of_players.clear();
                My_date_created.clear();
                My_id.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("created_by").getValue().toString().equals(username))
                    {
                        My_name_activity.add("שם הפעילות: "+ds.child("description").getValue().toString());
                        My_activity_type.add("סוג הפעילות: "+ds.child("type").getValue().toString());
                        My_game_date.add("תאריך הפעילות: "+ ds.child("game_date").getValue().toString());
                        My_location.add("מיקום הפעילות: "+ds.child("location").getValue().toString());
                        My_number_of_players.add("מספר השחקנים: "+ds.child("numbers_of_players").getValue().toString());
                        My_date_created.add("תאריך יצירת הפעילות: "+ds.child("date_created").getValue().toString());
                        My_id.add(ds.child("id").getValue().toString());

                    }
                }
                activityAdapter = new ActivityAdapter(My_name_activity,My_activity_type,My_game_date,My_location,My_number_of_players,My_date_created,My_id);
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
