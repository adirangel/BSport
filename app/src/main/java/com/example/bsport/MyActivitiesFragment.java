package com.example.bsport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsport.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;


public class MyActivitiesFragment extends Fragment {

    private Button MyActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initilizedFields();


    }



    /*private void initilizedFields() {
        MyActivity = (Button) MyActivity.findViewById(R.id.new_activity);

    }*/

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Button MyActivity = (Button) View.findViewById(R.id.new_activity);
        return inflater.inflate(R.layout.fragment_my_activities, container, false);
    }*/

   /* public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyActivity = (Button) view.findViewById(R.id.new_activity);
        MyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_activities, container, false);
        Button MyActivity = (Button) view.findViewById(R.id.new_activity);
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
                        String type1= type.getText().toString();
                        String description1= description.getText().toString();
                        String number_of_players1= number_of_players.getText().toString();
                        dialog.cancel();
                    }

                });

            }
        });
        return view;
    }

}
