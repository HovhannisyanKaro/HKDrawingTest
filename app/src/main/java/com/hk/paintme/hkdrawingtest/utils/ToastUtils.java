package com.hk.paintme.hkdrawingtest.utils;

import android.widget.Toast;

import com.hk.paintme.hkdrawingtest.controllers.ViewController;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class ToastUtils {
    private static final boolean isDebug = true;

    public static void t(String message) {
        if (isDebug)
        Toast.makeText(ViewController.getViewController().getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void t( int message) {
        if (isDebug)
            Toast.makeText(ViewController.getViewController().getContext(), "" + message, Toast.LENGTH_SHORT).show();
    }
}
