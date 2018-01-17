package com.hk.paintme.hkdrawingtest.controllers;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.activities.LaunchScreenActivity;
import com.hk.paintme.hkdrawingtest.activities.MainActivity;
import com.hk.paintme.hkdrawingtest.fragments.draw.DrawFragment;
import com.hk.paintme.hkdrawingtest.fragments.paint.PaintFragment;
import com.hk.paintme.hkdrawingtest.fragments.paint.SectionsFragment;
import com.hk.paintme.hkdrawingtest.utils.ToastUtils;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class ViewController {

    private static ViewController viewController = null;
    private Activity context;
    private LaunchScreenActivity launchScreenActivity;
    private MainActivity mainActivity;
    private FragmentManager fragmentManager;
    private SectionsFragment sectionsFragment;
    private DrawFragment drawFragment;
    private PaintFragment paintFragment;

    private ViewController() {

    }

    public static ViewController getViewController() {
        if (viewController == null) {
            viewController = new ViewController();
        }
        return viewController;
    }

    public DrawFragment getDrawFragment() {
        return drawFragment;
    }

    public void setDrawFragment(DrawFragment drawFragment) {
        this.drawFragment = drawFragment;
    }

    public PaintFragment getPaintFragment() {
        return paintFragment;
    }

    public void setPaintFragment(PaintFragment paintFragment) {
        this.paintFragment = paintFragment;
    }

    public SectionsFragment getSectionsFragment() {
        return sectionsFragment;
    }

    public void setSectionsFragment(SectionsFragment sectionsFragment) {
        this.sectionsFragment = sectionsFragment;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public LaunchScreenActivity getLaunchScreenActivity() {
        return launchScreenActivity;
    }

    public void setLaunchScreenActivity(LaunchScreenActivity launchScreenActivity) {
        this.launchScreenActivity = launchScreenActivity;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_main_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    public void setColorToBrush(View view) {
        if (DataController.getInstance().isPaintMode()) {
            if (paintFragment != null && paintFragment.isAdded()) {
                paintFragment.paintClicked(view);
            }
        } else {
            if (drawFragment != null && drawFragment.isAdded()) {
                drawFragment.paintClicked(view);
            }
        }

    }


    public void showToast(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.t(msg);
            }
        });
    }

    public void showToast(final int msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.t(msg);
            }
        });
    }

    public void showToastMandatory(final String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void goBackFF() {
        fragmentManager.popBackStack();
    }

    public void startLoader() {
        mainActivity.startLoader();
    }

    public void stopLoader() {
        mainActivity.stopLoader();
    }

    public boolean checkInternetConnection() {
//Todo check this (Fabric)
        if (mainActivity != null) {
            return mainActivity.isOnline();
        } else {
            return true;
        }
    }

    public void loadAds() {
        mainActivity.loadAds();
    }


    public void cashBitmap() {
        if (paintFragment != null && paintFragment.isAdded())
            paintFragment.cashBitmap();
    }

    public void getCashedBitmap() {
        if (paintFragment != null && paintFragment.isAdded())
            paintFragment.getBitmapFromCash();
    }
}
