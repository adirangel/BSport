package com.example.bsport;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SportsFacilitiesFragment extends Fragment {

    // ArrayList for person names, email Id's and mobile numbers
    private ArrayList<String> FacTypes = new ArrayList<>();
    private ArrayList<String> FacNames = new ArrayList<>();
    private ArrayList<String> FacNeighborhoods = new ArrayList<>();
    private ArrayList<String> FacStreets = new ArrayList<>();
    private ArrayList<String> FacHandi = new ArrayList<>();
    private ArrayList<String> filteredListType = new ArrayList<>();
    private ArrayList<String> filteredListName = new ArrayList<>();
    private ArrayList<String> filteredListNeig = new ArrayList<>();
    private ArrayList<String> filteredListStre = new ArrayList<>();
    private CheckBox CheckBox_Handi;
    private String SearchTxt = "";
    private Boolean isCheckedBool = false;
    private View SportsFacilitiesFragment;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private LinearLayoutManager linearLayoutManager;

    List<Integer> FilterArray;
    public SportsFacilitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SportsFacilitiesFragment = inflater.inflate(R.layout.fragment_sports_facilities, container, false);
        recyclerView = (RecyclerView) SportsFacilitiesFragment.findViewById(R.id.recyclerView);
        CheckBox_Handi = (CheckBox) SportsFacilitiesFragment.findViewById(R.id.checkbox_Handi);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        EditText searchFac = (EditText) SportsFacilitiesFragment.findViewById(R.id.search_fac);
        searchFac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchTxt = s.toString();
                filter(s.toString());
                HandiFilter(isCheckedBool);
                CheckFilterArray();

            }
        });

        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray facArray = obj.getJSONArray("Facilities");
            // implement for loop for getting users list data
            FacTypes.clear();
            FacNames.clear();
            FacNeighborhoods.clear();
            FacStreets.clear();
            FacHandi.clear();
            for (int i = 0; i < facArray.length(); i++) {

                // create a JSONObject for fetching single user data
                JSONObject facDetail = facArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                FacTypes.add(facDetail.getString("Type"));
                FacNames.add("מיקום: " + facDetail.getString("Name"));
                FacNeighborhoods.add("שכונה: " + facDetail.getString("neighborho"));
                FacStreets.add("רחוב: " + facDetail.getString("street"));
                FacHandi.add(facDetail.getString("handicappe"));
            }
            FilterArray = new ArrayList<Integer>(Collections.nCopies(facArray.length(), 1));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapter = new CustomAdapter(FacTypes, FacNames, FacNeighborhoods, FacStreets);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        initViews();
        CheckFilterArray();

        return SportsFacilitiesFragment;
    }
    private void CheckFilterArray() {
        filteredListName.clear();
        filteredListNeig.clear();
        filteredListStre.clear();
        filteredListType.clear();
        for (int i = 0 ; i<FacTypes.size();i++) {
            if (FilterArray.get(i) == 1) {
                filteredListType.add(FacTypes.get(i));
                filteredListName.add(FacNames.get(i));
                filteredListNeig.add(FacNeighborhoods.get(i));
                filteredListStre.add(FacStreets.get(i));
            }
        }
        customAdapter.filterList(filteredListType,filteredListName,filteredListNeig,filteredListStre);
    }
    private void initViews() {
        CheckBox_Handi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                isCheckedBool = isChecked;
                HandiFilter(isChecked);
            }
        });
    }
    private void HandiFilter(boolean isChecked) {
        ArrayList<String> filteredListHandi = new ArrayList<>();
        if (isChecked) {
            CheckBox_Handi.setVisibility(View.VISIBLE);
            for (int i = 0 ; i<FacTypes.size();i++) {
                if ((FacHandi.get(i).contains("נגיש לנכים") || FacHandi.get(i).contains("כן")) && FilterArray.get(i) == 1)
                    FilterArray.set(i, 1);
                else
                    FilterArray.set(i,0);
                }

            }
        else {
            if (SearchTxt == "" )
                Collections.fill(FilterArray,1);
            else
                filter(SearchTxt);
        }

        CheckFilterArray();
        }



    private void filter(String text) {

        for (int i = 0 ; i<FacTypes.size();i++){
            if(FacTypes.get(i).contains(text))
                FilterArray.set(i, 1);
                else
                FilterArray.set(i,0);
//                filteredListType.add(FacTypes.get(i));
//                filteredListName.add(FacNames.get(i));
//                filteredListNeig.add(FacNeighborhoods.get(i));
//                filteredListStre.add(FacStreets.get(i));
            }


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
