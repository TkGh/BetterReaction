package com.nehcam.betterreaction;

import android.view.View;

import com.nehcam.betterreaction.fragments.MainFragment;

public class SwitchListener implements View.OnClickListener {
    private final MainFragment callback;

    public SwitchListener(MainFragment fragment) {
        this.callback = fragment;
    }

    @Override
    public void onClick(View v) {
        callback.doChange(MainFragment.FLAG.ABNORMAL);
    }
}
