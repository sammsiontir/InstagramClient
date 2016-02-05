package com.codepath.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chengfu_lin on 2/5/16.
 */
public class InstagramPhotosAdaper extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdaper(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for this position
        InstagramPhoto photo = getItem(position);
        // Check if we are using recycle view. If not, we need to inflate
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the views for populating the data (image, Caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // Insert model data to each of the items
        tvCaption.setText(photo.caption);
        // Clear out the image view
        ivPhoto.setImageResource(0);
        // Insert image view using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        // Return the create item as a view
        return convertView;
    }
}
