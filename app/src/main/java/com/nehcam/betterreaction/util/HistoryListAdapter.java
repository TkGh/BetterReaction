package com.nehcam.betterreaction.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nehcam.betterreaction.R;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private String[] dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout view;

        public ViewHolder(RelativeLayout view) {
            super(view);
            this.view = view;
        }
    }

    public HistoryListAdapter(String[] dataSet) {
        this.dataSet = dataSet;
    }

    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView number = holder.view.findViewById(R.id.history_item_num);
        TextView text = holder.view.findViewById(R.id.history_item_score);

        number.setText(String.valueOf(position + 1));
        text.setText(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
