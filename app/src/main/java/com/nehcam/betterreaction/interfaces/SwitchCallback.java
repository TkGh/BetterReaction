package com.nehcam.betterreaction.interfaces;

import android.net.NetworkInfo;

public interface SwitchCallback<T> {
    /**
     * Indicates that the SwitchTask has finished. This method is called even if the
     * SwitchTask hasn't completed successfully.
     */
    void finishSwitching();
}
