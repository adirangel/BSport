package com.example.bsport;
import android.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class ListOfActivitiesAdapter extends RecyclerView.Adapter<ListOfActivitiesAdapter.MyViewHolder> {

    private ArrayList<String> My_name_activity;
    private ArrayList<String> My_game_date;
    private ArrayList<String> My_location;
    private ArrayList<Integer> Arr_image;
    private ArrayList<String> activity_type;
    private ArrayList<String> My_id;
    private String isAdmin = Paper.book().read(Prevalent.UserAdminKey).toString();
    private DatabaseReference RootRef;



    ListOfActivitiesAdapter(ArrayList<String> My_name_activity, ArrayList<String> My_game_date, ArrayList<String> My_location, ArrayList<Integer> Arr_Image, ArrayList<String> activity_type, ArrayList<String> My_id) {
        this.My_name_activity=My_name_activity;
        this.My_game_date=My_game_date;
        this.My_location=My_location;
        this.Arr_image = Arr_Image;
        this.activity_type =activity_type;
        this.My_id = My_id;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_activities, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(My_name_activity.get(position));
        holder.game_date.setText(My_game_date.get(position));
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

        MyViewHolder(View itemView) {
            super(itemView);
            Remove_button = itemView.findViewById(R.id.deleteButton);
            name = itemView.findViewById(R.id.name_activity);
            game_date = itemView.findViewById(R.id.game_date1);
            location = itemView.findViewById(R.id.location);
            imageType = itemView.findViewById(R.id.image_type);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
