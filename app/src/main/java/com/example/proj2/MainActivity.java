package com.example.proj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PostsRecyclerViewAdapter adapter;
    private List<Post> postList;
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataRepository = new DataRepository(this);

        postList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        adapter = new PostsRecyclerViewAdapter(postList);
        mRecyclerView.setAdapter(adapter);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onResume() {
        super.onResume();


        Disposable disposable = dataRepository
                .getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Post>>() {
                    @Override
                    public void accept(List<Post> posts) throws Exception {
                        postList.clear();
                        postList.addAll(posts);
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
