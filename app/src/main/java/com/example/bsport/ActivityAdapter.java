package com.example.bsport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {

    ArrayList<String> My_name_activity;
    ArrayList<String> My_activity_type;
    ArrayList<String> My_game_date;
    ArrayList<String> My_location;
    ArrayList<String> My_number_of_players;
    ArrayList<String> My_date_created;
    ArrayList<String> My_id;
    ArrayList<String> My_join;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();






    public ActivityAdapter(ArrayList<String> My_name_activity,ArrayList<String> My_activity_type,ArrayList<String> My_game_date,ArrayList<String> My_location,ArrayList<String> My_number_of_players,ArrayList<String> My_date_created,ArrayList<String> My_id,ArrayList<String> My_join) {
        this.My_name_activity=My_name_activity;
        this.My_activity_type=My_activity_type;
        this.My_game_date=My_game_date;
        this.My_location=My_location;
        this.My_number_of_players=My_number_of_players;
        this.My_date_created=My_date_created;
        this.My_id = My_id;
        this.My_join = My_join;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_activities, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(My_name_activity.get(position));
        holder.type.setText(My_activity_type.get(position));
        holder.game_date.setText(My_game_date.get(position));
        holder.location.setText(My_location.get(position));
        holder.number_of_players.setText(My_number_of_players.get(position));
        holder.date_create.setText(My_date_created.get(position));
        holder.join1.setText("שחקנים שהצטרפו: "+My_join.get(position));

        holder.remove_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>מחיקת פעילות</font>"));
                builder.setMessage("האם אתה בטוח?");
                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>כן</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String po= My_id.get(position);
                        Query activityQuery = rootRef.child("Activities").orderByChild("id").equalTo(po);
                        activityQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                    ds.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>לא</font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return My_activity_type.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, name, game_date, location,number_of_players,date_create,join1;// init the item view's
        ImageButton remove_activity;

        public MyViewHolder(View itemView) {
            super(itemView);
            join1 = (TextView) itemView.findViewById(R.id.joinParticipates);
            name = (TextView) itemView.findViewById(R.id.name_activity);
            type = (TextView) itemView.findViewById(R.id.activity_type1);
            game_date = (TextView) itemView.findViewById(R.id.game_date1);
            location = (TextView) itemView.findViewById(R.id.location);
            number_of_players = (TextView) itemView.findViewById(R.id.number_of_players1);
            date_create = (TextView) itemView.findViewById(R.id.date_created);
            remove_activity= (ImageButton)itemView.findViewById(R.id.remove_activity_image);


        }
    }
}
