package com.codepath.instagramclient;

import android.os.Bundle;
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
                        // - Type   : { "data" => [x] => "type" } ("image" or "video")
                        // photo.type = photoJSON.getJSONObject("type").getString("text");
                        // - Caption: { "data" => [x] => "caption" }
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        // - URL    : { "data" => [x] => "images" => "standard_resolution" }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // store photo objects
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
}
