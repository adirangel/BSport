package com.example.bsport;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SportsFacilitiesFragment extends Fragment {

    // ArrayList for person names, email Id's and mobile numbers
    ArrayList<String> FacTypes = new ArrayList<>();
    ArrayList<String> FacNames = new ArrayList<>();
    ArrayList<String> FacNeighborhoods = new ArrayList<>();
    ArrayList<String> FacStreets = new ArrayList<>();
    private View SportsFacilitiesFragment;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public SportsFacilitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SportsFacilitiesFragment = inflater.inflate(R.layout.fragment_sports_facilities, container, false);
        recyclerView = (RecyclerView) SportsFacilitiesFragment.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

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
                FacTypes.add(facDetail.getString("Type"));
                FacNames.add(facDetail.getString("Name"));
                FacNeighborhoods.add(facDetail.getString("neighborho"));
                FacStreets.add(facDetail.getString("street"));
                // create a object for getting contact data from JSONObject
               // JSONObject contact = userDetail.getJSONObject("contact");
                // fetch mobile number and store it in arraylist
              //  mobileNumbers.add(contact.getString("mobile"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(FacTypes, FacNames, FacNeighborhoods, FacStreets);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

        return SportsFacilitiesFragment;
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
