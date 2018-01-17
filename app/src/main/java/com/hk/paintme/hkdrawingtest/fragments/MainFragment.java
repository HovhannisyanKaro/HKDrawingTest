package com.hk.paintme.hkdrawingtest.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.DataController;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.fragments.paint.SectionsFragment;
import com.hk.paintme.hkdrawingtest.utils.ActivityUtils;
import com.hk.paintme.hkdrawingtest.utils.GM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    @BindView(R.id.tv_draw)
    TextView tvDraw;
    @BindView(R.id.tv_paint)
    TextView tvPaint;
    Unbinder unbinder;
    @BindView(R.id.ib_info)
    ImageButton ibInfo;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        setRipple();
        initListeners();
        return view;
    }


    private void initListeners() {
        ibInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewController.getViewController().showToast("Info");
            }
        });
    }

    private void setRipple() {
        GM.rippleEfect(tvDraw);
        GM.rippleEfect(tvPaint);
    }

    @OnClick({R.id.tv_draw, R.id.tv_paint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_draw:
                if (ViewController.getViewController().checkInternetConnection()){
                    ViewController.getViewController().startLoader();
                    ViewController.getViewController().addFragment(new GalleryFragment());
                    DataController.getInstance().setPaintMode(false);
                }
                break;
            case R.id.tv_paint:
                if (ViewController.getViewController().checkInternetConnection()) {
                    ViewController.getViewController().addFragment(new SectionsFragment());
                    DataController.getInstance().setPaintMode(true);
                    break;
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
