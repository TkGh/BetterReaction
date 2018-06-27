package com.nehcam.betterreaction;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nehcam.betterreaction.util.NavigationHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//TODO: record history scores.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    private ExecutorService exec;

    FileOutputStream outputStream;
    private String scoreCache = "scoreCache";
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DEBUG) Log.d(TAG, "onCreateOptionsMenu called with: menu = [" + menu + "]");

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (DEBUG) Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");

        switch (item.getItemId()) {
            case R.id.action_reset:
                NavigationHelper.gotoMainFragment(getSupportFragmentManager());
                return true;
            case R.id.action_history:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFragments() {
        if (DEBUG) Log.d(TAG, "initFragments() called");

        NavigationHelper.gotoMainFragment(getSupportFragmentManager());
    }

    public void recordScore(String data) {
        if (DEBUG) Log.d(TAG, "recordScore() called");

        Lock wLock = lock.writeLock();
        wLock.lock();
        if (exec == null) exec = Executors.newFixedThreadPool(2);
        try {
            exec.execute(() -> {
                try {
                    File dir = new File(getFilesDir(), scoreCache);
                    if (!dir.exists()) dir.createNewFile();
                    outputStream = openFileOutput(scoreCache, Context.MODE_PRIVATE);
                    outputStream.write((data + "\n").getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    if (DEBUG) Log.e(TAG, e.toString());
                }

            });
        } finally {
            wLock.unlock();
        }
    }

    public void readScore() {
        if (DEBUG) Log.d(TAG, "readScore() called");

        Lock rLock = lock.readLock();
        rLock.lock();
        if (exec == null) exec = Executors.newFixedThreadPool(2);
        try {
            exec.execute(() -> {

            });
        } finally {
            rLock.unlock();
        }
    }
}
