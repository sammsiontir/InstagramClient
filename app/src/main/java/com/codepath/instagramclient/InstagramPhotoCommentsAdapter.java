package com.codepath.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chengfu_lin on 2/6/16.
 */
public class InstagramPhotoCommentsAdapter extends ArrayAdapter<InstagramPhoto.Comment> {
    InstagramPhoto.Comment comment;

    private static class CommentViewHolder {
        TextView tvCommentUser;
        TextView tvCommentText;
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
            convertView = inflater.inflate(R.layout.item_comment, parent, false);

            // Initialize all the components in ListView layout
            commentViewHolder = new CommentViewHolder();
            commentViewHolder.tvCommentUser = (TextView) convertView.findViewById(R.id.tvCommentUser);
            commentViewHolder.tvCommentText = (TextView) convertView.findViewById(R.id.tvCommentText);

            // Add tag to viewHolder
            convertView.setTag(commentViewHolder);
        }
        else {
            commentViewHolder = (CommentViewHolder) convertView.getTag();
        }

        commentViewHolder.tvCommentUser.setText(comment.username);
        commentViewHolder.tvCommentText.setText(comment.text);

        return convertView;
    }
}
