package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReturn();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set ListView adapter
        photo = (InstagramPhoto) getIntent().getSerializableExtra("photo");
        comments = photo.comments;
        aComment = new InstagramPhotoCommentsAdapter(this, comments);

        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        lvComments.setAdapter(aComment);
    }


    public void btnReturn() {
        this.finish();
    }
}
