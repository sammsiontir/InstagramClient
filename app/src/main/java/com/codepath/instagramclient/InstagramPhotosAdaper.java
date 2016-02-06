package com.codepath.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InstagramPhotosAdaper extends ArrayAdapter<InstagramPhoto> {
    private InstagramPhoto photo;

    public InstagramPhotosAdaper(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for this position
        photo = getItem(position);
        // Check if we are using recycle view. If not, we need to inflate
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the views for populating the data (image, Caption)
        ImageView ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        // Insert model data to each of the items
        tvUsername.setText(photo.username);
        tvLocation.setText("");
        tvTime.setText("Now");
        tvLikes.setText(Integer.toString(photo.likesCount)  + "  Likes");
        tvCaption.setText(photo.caption);
        // Insert image view using picasso
        setProfilePicture(ivProfilePicture);
        setPhoto(ivPhoto);
        // Return the create item as a view
        return convertView;
    }

    public void setProfilePicture(ImageView ivProfilePicture) {
        ivProfilePicture.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(photo.profilePictureUrl)
                .fit()
                .transform(transformation)
                .into(ivProfilePicture);
    }

    public void setPhoto(ImageView ivPhoto) {
        // Clear current image
        ivPhoto.setImageResource(0);
        // Insert image view using picasso
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .fit()
                .centerInside()
                .into(ivPhoto);
    }
}
