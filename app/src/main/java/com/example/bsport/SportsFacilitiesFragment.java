package com.example.bsport;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class SportsFacilitiesFragment extends Fragment {

    private View facilitiesFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_facilities = new ArrayList<>();
    private DatabaseReference FacilitiesRef;
    public SportsFacilitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        facilitiesFragmentView = inflater.inflate(R.layout.fragment_sports_facilities, container, false);
        FacilitiesRef = FirebaseDatabase.getInstance().getReference().child("Facilities");

        IntializeFields();
        RetrieveAndDisplayFacilities();

        return facilitiesFragmentView;
    }

    private void RetrieveAndDisplayFacilities() {
        FacilitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                list_of_facilities.clear();
                list_of_facilities.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IntializeFields() {
        list_view = (ListView) facilitiesFragmentView.findViewById(R.id.list_view);
        arrayAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_facilities);
        list_view.setAdapter(arrayAdapter);
    }
}
