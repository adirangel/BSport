package com.example.bsport;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bsport.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {

    ArrayList<String> My_name_activity;
    ArrayList<String> My_activity_type;
    ArrayList<String> My_game_date;
    ArrayList<String> My_location;
    ArrayList<String> My_number_of_players;
    ArrayList<String> My_date_created;

    public ActivityAdapter(ArrayList<String> My_name_activity,ArrayList<String> My_activity_type,ArrayList<String> My_game_date,ArrayList<String> My_location,ArrayList<String> My_number_of_players,ArrayList<String> My_date_created) {
        this.My_name_activity=My_name_activity;
        this.My_activity_type=My_activity_type;
        this.My_game_date=My_game_date;
        this.My_location=My_location;
        this.My_number_of_players=My_number_of_players;
        this.My_date_created=My_date_created;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_activities, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(My_name_activity.get(position));
        holder.type.setText(My_activity_type.get(position));
        holder.game_date.setText(My_game_date.get(position));
        holder.location.setText(My_location.get(position));
        holder.number_of_players.setText(My_number_of_players.get(position));
        holder.date_create.setText(My_date_created.get(position));
    }

    @Override
    public int getItemCount() {
        return My_activity_type.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, name, game_date, location,number_of_players,date_create;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_activity);
            type = (TextView) itemView.findViewById(R.id.activity_type1);
            game_date = (TextView) itemView.findViewById(R.id.game_date1);
            location = (TextView) itemView.findViewById(R.id.location);
            number_of_players = (TextView) itemView.findViewById(R.id.number_of_players1);
            date_create = (TextView) itemView.findViewById(R.id.date_created);

        }
    }
}
