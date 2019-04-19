package com.example.proj2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

public class DataRepository {

    private ApiService apiService;
    private LocalDataSource localDataSource;
    private SharedPreferences sharedPreferences;

    DataRepository(Context context) {
        apiService = ApiClient.getClient(context)
                .create(ApiService.class);

        localDataSource = LocalDataSource.getInstance(context);

        sharedPreferences = context.getSharedPreferences("proj2-time",
                Context.MODE_PRIVATE);

    }

    Single<List<Post>> getPosts() {
        long lastFetchedTime = sharedPreferences.getLong("last_fetched_time", 0);
        final long currentTime = System.currentTimeMillis();
//        Log.e("WTF", String.valueOf(lastFetchedTime));
        if (currentTime - lastFetchedTime > 5 * 60 * 1000) {
//            Log.e("WTF", "to");
            return apiService.posts()
                    .doOnSuccess(new Consumer<List<Post>>() {
                        @Override
                        public void accept(List<Post> posts) throws Exception {
                            localDataSource.saveToDB(posts);
                        }
                    })
                    .doOnSuccess(new Consumer<List<Post>>() {
                        @Override
                        public void accept(List<Post> posts) throws Exception {
                            sharedPreferences.edit().putLong("last_fetched_time", System.currentTimeMillis()).apply();
                        }
                    }).onErrorReturnItem(localDataSource.getPosts());
        } else {
            return Single.just(localDataSource.getPosts());
        }

    }
}
