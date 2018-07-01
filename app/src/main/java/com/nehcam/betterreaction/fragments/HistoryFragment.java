package com.nehcam.betterreaction.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nehcam.betterreaction.BaseFragment;
import com.nehcam.betterreaction.MainActivity;
import com.nehcam.betterreaction.R;
import com.nehcam.betterreaction.util.HistoryListAdapter;
import com.nehcam.betterreaction.util.NavigationHelper;

import java.util.List;

public class HistoryFragment extends BaseFragment {
    private String[] dataSet;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment's LifeCycle
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.history_title);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        dataSet = retrieveData();

        View empty = activity.findViewById(R.id.history_empty);
        View holder = activity.findViewById(R.id.history_list_view);

        if (dataSet == null) {
            empty.setVisibility(View.VISIBLE);
            holder.setVisibility(View.GONE);

            return;
        }

        empty.setVisibility(View.GONE);
        holder.setVisibility(View.VISIBLE);

        init();
    }

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Menu
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                NavigationHelper.gotoMainFragment(activity.getSupportFragmentManager());
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Util
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    private String[] retrieveData() {
        List<Long> scores = ((MainActivity) activity).readScore();
        if (scores.size() == 0) return null;

        String[] data = new String[scores.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = scores.get(i).toString();
        }

        return data;
    }

    private void init() {
        recyclerView = activity.findViewById(R.id.history_list_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);

        adapter = new HistoryListAdapter(dataSet);
        recyclerView.setAdapter(adapter);
    }
}
