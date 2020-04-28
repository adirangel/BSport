package com.example.bsport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> FacTypes;
    ArrayList<String> FacNames;
    ArrayList<String> FacNeighborhoods;
    ArrayList<String> FacStreets;
//    Context context;


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
        holder.type.setText(FacTypes.get(position));
        holder.name.setText(FacNames.get(position));
        holder.neigberhood.setText(FacNeighborhoods.get(position));
        holder.street.setText(FacStreets.get(position));

        // implement setOnClickListener event on item view.
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // display a toast with person name on item click
//                Toast.makeText(context, FacNames.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return FacNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, name, neigberhood, street;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            type = (TextView) itemView.findViewById(R.id.type);
            name = (TextView) itemView.findViewById(R.id.name);
            neigberhood = (TextView) itemView.findViewById(R.id.neighborhood);
            street = (TextView) itemView.findViewById(R.id.street);

        }
    }
}