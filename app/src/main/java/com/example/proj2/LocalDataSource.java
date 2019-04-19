package com.example.proj2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class LocalDataSource {

    private static LocalDataSource instance;
    private DatabaseHelper database;
    private Context context;

    private LocalDataSource(Context context) {
        database = new DatabaseHelper(context);
    }

    static LocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new LocalDataSource(context);
        }

        return instance;
    }

    void saveToDB(final List<Post> posts) {

        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        for (Post post : posts) {
            ContentValues values = new ContentValues();
            values.put("id", post.getId());
            values.put("user_id", post.getUserId());
            values.put("title", post.getTitle());
            values.put("body", post.getBody());

            writableDatabase.insert("post", null, values);
        }
    }

    List<Post> getPosts() {

        Cursor cursor = database.getReadableDatabase().rawQuery("select * from post", null);

        List<Post> posts = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Post post = new Post();
                post.setBody(cursor.getString(cursor.getColumnIndex("body")));
                post.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                post.setId(cursor.getInt(cursor.getColumnIndex("id")));
                post.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));

                posts.add(post);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return posts;
    }
}
