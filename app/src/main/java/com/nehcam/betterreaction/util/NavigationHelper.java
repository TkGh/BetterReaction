package com.nehcam.betterreaction.util;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nehcam.betterreaction.R;
import com.nehcam.betterreaction.fragments.HistoryFragment;
import com.nehcam.betterreaction.fragments.MainFragment;

public class NavigationHelper {
    private static final String MAIN_FRAGMENT_TAG = "main_fragment_tag";
    private static final String HISTORY_FRAGMENT_TAG = "history_fragment_tag";

    private static FragmentTransaction defaultTransaction(FragmentManager fragmentManager) {
        return fragmentManager.beginTransaction();
    }

    public static void gotoMainFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate(MAIN_FRAGMENT_TAG, 0);
        openMainFragment(fragmentManager);
    }

    public static void openMainFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        defaultTransaction(fragmentManager)
                .replace(R.id.fragment_holder, new MainFragment())
                .addToBackStack(MAIN_FRAGMENT_TAG)
                .commit();
    }

    public static void openHistoryFragment(FragmentManager fragmentManager) {
        defaultTransaction(fragmentManager)
                .replace(R.id.fragment_holder, new HistoryFragment())
                .addToBackStack(HISTORY_FRAGMENT_TAG)
                .commit();
    }

}
