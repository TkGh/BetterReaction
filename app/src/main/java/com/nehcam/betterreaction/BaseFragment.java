package com.nehcam.betterreaction;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    protected boolean DEBUG = MainActivity.DEBUG;

    protected AppCompatActivity activity;

    /*////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment's LifeCycle
    ////////////////////////////////////////////////////////////////////////////////////////////*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        if (DEBUG) {
            Log.d(TAG, "onViewCreated() called with: rootView = ["
                    + rootView + "], savedInstanceState = ["
                    + savedInstanceState + "]");
        }
    }
}
