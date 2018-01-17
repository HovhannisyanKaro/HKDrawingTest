package com.hk.paintme.hkdrawingtest.fragments.paint;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.adapters.SectionsAdapter;
import com.hk.paintme.hkdrawingtest.controllers.DataController;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.fragments.GalleryFragment;
import com.hk.paintme.hkdrawingtest.interfacies.OnPageSelectedListener;
import com.hk.paintme.hkdrawingtest.utils.ActivityUtils;
import com.hk.paintme.hkdrawingtest.utils.GM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SectionsFragment extends Fragment implements OnPageSelectedListener {


    @BindView(R.id.vp_sections)
    ViewPager vpSections;
    Unbinder unbinder;
    @BindView(R.id.rg_sections)
    RadioGroup rgSections;

    private SectionsAdapter adapter;



    public SectionsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewController.getViewController().setSectionsFragment(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        unbinder = ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.changeStatusBarColor(R.color.section_animal_color);
    }

    private void init(View view) {
        ViewController.getViewController().checkInternetConnection();

        setAdapter();
        initListeners();
    }

    private void initListeners() {
        vpSections.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rgSections.check(rgSections.getChildAt(position).getId());
               changeStatusBarColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeStatusBarColor(int position){
        switch (position){
            case 0:
                ActivityUtils.changeStatusBarColor(R.color.section_animal_color);
                break;
            case 1:
                ActivityUtils.changeStatusBarColor(R.color.section_cars_color);
                break;
            case 2:
                ActivityUtils.changeStatusBarColor(R.color.section_nature_color);
                break;
            case 3:
                ActivityUtils.changeStatusBarColor(R.color.section_people_color);
                break;
            case 4:
                ActivityUtils.changeStatusBarColor(R.color.section_objects_color);

                break;
        }
    }

    private void setAdapter() {
        adapter = new SectionsAdapter(this);
        vpSections.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPageSelected( View view, int position) {
        if (ViewController.getViewController().checkInternetConnection()){
            DataController.getInstance().setCategory(position);
            view.setBackground(GM.makeSelector(position));
            ViewController.getViewController().addFragment(new GalleryFragment());
        }
    }
}
