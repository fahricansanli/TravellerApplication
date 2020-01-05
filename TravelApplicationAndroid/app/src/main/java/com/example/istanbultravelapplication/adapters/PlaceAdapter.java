package com.example.istanbultravelapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.models.Place;

import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {

    private ArrayList<Place> listData;
    private LayoutInflater layoutInflater;

    public PlaceAdapter(Context aContext, ArrayList<Place> listData) {
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
            v = layoutInflater.inflate(R.layout.list_places, null);
            holder = new ViewHolder();
            holder.placeName = (TextView) v.findViewById(R.id.placeName);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        holder.placeName.setText(listData.get(position).getName());
        return v;
    }
    static class ViewHolder {
        TextView placeName;
    }
}
