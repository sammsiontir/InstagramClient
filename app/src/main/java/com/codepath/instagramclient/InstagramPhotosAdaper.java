package com.codepath.instagramclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InstagramPhotosAdaper extends ArrayAdapter<InstagramPhoto> {
    private InstagramPhoto photo;

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    private static class ViewHolder {
        ImageView ivProfilePicture;
        TextView tvUsername;
        TextView tvLocation;
        TextView tvTime;
        ImageView ivPhoto;
        TextView tvLikes;
        TextView tvCaption;
        ImageView ivLocationIcon;
    }



    public InstagramPhotosAdaper(Context context, List<InstagramPhoto> objects) {
        super(context, 0, objects);
    }

    // Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for this position
        photo = getItem(position);
        // Check if we are using recycle view. If not, we need to inflate
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_photo, parent, false);

            // Initialize all the components in layout
            viewHolder = new ViewHolder();
            viewHolder.ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivLocationIcon = (ImageView) convertView.findViewById(R.id.ivLocationIcon);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Insert model data to each of the items
        viewHolder.tvUsername.setText(photo.user.username);
        viewHolder.tvLocation.setText("");
        if (photo.location.isEmpty()) {
            // move username to center if there is no location information
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.tvUsername.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            viewHolder.tvUsername.setLayoutParams(params);
        }
        else {
            // set location and location icon
            viewHolder.ivLocationIcon.setImageResource(R.drawable.location);
            viewHolder.tvLocation.setText(photo.location);
        }

        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(photo.createTime*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        viewHolder.tvTime.setText(relativeTime);
        viewHolder.tvLikes.setText(Integer.toString(photo.likesCount)  + "  Likes");
        viewHolder.tvCaption.setText(photo.caption);
        // Insert image view using picasso
        setProfilePicture(viewHolder.ivProfilePicture);
        setPhoto(viewHolder.ivPhoto);
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
                .load(photo.user.profilePictureUrl)
                .fit()
                .transform(transformation)
                .into(ivProfilePicture);
    }

    public void setPhoto(ImageView ivPhoto) {
        // Clear current image
        ivPhoto.setImageResource(0);
        // Insert image view using picasso
        Picasso.with(getContext())
                .load(photo.image.url)
                .fit().centerInside()
                .transform(new CropSquareTransformation())
                .into(ivPhoto);

    }

}
