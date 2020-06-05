package com.example.bsport;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bsport.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> FacTypes;
    ArrayList<String> FacNames;
    ArrayList<String> FacNeighborhoods;
    ArrayList<String> FacStreets;
    private static int count=0;
    public static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+$");
    public static final Pattern DATE_PATTERN = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-(2[0-9]{3}) (0[0-9]|1[0-9]|2[0123])\\:([012345][0-9])$");
private DatabaseReference RootRef;

    public CustomAdapter( ArrayList<String> FacTypes,ArrayList<String> FacNames, ArrayList<String> FacNeighborhoods, ArrayList<String> FacStreets) {
        this.FacNames = FacNames;
        this.FacNeighborhoods = FacNeighborhoods;
        this.FacStreets = FacStreets;
        this.FacTypes = FacTypes;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // set the data in items
        if(FacTypes.size() !=0  && FacNames.size() !=0 && FacNeighborhoods.size() !=0 && FacStreets.size() !=0) {
            holder.type.setText(FacTypes.get(position));
            holder.name.setText(FacNames.get(position));
            holder.neigberhood.setText(FacNeighborhoods.get(position));
            holder.street.setText(FacStreets.get(position));
            holder.create_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.newactiviry);

                    Button newActivitySub = (Button) dialog.findViewById(R.id.submit_activity);
                    dialog.show();




                    newActivitySub.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(final View v) {
                            final String name = Prevalent.getUserName();
                            final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            DatabaseReference CountActivityRef = FirebaseDatabase.getInstance().getReference().child("Activities");

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
                            final String description = ((EditText) dialog.findViewById(R.id.activity_type)).getText().toString();
                            final String number_of_players = ((EditText) dialog.findViewById(R.id.number_of_players)).getText().toString();
                            final String game_date = ((EditText) dialog.findViewById(R.id.game_date)).getText().toString();
                            if (Checking_description_Empty(description)) {
                                Toast.makeText(v.getContext(), "אנא הכנס את שם הפעילות שלך", Toast.LENGTH_SHORT).show();
                            }
                            else if (Checking_number_of_players_Empty(number_of_players)) {
                                Toast.makeText(v.getContext(), "אנא הכנס מספר משתתפים שלך", Toast.LENGTH_SHORT).show();
                            }
                            else if (Checking_number_of_players_valid_value(number_of_players)) {
                                Toast.makeText(v.getContext(), "אנא הכנס מספרים בלבד לכמות שחקנים", Toast.LENGTH_SHORT).show();
                            }
                            else if (Checking_game_date_Empty(game_date)) {
                                Toast.makeText(v.getContext(), "אנא הכנס את תאריך ושעה לפעילות שלך", Toast.LENGTH_SHORT).show();
                            }
                            else if (Checking_game_date_valid_value(game_date)) {
                                Toast.makeText(v.getContext(), "אנא הכנס תאריך מהצורה dd-mm-yyyy hh:mm", Toast.LENGTH_SHORT).show();
                            }
                            else if(Check_that_date_after_today(game_date,date) ){
                                Toast.makeText(v.getContext(), "תאריך זה כבר עבר", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                 RootRef = FirebaseDatabase.getInstance().getReference();

                                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String keyId = FirebaseDatabase.getInstance().getReference("Activities").push().getKey();
                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                        userdataMap.put("created_by", name);
                                        userdataMap.put("date_created", date);
                                        userdataMap.put("description", description);
                                        userdataMap.put("numbers_of_players", number_of_players);
                                        userdataMap.put("type", FacTypes.get(position));
                                        userdataMap.put("game_date", game_date);
                                        userdataMap.put("id", keyId);
                                        userdataMap.put("location", FacNeighborhoods.get(position) + ", " + FacStreets.get(position));
                                        RootRef.child("Activities").child(String.valueOf(keyId)).updateChildren(userdataMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(v.getContext(), "הפעילות נוספה בהצלחה", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(v.getContext(), "הפעילות לא התווספה בהצלחה- אנא נסה שנית", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        HashMap<String, Object> joindataMap = new HashMap<>();
                                        joindataMap.put("name1",name);
                                        RootRef.child("Activities").child(String.valueOf(keyId)).child("join").updateChildren(joindataMap);

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
            });
        }
    }

    @Override
    public int getItemCount() {
        return FacNames.size();
    }

    public void filterList(ArrayList<String> filteredListType,ArrayList<String> filteredListName,ArrayList<String> filteredListNeig,ArrayList<String> filteredListStre) {
        FacTypes = filteredListType;
        FacNames = filteredListName;
        FacNeighborhoods = filteredListNeig;
        FacStreets = filteredListStre;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, name, neigberhood, street;// init the item view's
        ImageButton create_activity;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            type = (TextView) itemView.findViewById(R.id.type);
            name = (TextView) itemView.findViewById(R.id.name);
            neigberhood = (TextView) itemView.findViewById(R.id.neighborhood);
            street = (TextView) itemView.findViewById(R.id.street);
            create_activity= (ImageButton)itemView.findViewById(R.id.create_activity_image);

        }
    }
    public static boolean Check_that_date_after_today(String game_date, String dateToday) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
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

}