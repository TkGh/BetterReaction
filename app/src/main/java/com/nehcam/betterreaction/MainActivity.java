package com.nehcam.betterreaction;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nehcam.betterreaction.utils.NavigationHelper;

import java.util.Date;

//TODO: record history scores.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager() != null && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            initFragments();
        }
    }

    private void initFragments() {
        if (DEBUG) Log.d(TAG, "initFragments() called");

        NavigationHelper.gotoMainFragment(getSupportFragmentManager());
    }
}
