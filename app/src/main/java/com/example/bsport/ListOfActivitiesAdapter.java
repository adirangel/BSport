package com.example.bsport;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bsport.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class ListOfActivitiesAdapter extends RecyclerView.Adapter<ListOfActivitiesAdapter.MyViewHolder> {

    ArrayList<String> My_name_activity;
    ArrayList<String> My_game_date;
    ArrayList<String> My_location;


    public ListOfActivitiesAdapter(ArrayList<String> My_name_activity,ArrayList<String> My_game_date,ArrayList<String> My_location) {
        this.My_name_activity=My_name_activity;
        this.My_game_date=My_game_date;
        this.My_location=My_location;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_activities, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(My_name_activity.get(position));
        holder.game_date.setText(My_game_date.get(position));
        holder.location.setText(My_location.get(position));

    }

    @Override
    public int getItemCount() {
        return My_game_date.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, game_date, location;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_activity);
            game_date = (TextView) itemView.findViewById(R.id.game_date1);
            location = (TextView) itemView.findViewById(R.id.location);


        }
    }
}
