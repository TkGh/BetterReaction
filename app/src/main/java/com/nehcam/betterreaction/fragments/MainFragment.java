package com.nehcam.betterreaction.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nehcam.betterreaction.BaseFragment;
import com.nehcam.betterreaction.MainActivity;
import com.nehcam.betterreaction.R;
import com.nehcam.betterreaction.StartTask;
import com.nehcam.betterreaction.SwitchListener;
import com.nehcam.betterreaction.interfaces.SwitchCallback;
import com.nehcam.betterreaction.util.NavigationHelper;

import java.util.Date;

public class MainFragment extends BaseFragment implements SwitchCallback<Long> {
    private StartTask startTask;
    private View background;

    private long startTime = 0;
    private long scoreT = 0;
    private long total = 0;
    private int tries = 0;

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment's LifeCycle
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        initClickBackground();
    }

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Menu
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu() called with: menu = [" + menu + "], inflater = [" + inflater + "]");
        }

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (DEBUG) Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");

        switch (item.getItemId()) {
            case R.id.action_reset:
                NavigationHelper.gotoMainFragment(activity.getSupportFragmentManager());
                return true;
            case R.id.action_history:
                NavigationHelper.openHistoryFragment(activity.getSupportFragmentManager());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Utils
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    @Override
    public void finishSwitching() {
        if (startTask != null) {
            startTask.cancel(true);
            startTask = null;
        }
    }

    private void initClickBackground() {
        background = activity.findViewById(R.id.clickable_background);
        background.setOnClickListener(new SwitchListener(this));
    }

    public StartTask getStartTask() {
        if (startTask == null) startTask = new StartTask(this);
        return startTask;
    }

    public synchronized void doChange(MainFragment.FLAG flag) {
        if (DEBUG) {
            Log.d(TAG, "doChange() called with: activity=[" + this + "], flag = [" + flag + "]");
        }

        if (background != null) {
            int color = ((ColorDrawable) background.getBackground()).getColor();

            if (color == getResources().getColor(R.color.blue)) {
                getStartTask().execute();

                changeUI(MainFragment.COLOR.RED, FLAG.NORMAL);
            } else if (color == getResources().getColor(R.color.red)) {
                if (flag == MainFragment.FLAG.NORMAL) {
                    this.startTime = new Date().getTime();

                    changeUI(MainFragment.COLOR.GREEN, FLAG.NORMAL);
                } else if (flag == MainFragment.FLAG.ABNORMAL) {
                    finishSwitching();

                    changeUI(MainFragment.COLOR.BLUE, FLAG.ABNORMAL);
                }
            } else if (color == getResources().getColor(R.color.green)) {
                scoreT = new Date().getTime() - startTime;
                total += scoreT;
                tries++;
                if (tries == 5) ((MainActivity) activity).writeScore(total / tries);

                changeUI(MainFragment.COLOR.BLUE, FLAG.NORMAL);
            }
        }
    }

    private void changeUI(MainFragment.COLOR color, MainFragment.FLAG flag) {
        TextView tapV = activity.findViewById(R.id.tap);
        TextView descV = activity.findViewById(R.id.description);

        TextView scoreV = activity.findViewById(R.id.score);
        TextView averScoreV = activity.findViewById(R.id.average_score);
        TextView triesV = activity.findViewById(R.id.tries_score);

        if (color == MainFragment.COLOR.BLUE) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.blue)));

            if (flag == FLAG.NORMAL) {
                tapV.setText(getResources().getString(R.string.tap_to_continue));
                descV.setText(getResources().getString(R.string.description));

                scoreV.setText(Long.toString(scoreT) + " ms");
                triesV.setText(String.valueOf(tries));
                averScoreV.setText(Long.toString(total / tries));

                scoreV.setVisibility(View.VISIBLE);
            } else if (flag == FLAG.ABNORMAL) {
                tapV.setText(getResources().getString(R.string.too_soon));
            }
        } else if (color == MainFragment.COLOR.RED) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));

            tapV.setText(getResources().getString(R.string.waiting_title));

            scoreV.setVisibility(View.GONE);

        } else if (color == MainFragment.COLOR.GREEN) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.green)));

            tapV.setText(getResources().getString(R.string.reaction_title));
            descV.setText(getResources().getString(R.string.reaction_description));
        }
    }

    public enum FLAG {
        ABNORMAL, NORMAL
    }

    public enum COLOR {
        BLUE, RED, GREEN
    }
}
