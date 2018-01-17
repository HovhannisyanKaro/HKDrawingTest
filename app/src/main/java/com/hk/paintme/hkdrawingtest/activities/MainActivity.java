package com.hk.paintme.hkdrawingtest.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.DataController;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.fragments.MainFragment;
import com.hk.paintme.hkdrawingtest.utils.ActivityUtils;
import com.hk.paintme.hkdrawingtest.utils.LogUtils;
import com.hk.paintme.hkdrawingtest.views.Loader;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener {


    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    private InterstitialAd mInterstitialAd;

    @BindView(R.id.loader)
    Loader loader;

    {
        //TODO TODOLIST
        // after main fragment open gallery fragment with draw images
        // check internet connection and show dialog to user
        // check for mersision
    }


    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtils.changeStatusBarColor(R.color.section_people_color);
        ViewController.getViewController().setContext(this);
        ViewController.getViewController().setMainActivity(this);
        d("activity onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        ActivityUtils.changeStatusBarColor(R.color.section_people_color);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        d("activity onCreate");

        String android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        d("deviceId " + android_id);
    }


    public void startLoader() {
        loader.startAnimation();
    }

    public void stopLoader() {
        loader.stopAnimation();
    }

    private void init() {
        isStoragePermissionGranted();
        isOnline();
        ViewController.getViewController().setFragmentManager(getSupportFragmentManager());
        ViewController.getViewController().addFragment(new MainFragment());
        rlRoot.setOnFocusChangeListener(this);


    }

    public void paintClicked(View view) {
        ViewController.getViewController().setColorToBrush(view);
    }

    @Override
    public void onBackPressed() {
        if (!DataController.getInstance().isLoaderRunning()) {
            if (ViewController.getViewController().getFragmentManager().getBackStackEntryCount() > 1) {
                if (ViewController.getViewController().getFragmentManager().getBackStackEntryCount() == 2) {
                    ActivityUtils.changeStatusBarColor(R.color.section_people_color);
                }
                ViewController.getViewController().getFragmentManager().popBackStack();
            } else {
                moveTaskToBack(true);
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
        }
    }


    public boolean isOnline() {
        boolean conection;
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        conection = netInfo != null && netInfo.isConnectedOrConnecting();

        if (conection) {
            return true;
        } else {
            ViewController.getViewController().showToastMandatory(getString(R.string.no_internet));
            return false;
        }
    }

    public void loadAds() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
                LogUtils.d("onAdLoaded");

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                LogUtils.d(errorCode + "");
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
            }
        });
        return interstitialAd;
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template")
                .addTestDevice("27F56163EB89FF33B703CB62C0082FEF")
                .addTestDevice("ad68a240687b1216").build(); //s7
        mInterstitialAd.loadAd(adRequest);
    }

    private void d(String msg) {
        LogUtils.d("HK_LOG" + this.getClass().getSimpleName(), msg);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        d(String.valueOf(b));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        d(String.valueOf(hasFocus));
        if (hasFocus)
            ViewController.getViewController().getCashedBitmap();
        else
            ViewController.getViewController().cashBitmap();
    }
}
