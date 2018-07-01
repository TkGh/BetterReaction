package com.nehcam.betterreaction;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nehcam.betterreaction.util.NavigationHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//TODO: record history scores.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    private ExecutorService exec;

    private String scoreCache = "scoreCache";
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private static final int BSIZE = 1024;

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

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    private void initFragments() {
        if (DEBUG) Log.d(TAG, "initFragments() called");

        NavigationHelper.gotoMainFragment(getSupportFragmentManager());
    }

    public void writeScore(Long data) {
        if (DEBUG) Log.d(TAG, "recordScore() called");

        Lock wLock = lock.writeLock();
        wLock.lock();
        if (exec == null) exec = Executors.newFixedThreadPool(2);
        try {
            exec.execute(() -> {
                try {
                    File dir = new File(getFilesDir(), scoreCache);
                    if (!dir.exists()) dir.createNewFile();
                    DataOutputStream out = new DataOutputStream(
                            new BufferedOutputStream(
                                    openFileOutput(scoreCache, Context.MODE_APPEND)));

                    out.writeLong(data);

                    out.close();
                } catch (IOException e) {
                    if (DEBUG) Log.e(TAG, e.toString());
                }

            });
        } finally {
            wLock.unlock();
        }
    }

    public List<Long> readScore() {
        if (DEBUG) Log.d(TAG, "readScore() called");

        List<Long> scores = new ArrayList<>();
        Lock rLock = lock.readLock();
        rLock.lock();
        try {
            File dir = new File(getFilesDir(), scoreCache);
            if (dir.exists()) {
                DataInputStream in = new DataInputStream(
                        new BufferedInputStream(
                                openFileInput(scoreCache)));

                while (in.available() >= 8) {
                    scores.add(in.readLong());
                }

                in.close();
            }
        } catch (FileNotFoundException FileEx) {
            if (DEBUG) Log.e(TAG, FileEx.toString());
        } catch (IOException IOEx) {
            if (DEBUG) Log.e(TAG, IOEx.toString());
        } finally {
            rLock.unlock();
        }


        return scores;
    }
}
