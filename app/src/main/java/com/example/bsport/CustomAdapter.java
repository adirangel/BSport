package com.example.bsport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> FacTypes;
    ArrayList<String> FacNames;
    ArrayList<String> FacNeighborhoods;
    ArrayList<String> FacStreets;

    public int getPos() {
        return pos;
    }

    int pos=-1;


    public CustomAdapter( ArrayList<String> FacTypes,ArrayList<String> FacNames, ArrayList<String> FacNeighborhoods, ArrayList<String> FacStreets) {
        this.FacNames = FacNames;
        this.FacNeighborhoods = FacNeighborhoods;
        this.FacStreets = FacStreets;
        this.FacTypes = FacTypes;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        if(FacTypes.size() !=0  && FacNames.size() !=0 && FacNeighborhoods.size() !=0 && FacStreets.size() !=0) {
            holder.type.setText(FacTypes.get(position));
            holder.name.setText(FacNames.get(position));
            holder.neigberhood.setText(FacNeighborhoods.get(position));
            holder.street.setText(FacStreets.get(position));
            holder.create_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"בחרת מגרש להוספת פעילות" + "\n" + "מוזמן ללחוץ על יצירת פעילות חדשה", Toast.LENGTH_LONG).show();
                    pos = position;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
}