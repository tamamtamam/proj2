package com.example.proj2;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("posts")
    Single<List<Post>> posts();


}