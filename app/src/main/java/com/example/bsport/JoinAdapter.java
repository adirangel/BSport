package com.example.bsport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.MyViewHolder>{
    private ArrayList<String> UserJoin = new ArrayList<>();
    JoinAdapter(ArrayList<String> UserJoin) {
        this.UserJoin = UserJoin;
    }

    @NonNull
    @Override
    public JoinAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_activity_layout, parent, false);
        JoinAdapter.MyViewHolder vh = new JoinAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(UserJoin.get(position));
    }

    @Override
    public int getItemCount() {
        return UserJoin.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;// init the item view's
        CardView cardView;

        MyViewHolder(final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view1);
            name = itemView.findViewById(R.id.nameJoin);
        }
    }
}
