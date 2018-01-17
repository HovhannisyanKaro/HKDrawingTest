package com.hk.paintme.hkdrawingtest.utils;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;

/**
 * Created by Hovhannisyan.Karo on 19.11.2017.
 */

public class ActivityUtils {

    public static void changeStatusBarColor(@ColorRes int resId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Activity activity = ViewController.getViewController().getContext();
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, resId ));
        }
    }
}
