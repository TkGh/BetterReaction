package com.nehcam.betterreaction;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nehcam.betterreaction.interfaces.SwitchCallback;

import java.util.Date;

//TODO: record history scores.
public class MainActivity extends AppCompatActivity implements SwitchCallback<Long> {
    private static final String TAG = "MainActivity";
    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    private View background;
    private StartTask startTask;

    private long startTime;
    private long scoreT;
    private long total = 0;
    private int tries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = findViewById(R.id.clickable_background);
        background.setOnClickListener(new SwitchListener());
    }

    public synchronized void doChange(FLAG flag) {
        if (DEBUG) {
            Log.d(TAG, "doChange() called with: activity=[" + this + "], flag = [" + flag + "]");
        }

        if (background != null) {
            int color = ((ColorDrawable) getBackground().getBackground()).getColor();

            if (color == getResources().getColor(R.color.blue)) {
                getStartTask().execute();

                changeUI(COLOR.RED);
            } else if (color == getResources().getColor(R.color.red)) {
                if (flag == FLAG.AUTO) {
                    this.startTime = new Date().getTime();

                    changeUI(COLOR.GREEN);
                } else if (flag == FLAG.MANUAL) {
                    finishSwitching();

                    changeUI(COLOR.BLUE);
                }
            } else if (color == getResources().getColor(R.color.green)) {
                scoreT = new Date().getTime() - startTime;
                total += scoreT;
                tries++;

                changeUI(COLOR.BLUE);
            }
        }
    }

    private void changeUI(COLOR color) {
        TextView tapV = findViewById(R.id.tap);
        TextView descV = findViewById(R.id.description);

        TextView scoreV = findViewById(R.id.score);
        TextView averScoreV = findViewById(R.id.average_score);
        TextView triesV = findViewById(R.id.tries_score);

        if (color == COLOR.BLUE) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.blue)));


            tapV.setText(getResources().getString(R.string.tap_to_continue));
            descV.setText(getResources().getString(R.string.description));

            scoreV.setText(Long.toString(scoreT) + " ms");
            triesV.setText(String.valueOf(tries));
            averScoreV.setText(Long.toString(total / tries));

            scoreV.setVisibility(View.VISIBLE);
        } else if (color == COLOR.RED) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));

            tapV.setText(getResources().getString(R.string.waiting_title));

            scoreV.setVisibility(View.GONE);

        } else if (color == COLOR.GREEN) {
            background.setBackground(new ColorDrawable(getResources().getColor(R.color.green)));

            tapV.setText(getResources().getString(R.string.reaction_title));
            descV.setText(getResources().getString(R.string.reaction_description));
        }
    }

    public StartTask getStartTask() {
        if (startTask == null) startTask = new StartTask(this);
        return startTask;
    }

    @Override
    public void finishSwitching() {
        if (startTask != null) {
            startTask.cancel(true);
            startTask = null;
        }
    }

    public enum FLAG {
        MANUAL, AUTO
    }

    public enum COLOR {
        BLUE, RED, GREEN
    }

    public View getBackground() {
        return this.background;
    }
}
