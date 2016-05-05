package com.edvaldotsi.nodejsandandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edvaldotsi.nodejsandandroid.R;
import com.edvaldotsi.nodejsandandroid.model.Post;

import java.util.List;

/**
 * Created by Edvaldo on 23/04/2016.
 */
public class PostAdapter extends AbstractAdapter<PostAdapter.ViewHolder, Post> {

    public PostAdapter(Context context) {
        super(context);
    }

    public PostAdapter(Context context, List<Post> items) {
        super(context, items);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Post post) {
        holder.title(post.getTitle());
        holder.status(post.getStatus());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.card_post, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends AbstractAdapter.AbstractViewHolder {

        public TextView textTitle;
        public TextView textStatus;

        public ViewHolder(View v) {
            super(v);

            textTitle = (TextView) v.findViewById(R.id.text_post_title);
            textStatus = (TextView) v.findViewById(R.id.text_post_status);
        }

        public void title(String title) {
            textTitle.setText(title);
        }

        public void status(String status) {
            textStatus.setText(status);

            int color = "ativo".equals(status) ? Color.rgb(0, 102, 0) : Color.rgb(204, 0, 0);
            textStatus.setTextColor(color);
        }
    }
}
