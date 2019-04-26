package com.example.proj2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CommentsRecyclerViewAdapter adapter;
    private List<Comment> commentList;
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.parseColor("#EF9A9A"));
        setSupportActionBar(myToolbar);

        dataRepository = new DataRepository(this);
        postId = getIntent().getIntExtra("post_id", -1);

        commentList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        adapter = new CommentsRecyclerViewAdapter(commentList);
        mRecyclerView.setAdapter(adapter);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Disposable disposable = dataRepository
                .getComments(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comment>>() {
                    @Override
                    public void accept(List<Comment> comments) {
                        commentList.clear();
                        commentList.addAll(comments);
                        adapter.notifyDataSetChanged();
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onStop() {
        super.onStop();

        compositeDisposable.dispose();
    }
}
