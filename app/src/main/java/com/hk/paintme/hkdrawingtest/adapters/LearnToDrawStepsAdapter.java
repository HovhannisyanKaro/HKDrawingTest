package com.hk.paintme.hkdrawingtest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.interfacies.OnPageSelectedListener;

import java.util.ArrayList;

/**
 * Created by Hovhannisyan.Karo on 13.08.2017.
 */

public class LearnToDrawStepsAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> imagesUrl;
    private OnPageSelectedListener onPageSelectedListener;

    public LearnToDrawStepsAdapter(OnPageSelectedListener onPageSelectedListener, ArrayList<String> imagesUrl) {
        mContext = ViewController.getViewController().getContext();
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onPageSelectedListener = onPageSelectedListener;
        this.imagesUrl = imagesUrl;
    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_learn_to_draw_learn_mode, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_learn_to_draw_sections);
        final ProgressBar pb = (ProgressBar) itemView.findViewById(R.id.pb_step);
        pb.getIndeterminateDrawable().setColorFilter(Color.parseColor("#c24fc0"), PorterDuff.Mode.MULTIPLY);
        final ImageView imageViewForRipple = (ImageView) itemView.findViewById(R.id.iv_learn_to_draw_section_for_ripple);

        String currUrl = imagesUrl.get(position);
        Glide.with(mContext)
                .load(currUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
//        imageView.setImageResource(mResources[position]);

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


}