package com.example.proj2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.CustomViewHolder> {
    private List<Comment> commentList;

    CommentsRecyclerViewAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, null);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        Comment comment = commentList.get(i);
        customViewHolder.title.setText(comment.getName());
        customViewHolder.body.setText(comment.getBody());
    }

    @Override
    public int getItemCount() {
        return (null != commentList ? commentList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;

        CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.body = (TextView) view.findViewById(R.id.body);
        }
    }
}
