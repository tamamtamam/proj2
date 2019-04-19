package com.example.proj2;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {

    private ApiService apiService;

    public DataRepository(Context context) {
        apiService = ApiClient.getClient(context)
                .create(ApiService.class);

    }

    Single<List<Post>> getPosts() {
        return apiService.posts();

    }

}
