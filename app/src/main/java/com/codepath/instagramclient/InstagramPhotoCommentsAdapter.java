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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstagramPhotoCommentsAdapter extends ArrayAdapter<InstagramPhoto.Comment> {
    InstagramPhoto.Comment comment;

    static class CommentViewHolder {
        public CommentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.ivProfilePictureC) ImageView ivProfilePictureC;
        @Bind(R.id.tvUsernameC) TextView tvUsernameC;
        @Bind(R.id.tvComment) TextView tvComment;
    }

    public InstagramPhotoCommentsAdapter(Context context, ArrayList<InstagramPhoto.Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for this position
        comment = getItem(position);
        // Check if we are using recycle view. If not, we need to inflate
        CommentViewHolder commentViewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_comment_detail, parent, false);
            // Initialize all the components in ListView layout
            commentViewHolder = new CommentViewHolder(convertView);
            // Add tag to viewHolder
            convertView.setTag(commentViewHolder);
        }
        else {
            commentViewHolder = (CommentViewHolder) convertView.getTag();
        }

        setProfilePicture(commentViewHolder.ivProfilePictureC);
        commentViewHolder.tvUsernameC.setText(comment.username);
        commentViewHolder.tvComment.setText(comment.text);

        return convertView;
    }

    protected void setProfilePicture(ImageView ivProfilePicture) {
        ivProfilePicture.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(0)
                .cornerRadiusDp(20)
                .oval(true)
                .build();

        Picasso.with(getContext())
                .load(comment.profilePictureUrl)
                .fit()
                .transform(transformation)
                .into(ivProfilePicture);
    }
}
