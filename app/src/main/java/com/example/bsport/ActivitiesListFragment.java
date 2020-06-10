package com.example.bsport;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.ArrayList;



public class ActivitiesListFragment extends Fragment {
    private static int count=0;
    private RecyclerView recyclerView;
    private ListOfActivitiesAdapter list_of_activity_adapter;
    private ArrayList<String> My_Created_by = new ArrayList<>();
    private ArrayList<String> My_name_activity = new ArrayList<>();
    private ArrayList<String> My_game_date = new ArrayList<>();
    private ArrayList<String> My_location = new ArrayList<>();
    private ArrayList<Integer> Arr_image = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> type_activity = new ArrayList<>();
    private ArrayList<String> num_of_players = new ArrayList<>();
    private ArrayList<String> My_id = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_list, container, false);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference countActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNewActivity);
        create_Arr_Image(Arr_image);
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

        countActivityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                My_Created_by.clear();
                My_name_activity.clear();
                My_game_date.clear();
                My_id.clear();
                My_location.clear();
                num_of_players.clear();
                type_activity.clear();


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    My_Created_by.add(ds.child("created_by").getValue().toString());
                    My_name_activity.add(ds.child("description").getValue().toString());
                    My_game_date.add(ds.child("game_date").getValue().toString());
                    My_id.add(ds.child("id").getValue().toString());
                    My_location.add(ds.child("location").getValue().toString());
                    num_of_players.add(ds.child("numbers_of_players").getValue().toString());
                    type_activity.add(ds.child("type").getValue().toString());


                }
                list_of_activity_adapter = new ListOfActivitiesAdapter(My_Created_by, My_name_activity, My_game_date, My_id, My_location, num_of_players, type_activity, Arr_image);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                recyclerView.setAdapter(list_of_activity_adapter); // set the Adapter to RecyclerView

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void create_Arr_Image(ArrayList<Integer> arr_image) {
        arr_image.add(R.drawable.basketball);
        arr_image.add(R.drawable.football);
        arr_image.add(R.drawable.tennis);
        arr_image.add(R.drawable.pool);
        arr_image.add(R.drawable.athletics);
        arr_image.add(R.drawable.fitness);
        arr_image.add(R.drawable.patnak);
        arr_image.add(R.drawable.mini);
        arr_image.add(R.drawable.judo);
        arr_image.add(R.drawable.logo);
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
