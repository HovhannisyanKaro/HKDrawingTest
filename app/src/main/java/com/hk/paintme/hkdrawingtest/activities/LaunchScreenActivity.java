package com.hk.paintme.hkdrawingtest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;

import butterknife.ButterKnife;

public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        ViewController.getViewController().setContext(this);
        ViewController.getViewController().setLaunchScreenActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_welcome_view);
        ButterKnife.bind(this);
        init();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        runSplash();
    }

    private void runSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMenu();
                finish();
            }
        }, 3000);
    }

    private void goToMenu() {
        startActivity(new Intent(this, MainActivity.class));
    }


}
