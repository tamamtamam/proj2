package com.example.proj2;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("posts")
    Single<List<Post>> posts();

    @GET("comments")
    Single<List<Comment>> comments(@Query("postId") int id);
}