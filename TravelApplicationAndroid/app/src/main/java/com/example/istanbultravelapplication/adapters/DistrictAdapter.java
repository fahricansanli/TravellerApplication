package com.example.istanbultravelapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.models.District;

import java.util.ArrayList;

public class DistrictAdapter extends BaseAdapter {

    private ArrayList<District> listData;
    private LayoutInflater layoutInflater;

    public DistrictAdapter(Context aContext, ArrayList<District> listData) {
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
            v = layoutInflater.inflate(R.layout.list_districts, null);
            holder = new ViewHolder();
            holder.districtName = (TextView) v.findViewById(R.id.districtName);
            holder.mostPopularActivity = (TextView) v.findViewById(R.id.mostPopularActivityName);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        holder.districtName.setText(listData.get(position).getDistrictName());
        holder.mostPopularActivity.setText(listData.get(position).getMostPopularActivity());
        return v;
    }
    static class ViewHolder {
        TextView districtName;
        TextView mostPopularActivity;
    }
}
