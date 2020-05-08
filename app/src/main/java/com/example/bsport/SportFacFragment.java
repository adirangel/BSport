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

    private SportFacViewModel mViewModel;

    public static SportFacFragment newInstance() {
        return new SportFacFragment();
    }
    public static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+$");
    public static final Pattern DATE_PATTERN = Pattern.compile("^([0-2][0-9]|(3)[0-1])(\\-)(((0)[0-9])|((1)[0-2]))(\\-)\\d{4}$");

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
    private DatabaseReference RootRef,CountActivityRef;
    private static int count=0;
    private View sportsFacilitiesFragment;
    private int pos ;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sportsFacilitiesFragment =  inflater.inflate(R.layout.sport_fac_fragment, container, false);
        recyclerView = (RecyclerView) sportsFacilitiesFragment.findViewById(R.id.recyclerView);
        CheckBox_Handi = (CheckBox) sportsFacilitiesFragment.findViewById(R.id.checkbox_Handi);
        RootRef = FirebaseDatabase.getInstance().getReference();
        CountActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        EditText searchFac = (EditText) sportsFacilitiesFragment.findViewById(R.id.search_fac);
        Button myActivity = (Button)sportsFacilitiesFragment.findViewById(R.id.create_activity);
        myActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = customAdapter.getPos();
                if(pos==-1) {
                    Toast.makeText(getActivity(),"בחר תחילה מגרש",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final View mView = getLayoutInflater().inflate(R.layout.newactiviry, null);
                    Button newActivitySub = (Button) mView.findViewById(R.id.submit_activity);
                    builder.setView(mView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    newActivitySub.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {
                            final String name = Paper.book().read(Prevalent.UserNameKey).toString();
                            final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            CountActivityRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        count = (int) dataSnapshot.getChildrenCount() + 1;
                                    } else {
                                        count = 1;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            final String description = ((EditText) mView.findViewById(R.id.activity_type)).getText().toString();
                            final String number_of_players = ((EditText) mView.findViewById(R.id.number_of_players)).getText().toString();
                            final String game_date = ((EditText) mView.findViewById(R.id.game_date)).getText().toString();
                            if (Checking_description_Empty(description)) {
                                Toast.makeText(getActivity(), "אנא הכנס את שם הפעילות שלך", Toast.LENGTH_SHORT).show();
                            } else if (Checking_number_of_players_Empty(number_of_players)) {
                                Toast.makeText(getActivity(), "אנא הכנס מספר משתתפים שלך", Toast.LENGTH_SHORT).show();
                            } else if (Checking_number_of_players_valid_value(number_of_players)) {
                                Toast.makeText(getActivity(), "אנא הכנס מספרים בלבד לכמות שחקנים", Toast.LENGTH_SHORT).show();
                            } else if (Checking_game_date_Empty(game_date)) {
                                Toast.makeText(getActivity(), "אנא הכנס את תאריך הפעילות שלך", Toast.LENGTH_SHORT).show();
                            } else if (Checking_game_date_valid_value(game_date)) {
                                Toast.makeText(getActivity(), "אנא הכנס תאריך מהצורה dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                            } else if(Check_that_date_after_today(game_date,date) ){
                                Toast.makeText(getActivity(), "תאריך זה כבר עבר", Toast.LENGTH_SHORT).show();
                            } else {

                                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                        userdataMap.put("created_by", name);
                                        userdataMap.put("date_created", date);
                                        userdataMap.put("description", description);
                                        userdataMap.put("numbers_of_players", number_of_players);
                                        userdataMap.put("type", filteredListType.get(pos));
                                        userdataMap.put("game_date", game_date);
                                        userdataMap.put("id", count);
                                        userdataMap.put("location", filteredListNeig.get(pos) + ", " + filteredListStre.get(pos));
                                        RootRef.child("Activities").child(String.valueOf(count)).updateChildren(userdataMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(getActivity(), "הפעילות נוספה בהצלחה", Toast.LENGTH_SHORT).show();
                                                        } else {
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
                        }
                    });
                }
            }
        });

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
                    FacNeighborhoods.add("שכונה: " + facDetail.getString("neighborho"));
                    FacStreets.add("רחוב: " + facDetail.getString("street"));
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

    public static boolean Check_that_date_after_today(String game_date, String dateToday) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1=format.parse(game_date);
            Date date2=format.parse(dateToday);
            return date2.after(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean Checking_game_date_valid_value(String game_date) {
        return !DATE_PATTERN.matcher(game_date).matches();
    }

    public static boolean Checking_number_of_players_valid_value(String number_of_players) {
        return !NUMBER_PATTERN.matcher(number_of_players).matches();
    }

    public static boolean Checking_game_date_Empty(String game_date) {
        return game_date.equals("");
    }

    public static boolean Checking_number_of_players_Empty(String number_of_players) {
        return number_of_players.equals("");
    }

    public static boolean Checking_description_Empty(String description) {
        return description.equals("");
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
        mViewModel = ViewModelProviders.of(this).get(SportFacViewModel.class);
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
        pos = customAdapter.getPos();

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
