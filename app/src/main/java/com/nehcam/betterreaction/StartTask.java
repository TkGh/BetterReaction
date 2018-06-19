package com.nehcam.betterreaction;

import android.os.AsyncTask;
import android.util.Log;

import com.nehcam.betterreaction.interfaces.SwitchCallback;

import java.util.Random;

public class StartTask extends AsyncTask<Void, Void, Void> {
    protected String TAG = getClass().getSimpleName().toString();
    private final Random rand = new Random(5);

    private SwitchCallback<Long> callback;

    StartTask(SwitchCallback<Long> callback) {
        this.callback = callback;
    }

    void setCallback(SwitchCallback<Long> callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        if (callback == null) {
            cancel(true);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(rand.nextInt(7) * 1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "doInBackground().sleep() interrupted");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void noResult) {
        if (callback != null && callback instanceof MainActivity) {
            callback.finishSwitching();
            ((MainActivity) callback).doChange(MainActivity.FLAG.AUTO);
        }
    }
}
