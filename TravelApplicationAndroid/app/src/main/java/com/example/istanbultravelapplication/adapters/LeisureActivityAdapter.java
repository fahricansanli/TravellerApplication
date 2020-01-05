package com.example.istanbultravelapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.models.LeisureTimeActivity;

import java.util.ArrayList;

public class LeisureActivityAdapter extends BaseAdapter {
    private ArrayList<LeisureTimeActivity> listData;
    private LayoutInflater layoutInflater;

    public LeisureActivityAdapter(Context aContext, ArrayList<LeisureTimeActivity> listData) {
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
            v = layoutInflater.inflate(R.layout.list_leisure, null);
            holder = new ViewHolder();
            holder.leisureActivityName = (TextView) v.findViewById(R.id.leisureActivityName);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        holder.leisureActivityName.setText(listData.get(position).getName());
        return v;
    }
    static class ViewHolder {
        TextView leisureActivityName;
    }
}
