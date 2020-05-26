package com.example.bsport;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bsport.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class ListOfActivitiesAdapter extends RecyclerView.Adapter<ListOfActivitiesAdapter.MyViewHolder> {

    private ArrayList<String> My_created_By;
    private ArrayList<String> Date_created;
    private ArrayList<String> My_name_activity;
    private ArrayList<String> My_game_date;
    private ArrayList<String> My_location;
    private ArrayList<String> num_of_players;
    private ArrayList<Integer> Arr_image;
    private ArrayList<String> activity_type;
    private ArrayList<String> My_id;
    private String isAdmin = Prevalent.getUserAdminKey();
    private DatabaseReference RootRef;



    ListOfActivitiesAdapter(
            ArrayList<String> My_created_By,
            ArrayList<String> date_created,
            ArrayList<String> My_name_activity,
            ArrayList<String> My_game_date,
            ArrayList<String> My_id,
            ArrayList<String> My_location,
            ArrayList<String> num_of_players,
            ArrayList<String> activity_type,
            ArrayList<Integer> Arr_Image) {
        this.My_created_By = My_created_By;
        this.Date_created = date_created;
        this.My_name_activity=My_name_activity;
        this.My_game_date=My_game_date;
        this.num_of_players = num_of_players;
        this.activity_type = activity_type;
        this.My_location=My_location;
        this.Arr_image = Arr_Image;
        this.My_id = My_id;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_activities, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(My_name_activity.get(position));
        holder.game_date.setText(My_game_date.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.activity_details_dialog);
                TextView createdbyTV = (TextView) dialog.findViewById(R.id.created_by11);
                createdbyTV.setText(My_created_By.get(position).toString());
                TextView My_name_activityTV = (TextView) dialog.findViewById(R.id.name11);
                My_name_activityTV.setText(My_name_activity.get(position).toString());
                TextView My_game_dateTV = (TextView) dialog.findViewById(R.id.game_date11);
                My_game_dateTV.setText(My_game_date.get(position).toString());
                TextView num_of_playersTV = (TextView) dialog.findViewById(R.id.number_of_players11);
                num_of_playersTV.setText(num_of_players.get(position).toString());
                TextView My_locationTV = (TextView) dialog.findViewById(R.id.location11);
                My_locationTV.setText(My_location.get(position).toString());







                dialog.show();
            }
        });
        if(My_location.get(position).toLowerCase().contains("זמינה".toLowerCase()) ){
            holder.location.setVisibility(View.INVISIBLE);
        }
        else {
            holder.location.setText(My_location.get(position));
        }
        holder.imageType.setImageResource(Arr_image.get(isCheckedWord(activity_type.get(position))));
        if (isAdmin.equals("false")) {
            holder.Remove_button.setVisibility(View.INVISIBLE);
        }
        else {
            holder.Remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RootRef = FirebaseDatabase.getInstance().getReference();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogStyle);

                    builder.setMessage("למחוק את הפעילות?");
                    builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            String po= My_id.get(position);

                            Query activityQuery = RootRef.child("Activities").orderByChild("id").equalTo(po);
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

                    builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {

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



    }

    @Override
    public int getItemCount() {
        return My_game_date.size();
    }

    private Integer isCheckedWord(String type) {
        if( type.toLowerCase().contains("סל".toLowerCase()))
            return 0;
        else if( type.toLowerCase().contains("כדוררגל".toLowerCase()) || type.toLowerCase().contains("כדורגל".toLowerCase()))
            return 1;
        else if (type.toLowerCase().contains("טניס".toLowerCase()))
            return 2;
        else if (type.toLowerCase().contains("שחיה".toLowerCase()) || type.toLowerCase().contains("בריכת".toLowerCase()) || type.toLowerCase().contains("בריכה".toLowerCase()))
            return 3;
        else if (type.toLowerCase().contains("אתלטיקה".toLowerCase()))
            return 4;
        else if (type.toLowerCase().contains("כושר".toLowerCase()))
            return 5;
        else if (type.toLowerCase().contains("פטאנק".toLowerCase()))
            return 6;
        else if (type.toLowerCase().contains("מיני".toLowerCase()) || type.toLowerCase().contains("קטרגל".toLowerCase()))
            return 7;
        else if (type.toLowerCase().contains("גודו".toLowerCase()) || type.toLowerCase().contains("ג'ודו".toLowerCase()))
            return 8;
        else
            return 9;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, game_date, location;// init the item view's
        ImageView imageType;
        CardView cardView;
        ImageButton Remove_button;
        TextView created_by11, date_created11,name11,game_date11,location11,number_of_players11;

        MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_id);
            Remove_button = itemView.findViewById(R.id.deleteButton);
            name = itemView.findViewById(R.id.name_activity);
            game_date = itemView.findViewById(R.id.game_date1);
            location = itemView.findViewById(R.id.location);
            imageType = itemView.findViewById(R.id.image_type);
            created_by11 = itemView.findViewById(R.id.created_by11);
            name11 = itemView.findViewById(R.id.name11);
            game_date11 = itemView.findViewById(R.id.game_date11);
            location11 = itemView.findViewById(R.id.location11);
            number_of_players11 = itemView.findViewById(R.id.number_of_players11);
        }
    }
}
