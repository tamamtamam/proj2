package com.example.proj2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    void savePostsToDb(final List<Post> posts) {

        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        for (Post post : posts) {
            ContentValues values = new ContentValues();
            values.put("id", post.getId());
            values.put("user_id", post.getUserId());
            values.put("title", post.getTitle());
            values.put("body", post.getBody());

            writableDatabase.insertWithOnConflict("post", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    void saveCommentsToDb(final List<Comment> comments) {

        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        for (Comment comment : comments) {
            ContentValues values = new ContentValues();
            values.put("id", comment.getId());
            values.put("email", comment.getEmail());
            values.put("name", comment.getName());
            values.put("body", comment.getBody());
            values.put("post_id", comment.getPostId());

            writableDatabase.insertWithOnConflict("comment", null, values, SQLiteDatabase.CONFLICT_REPLACE);
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

    List<Comment> getComments(int post_id) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from comment where post_id=" + post_id, null);

        List<Comment> comments = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Comment comment = new Comment();
                comment.setBody(cursor.getString(cursor.getColumnIndex("body")));
                comment.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                comment.setName(cursor.getString(cursor.getColumnIndex("name")));
                comment.setId(cursor.getInt(cursor.getColumnIndex("id")));
                comment.setPostId(cursor.getInt(cursor.getColumnIndex("post_id")));
                comments.add(comment);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return comments;
    }
}
