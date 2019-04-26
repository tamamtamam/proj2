package com.example.proj2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
    private Menu toolbarMenu;
    private RecyclerView.ItemDecoration horizontalDecoration;
    private boolean isGrid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.parseColor("#EF9A9A"));
        setSupportActionBar(myToolbar);

        dataRepository = new DataRepository(this);

        postList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);
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
                    public void accept(List<Post> posts) {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        toolbarMenu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
                if (!isGrid) {
                    isGrid = true;
                    mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    horizontalDecoration = new DividerItemDecoration(this,
                            DividerItemDecoration.HORIZONTAL);
                    mRecyclerView.addItemDecoration(horizontalDecoration);

                    toolbarMenu.getItem(0).setIcon(R.drawable.ic_action_non_grid);
                } else {
                    isGrid = false;
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerView.removeItemDecoration(horizontalDecoration);
                    toolbarMenu.getItem(0).setIcon(R.drawable.ic_action_grid);

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
