package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewCommentsActivity extends AppCompatActivity {
    InstagramPhoto photo;
    ArrayList<InstagramPhoto.Comment> comments;
    InstagramPhotoCommentsAdapter aComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("All comments ");

        // set ListView adapter
        photo = (InstagramPhoto) getIntent().getSerializableExtra("photo");
        comments = photo.comments;
        aComment = new InstagramPhotoCommentsAdapter(this, comments);

        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        lvComments.setAdapter(aComment);
    }
}
