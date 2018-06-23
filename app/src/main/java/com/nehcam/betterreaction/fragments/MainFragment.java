package com.nehcam.betterreaction.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nehcam.betterreaction.BaseFragment;
import com.nehcam.betterreaction.R;
import com.nehcam.betterreaction.StartTask;
import com.nehcam.betterreaction.SwitchListener;
import com.nehcam.betterreaction.interfaces.SwitchCallback;

import java.util.Date;

public class MainFragment extends BaseFragment implements SwitchCallback<Long> {
    private StartTask startTask;
    private View background;

    private long startTime;
    private long scoreT;
    private long total = 0;
    private int tries = 0;

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment's LifeCycle
    ////////////////////////////////////////////////////////////////////////////////////////////*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initClickBackground();
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

                changeUI(MainFragment.COLOR.RED);
            } else if (color == getResources().getColor(R.color.red)) {
                if (flag == MainFragment.FLAG.AUTO) {
                    this.startTime = new Date().getTime();

                    changeUI(MainFragment.COLOR.GREEN);
                } else if (flag == MainFragment.FLAG.MANUAL) {
                    finishSwitching();

                    changeUI(MainFragment.COLOR.BLUE);
                }
            } else if (color == getResources().getColor(R.color.green)) {
                scoreT = new Date().getTime() - startTime;
                total += scoreT;
                tries++;

                changeUI(MainFragment.COLOR.BLUE);
            }
        }
    }

    private void changeUI(MainFragment.COLOR color) {
        TextView tapV = activity.findViewById(R.id.tap);
        TextView descV = activity.findViewById(R.id.description);

        TextView scoreV = activity.findViewById(R.id.score);
        TextView averScoreV = activity.findViewById(R.id.average_score);
        TextView triesV = activity.findViewById(R.id.tries_score);

        if (color == MainFragment.COLOR.BLUE) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.blue)));

            tapV.setText(getResources().getString(R.string.tap_to_continue));
            descV.setText(getResources().getString(R.string.description));

            scoreV.setText(Long.toString(scoreT) + " ms");
            triesV.setText(String.valueOf(tries));
            averScoreV.setText(Long.toString(total / tries));

            scoreV.setVisibility(View.VISIBLE);
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
        MANUAL, AUTO
    }

    public enum COLOR {
        BLUE, RED, GREEN
    }
}
