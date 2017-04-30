package com.example.solinari.GetTaipeiMetroInfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Solinari on 2017/1/21.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] MetroInfo;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStation;
        public TextView tvDestination;
        public TextView tvTime;
        public ViewHolder(View v) {
            super(v);
            tvStation =(TextView) v.findViewById(R.id.tvStation);
            tvDestination =(TextView) v.findViewById(R.id.tvDestination);
            tvTime =(TextView) v.findViewById(R.id.tvTime);
        }

    }
    public RecyclerViewAdapter(String[] info) {
        MetroInfo = info;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_metroinfo, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //MetroInfo[position] 士林站_大安站_20:59:27 ，用"_"來區分各個欄位
        String[] tmp  = MetroInfo[position].split("_");
        holder.tvStation.setText(tmp[0]);
        holder.tvDestination.setText(tmp[1]);
        holder.tvTime.setText(tmp[2]);
    }
    @Override
    public int getItemCount() {
        return MetroInfo.length;
    }
}