package com.example.bsport;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class SportFacFragment extends Fragment {

    public static SportFacFragment newInstance() {
        return new SportFacFragment();
    }

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
    private CustomAdapter customAdapter;
    private List<Integer> FilterArray;
    private static int count=0;
    private int pos ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View sportsFacilitiesFragment = inflater.inflate(R.layout.sport_fac_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) sportsFacilitiesFragment.findViewById(R.id.recyclerView);
        CheckBox_Handi = (CheckBox) sportsFacilitiesFragment.findViewById(R.id.checkbox_Handi);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference countActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        EditText searchFac = (EditText) sportsFacilitiesFragment.findViewById(R.id.search_fac);

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
            JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAsset()));
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
                if (isCheckedWord(facDetail.getString("Type"))) {
                    FacTypes.add(facDetail.getString("Type"));
                    FacNames.add("מיקום: " + facDetail.getString("Name"));
                    if(!facDetail.getString("neighborho").equals("")) {
                        FacNeighborhoods.add("שכונה: " + facDetail.getString("neighborho"));
                    }
                    else{
                        FacNeighborhoods.add("שכונה לא זמינה");
                    }
                    if(!facDetail.getString("street").equals("")) {

                        FacStreets.add("רחוב: " + facDetail.getString("street"));
                    }
                    else{
                        FacStreets.add("רחוב לא זמין");
                    }
                    FacHandi.add(facDetail.getString("handicappe"));
                }
            }
            FilterArray = new ArrayList<Integer>(Collections.nCopies(FacTypes.size(), 1));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        filteredListName=  (ArrayList<String>)FacNames.clone();
        filteredListNeig =(ArrayList<String>)FacNeighborhoods.clone();
        filteredListStre=(ArrayList<String>)FacStreets.clone();
        filteredListType = (ArrayList<String>)FacTypes.clone();
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapter = new CustomAdapter(filteredListType, filteredListName, filteredListNeig, filteredListStre);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        initViews();
        CheckFilterArray();
        return sportsFacilitiesFragment;
    }


    private boolean isCheckedWord(String type) {
        return type.toLowerCase().contains("כדור".toLowerCase()) || type.toLowerCase().contains("מיני".toLowerCase()) ||
                type.toLowerCase().contains("שחיה".toLowerCase()) || type.toLowerCase().contains("בריכה".toLowerCase()) ||
                type.toLowerCase().contains("בריכת".toLowerCase()) || type.toLowerCase().contains("כושר".toLowerCase()) ||
                type.toLowerCase().contains("טניס".toLowerCase()) || type.toLowerCase().contains("אתלטיקה".toLowerCase()) ||
                type.toLowerCase().contains("פטנאק".toLowerCase()) || type.toLowerCase().contains("רגל".toLowerCase());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SportFacViewModel mViewModel = ViewModelProviders.of(this).get(SportFacViewModel.class);
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
