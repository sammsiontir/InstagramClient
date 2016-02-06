package com.codepath.instagramclient;

import java.util.ArrayList;

/**
 * Created by chengfu_lin on 2/5/16.
 */
public class InstagramPhoto {
    public class User {
        public String username;
        public String id;
        public String profilePictureUrl;
    }

    public class Comment {
        public long createTime;
        public String text;
        public String username;
        public String id;
    }

    public class Image {
        public String url;
        public int height;
        public int width;
    }

    public class Video {
        public String url;
        public int height;
        public int width;
    }

    public String type;
    public String caption;
    public int likesCount;
    public String location;
    public long createTime;
    public String id;
    public ArrayList<Comment> comments;
    public Image image;
    public Video video;
    public User user;

    public InstagramPhoto() {
        this.comments = new ArrayList<>();
        this.image = new Image();
        this.video = new Video();
        this.user = new User();
    }
}
