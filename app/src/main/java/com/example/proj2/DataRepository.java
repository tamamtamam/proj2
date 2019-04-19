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
        Log.e("WTF", String.valueOf(lastFetchedTime));
        if (currentTime - lastFetchedTime > 5 * 60 * 1000) {
            Log.e("WTF", "to");
            return apiService.posts()
                    .doOnSuccess(new Consumer<List<Post>>() {
                        @Override
                        public void accept(List<Post> posts) throws Exception {
                            localDataSource.savePostsToDb(posts);
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

    Single<List<Comment>> getComments(int post_id) {
        final String sharedPreferencesKey = "post_" + String.valueOf(post_id) + "_comments_last_fetched_time";
        long lastFetchedTime = sharedPreferences.getLong(sharedPreferencesKey, 0);
        final long currentTime = System.currentTimeMillis();
//        Log.e("WTF_LASTFT", String.valueOf(lastFetchedTime));
        if (currentTime - lastFetchedTime > 5 * 60 * 1000) {
//            Log.e("WTF", "to");
            return apiService.comments(post_id)
                    .doOnSuccess(new Consumer<List<Comment>>() {
                        @Override
                        public void accept(List<Comment> comments) throws Exception {
//                            Log.e("WTF", "chert_to_db");
                            localDataSource.saveCommentsToDb(comments);
                        }
                    })
                    .doOnSuccess(new Consumer<List<Comment>>() {
                        @Override
                        public void accept(List<Comment> comments) throws Exception {
//                            Log.e("WTF", "chert");
                            sharedPreferences.edit().putLong(sharedPreferencesKey, System.currentTimeMillis()).apply();
                        }
                    }).onErrorReturnItem(localDataSource.getComments(post_id));
        } else {
//            Log.e("WTF", "else");
            return Single.just(localDataSource.getComments(post_id));
        }

    }
}
