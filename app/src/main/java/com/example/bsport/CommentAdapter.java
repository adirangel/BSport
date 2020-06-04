package com.example.bsport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{
    private ArrayList<String> UserComment = new ArrayList<>();
    private ArrayList<String> Comment = new ArrayList<>();
    public CommentAdapter(ArrayList<String> UserComment, ArrayList<String> comment) {
        this.UserComment = UserComment;
        this.Comment=comment;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(UserComment.get(position));
        holder.comment.setText(Comment.get(position));
    }

    @Override
    public int getItemCount() {
        return Comment.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,comment;// init the item view's
        CardView cardView;

        MyViewHolder(final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view1);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
