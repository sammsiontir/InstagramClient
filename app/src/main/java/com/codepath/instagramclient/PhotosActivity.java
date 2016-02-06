package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdaper aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // SEND out API request to popular hotos
        photos = new ArrayList<>();
        // Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdaper(this, photos);
        // Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set adapter to the listview
        lvPhotos.setAdapter(aPhotos);
        // Fetch the Popular Photos
        fetchPopularPhotos();


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // Trigger API request
    public void fetchPopularPhotos() {
        /*
         * Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
         * - Response
         *   - Type   : { "data" => [x] => "type" } ("image" or "video")
         *   - URL    : { "data" => [x] => "images" => "standard_resolution" }
         *   - Caption: { "data" => [x] => "caption" }
         *   - Author : { "data" => [x] => "user" => "username"}
         */
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // Create network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Clear current items in photos
                photos.clear();

                // Iterate each of the photo items and decode item into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); // array of photos
                    // iterate array of photos
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get JSON object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributes of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        // - Author : { "data" => [x] => "user" => "username"}
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.profilePictureUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        // - Type   : { "data" => [x] => "type" } ("image" or "video")
                        // photo.type = photoJSON.getJSONObject("type").getString("text");
                        // - Caption: { "data" => [x] => "caption" }
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        // - URL    : { "data" => [x] => "images" => "standard_resolution" }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imagewidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // - Create time
                        photo.createTime = photoJSON.getLong("created_time");
                        // - Location
                        photo.location = "";
                        // store photo objects
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
}
