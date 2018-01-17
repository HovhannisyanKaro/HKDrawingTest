package com.hk.paintme.hkdrawingtest.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.interfacies.OnPageSelectedListener;

/**
 * Created by Hovhannisyan.Karo on 05.08.2017.
 */

public class SectionsAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnPageSelectedListener onPageSelectedListener;

    public SectionsAdapter(OnPageSelectedListener onPageSelectedListener) {
        mContext = ViewController.getViewController().getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        this.onPageSelectedListener = onPageSelectedListener;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_section, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_sections);
        final ImageView imageViewForRipple = (ImageView) itemView.findViewById(R.id.iv_section_for_ripple);
        imageView.setImageResource(mResources[position]);

        container.addView(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPageSelectedListener.onPageSelected(imageViewForRipple, position);
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    int[] mResources = {
            R.drawable.section_animals,
            R.drawable.section_cars,
            R.drawable.section_nature,
            R.drawable.section_people,
            R.drawable.section_objects
    };
}