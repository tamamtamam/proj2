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
    private ClickListener clickListener;

    PostsRecyclerViewAdapter(List<Post> postList, ClickListener clickListener) {
        this.postList = postList;
        this.clickListener = clickListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, null);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        customViewHolder.bind(postList.get(i), clickListener);
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

        void bind(final Post post, final ClickListener listener) {
            title.setText(post.getTitle());
            body.setText(post.getBody());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(post);
                }
            });

        }

    }
}
