package com.nehcam.betterreaction;

import android.view.View;

public class SwitchListener implements View.OnClickListener {
    private MainActivity context;

    @Override
    public void onClick(View v) {
        if (context == null) {context = (MainActivity) v.getContext();}
        context.doChange(MainActivity.FLAG.MANUAL);
    }
}
