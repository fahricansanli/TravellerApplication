package com.example.istanbultravelapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.models.Comment;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private ArrayList<Comment> listData;
    private LayoutInflater layoutInflater;

    public CommentAdapter(Context aContext, ArrayList<Comment> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;

        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_comments, null);
            holder = new ViewHolder();
            holder.commentText = (TextView) v.findViewById(R.id.commentText);
            holder.username = (TextView) v.findViewById(R.id.username);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        holder.commentText.setText(listData.get(position).getComment());
        holder.username.setText(listData.get(position).getUsername());
        return v;
    }
    static class ViewHolder {
        TextView commentText;
        TextView username;
    }
}
