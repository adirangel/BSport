package com.example.bsport;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bsport.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListOfActivitiesAdapter extends RecyclerView.Adapter<ListOfActivitiesAdapter.MyViewHolder> {

    private ArrayList<String> My_created_By;
    private ArrayList<String> My_name_activity;
    private ArrayList<String> My_game_date;
    private ArrayList<String> My_location;
    private ArrayList<String> num_of_players;
    private ArrayList<Integer> Arr_image;
    private ArrayList<String> activity_type;
    private ArrayList<String> My_id;
    private ArrayList<String> UserJoin = new ArrayList<>();
    private ArrayList<String> UserCommit = new ArrayList<>();
    private ArrayList<String> Comment = new ArrayList<>();
    private String isAdmin = Prevalent.getUserAdminKey();
    private TextView spotsLeft, num_of_playersTV;
    private static String username = Prevalent.getUserName();
    private static int count=1,countJoin=1;
    private CommentAdapter comment_adapter;
    private JoinAdapter join_adapter;
    private static DatabaseReference RootRef,CountActivityRef2,CountActivityRef1,CountActivityRef;
    int flag=0;
    private int countPlayer = 0,size;
    ListOfActivitiesAdapter(
            ArrayList<String> My_created_By,
            ArrayList<String> My_name_activity,
            ArrayList<String> My_game_date,
            ArrayList<String> My_id,
            ArrayList<String> My_location,
            ArrayList<String> num_of_players,
            ArrayList<String> activity_type,
            ArrayList<Integer> Arr_Image) {
        this.My_created_By = My_created_By;
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(v.getContext(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.activity_details_dialog);

                CountActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities").child(My_id.get(position).toString());
                final Button submit_activity2 = (Button) dialog.findViewById(R.id.submit_activity2);

                CountActivityRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.child("join")).exists()) {
                            countJoin = (int) dataSnapshot.child("join").getChildrenCount() + 1;
                            for (DataSnapshot bs : dataSnapshot.child("join").getChildren()){
                                size = countPlayer- countJoin+1;
                                if (bs.getValue().equals(username)) {
                                    if (size <= 0) {
                                        spotsLeft.setText(" אין עוד מקום בפעילות זו " );
                                        submit_activity2.setBackgroundColor(Color.GRAY);
                                        submit_activity2.setClickable(false);
                                    }
                                    else {
                                        spotsLeft.setText("נשארו עוד " + size + " מקומות");
                                    }
                                    submit_activity2.setClickable(false);
                                    break;
                                }

                                if (size <=0) {

                                    submit_activity2.setClickable(false);
                                    break;
                                }
                                else {
                                    submit_activity2.setClickable(true);
                                    spotsLeft.setText("נשארו עוד " + (countPlayer - countJoin+1) + " מקומות");
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final TextView num_of_playersTV = (TextView) dialog.findViewById(R.id.number_of_players11);
                num_of_playersTV.setText(num_of_players.get(position).toString());
                spotsLeft = (TextView) dialog.findViewById(R.id.spotsLeft);
                countPlayer = Integer.parseInt(num_of_playersTV.getText().toString());
                size = countPlayer- countJoin;
                if (size == 0) {
                    spotsLeft.setText(" אין עוד מקום בפעילות זו " );
                    submit_activity2.setClickable(false);
                }
                else {
                    spotsLeft.setText("נשארו עוד " + size + " מקומות");
                }
                TextView createdbyTV = (TextView) dialog.findViewById(R.id.created_by11);
                createdbyTV.setText(My_created_By.get(position).toString());
                TextView My_name_activityTV = (TextView) dialog.findViewById(R.id.name11);
                My_name_activityTV.setText(My_name_activity.get(position).toString());
                TextView My_game_dateTV = (TextView) dialog.findViewById(R.id.game_date11);
                My_game_dateTV.setText(My_game_date.get(position).toString());
                TextView My_locationTV = (TextView) dialog.findViewById(R.id.location11);
                My_locationTV.setText(My_location.get(position).toString());
                Button newActivitySub = (Button) dialog.findViewById(R.id.add_comments_button);
                CountActivityRef1 = FirebaseDatabase.getInstance().getReference().child("Activities").child(My_id.get(position).toString()).child("comments");
                CountActivityRef2 = FirebaseDatabase.getInstance().getReference().child("Activities").child(My_id.get(position).toString()).child("join");
                final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recylerCommit);
                final RecyclerView recyclerJoin = (RecyclerView) dialog.findViewById(R.id.recylerJoin);


                username=Prevalent.getUserName();

                CountActivityRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserCommit.clear();
                        Comment.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserCommit.add(ds.child("username").getValue().toString());
                            Comment.add(ds.child("comment").getValue().toString());


                        }
                        comment_adapter = new CommentAdapter(UserCommit,Comment);
                        size = countPlayer- countJoin+1;
                        if (size <= 0) {
                            spotsLeft.setText(" אין עוד מקום בפעילות זו " );
                            submit_activity2.setBackgroundColor(Color.GRAY);
                            submit_activity2.setClickable(false);
                        }
                        else {
                            spotsLeft.setText("נשארו עוד " + size + " מקומות");
                        }
                        recyclerView.setLayoutManager(new GridLayoutManager(dialog.getContext(),1));
                        recyclerView.setAdapter(comment_adapter); // set the Adapter to RecyclerView

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                CountActivityRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserJoin.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserJoin.add(ds.getValue().toString());
                        }
                        join_adapter = new JoinAdapter(UserJoin);
                        recyclerJoin.setLayoutManager(new GridLayoutManager(dialog.getContext(),3));
                        recyclerJoin.setAdapter(join_adapter); // set the Adapter to RecyclerView

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                submit_activity2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RootRef = FirebaseDatabase.getInstance().getReference();
                        CountActivityRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if ((dataSnapshot.child("join")).exists()) {
                                    countJoin = (int) dataSnapshot.child("join").getChildrenCount() + 1;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                            TextView spotsLeft = (TextView) dialog.findViewById(R.id.spotsLeft);
                            size = countPlayer - countJoin;
                            if (size <= 0) {
                                spotsLeft.setText(" אין עוד מקום בפעילות זו " );
                                submit_activity2.setBackgroundColor(Color.GRAY);
                                submit_activity2.setClickable(false);
                                Map<String, Object> map = new HashMap<>();
                                map.put("name"+String.valueOf(countJoin),username);
                                RootRef.child("Activities").child(My_id.get(position).toString()).child("join").updateChildren(map);
                            }
                            else {
                                spotsLeft.setText("נשארו עוד " + size + " מקומות");
                                Map<String, Object> map = new HashMap<>();
                                map.put("name" + String.valueOf(countJoin), username);
                                RootRef.child("Activities").child(My_id.get(position).toString()).child("join").updateChildren(map);
                            }
                    }
                });

                newActivitySub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag=0;
                        RootRef = FirebaseDatabase.getInstance().getReference();
                        final String comment;
                        comment = ((EditText)dialog.findViewById(R.id.add_comments)).getText().toString();
                        CountActivityRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if ((dataSnapshot.child("comments")).exists()) {
                                    count = (int) dataSnapshot.child("comments").getChildrenCount() + 1;

                                }
                                else {
                                    count = 1;
                                }
                                if(!comment.equals("") && flag==0){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("username",username);
                                    map.put("comment",comment);
                                    RootRef.child("Activities").child(My_id.get(position).toString()).child("comments").child(String.valueOf(count)).updateChildren(map);
                                    flag=1;
                                    return;
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(v.getContext(), "תגובה נוספה בהצלחה", Toast.LENGTH_SHORT).show();

                    }

                });
                flag=0;
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
        Button newActivitySub;

        MyViewHolder(final View itemView) {
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
