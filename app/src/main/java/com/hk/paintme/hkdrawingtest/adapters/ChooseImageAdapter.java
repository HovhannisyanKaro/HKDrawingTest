package com.hk.paintme.hkdrawingtest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.interfacies.OnPageSelectedListener;
import com.hk.paintme.hkdrawingtest.models.ImageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class ChooseImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageModel> data = Collections.emptyList();

    public ChooseImageAdapter(ArrayList<ImageModel> data) {
        mContext = ViewController.getViewController().getContext();
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            viewHolder.llProgress = (LinearLayout) convertView.findViewById(R.id.ll_progress);
            viewHolder.pb = (ProgressBar) convertView.findViewById(R.id.pb);
            viewHolder.pb.getIndeterminateDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        String imageUri = data.get(position).getImage();
        if (imageUri != null) {
            Glide.with(mContext)
                    .load(imageUri)
                    .override(width / 3, height / 4)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            viewHolder.llProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.llProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(viewHolder.imageView);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        LinearLayout llProgress;
        ProgressBar pb;
    }


}