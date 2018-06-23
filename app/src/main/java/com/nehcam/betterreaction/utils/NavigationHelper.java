package com.nehcam.betterreaction.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nehcam.betterreaction.R;
import com.nehcam.betterreaction.fragments.MainFragment;

public class NavigationHelper {

    private static FragmentTransaction defaultTransaction(FragmentManager fragmentManager) {
        return fragmentManager.beginTransaction();
    }

    public static void gotoMainFragment(FragmentManager fragmentManager) {
        openMainFragment(fragmentManager);
    }

    public static void openMainFragment(FragmentManager fragmentManager) {
        defaultTransaction(fragmentManager).replace(R.id.fragment_holder, new MainFragment())
                .commit();
    }
}
