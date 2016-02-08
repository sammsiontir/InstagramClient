package com.codepath.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstagramPhotosAdaper extends ArrayAdapter<InstagramPhoto> {
    private Context parentContext;
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

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.ivProfilePicture) ImageView ivProfilePicture;
        @Bind(R.id.tvUsername) TextView tvUsername;
        @Bind(R.id.tvLocation) TextView tvLocation;
        @Bind(R.id.tvTime) TextView tvTime;
        @Bind(R.id.ivPhoto) ImageView ivPhoto;
        @Bind(R.id.tvLikes) TextView tvLikes;
        @Bind(R.id.tvCaption) TextView tvCaption;
        @Bind(R.id.ivLocationIcon) ImageView ivLocationIcon;
        @Bind(R.id.tvViewAllComments) TextView tvViewAllComments;
    }

    public InstagramPhotosAdaper(Context context, List<InstagramPhoto> objects) {
        super(context, 0, objects);
        this.parentContext = context;
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

            // Initialize all the components in ListView layout
            viewHolder = new ViewHolder(convertView);
            // Add tag to viewHolder
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

        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(photo.createTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        viewHolder.tvTime.setText(relativeTime);
        viewHolder.tvLikes.setText(String.format("%,d", photo.likesCount) + "  Likes");
        if(!photo.caption.isEmpty()) {
            viewHolder.tvCaption.setText(photo.caption);
        }
        else {
            viewHolder.tvCaption.clearComposingText();
        }

        // Insert image view using picasso
        setProfilePicture(viewHolder.ivProfilePicture);
        setPhoto(viewHolder.ivPhoto);

        // Add comments to the bottom
        // Initialize components in Comment LinearLayout
        LinearLayout llButton = (LinearLayout) convertView.findViewById(R.id.llButton);
        LinearLayout llComments = (LinearLayout) convertView.findViewById(R.id.llComments);
        llComments.removeAllViews();
        // add "View all # comments" if there are more than two comments
        if (photo.comments.isEmpty()) {
            llButton.removeView(viewHolder.tvViewAllComments);
        }
        else {
            viewHolder.tvViewAllComments.setText("View all " + String.format("%,d", photo.comments.size()) + " comments");

            viewHolder.tvViewAllComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ViewAllComments = new Intent(parentContext, ViewCommentsActivity.class);
                    ViewAllComments.putExtra("photo", photo);
                    parentContext.startActivity(ViewAllComments);
                }
            });


        }

        // Add latest two comments to the view
        for(int i=0; i < Math.min(2, photo.comments.size()); i++) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View commentView = inflater.inflate(R.layout.item_comment, null);
            TextView tvCommentUser = (TextView) commentView.findViewById(R.id.tvCommentUser);
            TextView tvCommentText = (TextView) commentView.findViewById(R.id.tvCommentText);
            tvCommentUser.setText(photo.comments.get(i).username);
            tvCommentText.setText(photo.comments.get(i).text);
            llComments.addView(commentView);
        }
        // Return the create item as a view
        return convertView;
    }

    protected void setProfilePicture(ImageView ivProfilePicture) {
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
