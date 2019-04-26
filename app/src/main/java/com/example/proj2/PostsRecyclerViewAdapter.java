package com.example.proj2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.CustomViewHolder> {
    private List<Post> postList;

    PostsRecyclerViewAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        Post post = postList.get(i);
        customViewHolder.title.setText(post.getTitle());
        customViewHolder.body.setText(post.getBody());
    }

    @Override
    public int getItemCount() {
        return (null != postList ? postList.size() : 0);
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
