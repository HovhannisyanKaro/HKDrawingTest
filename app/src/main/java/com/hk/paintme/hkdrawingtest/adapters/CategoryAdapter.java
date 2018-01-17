package com.hk.paintme.hkdrawingtest.adapters;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.models.MenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<MenuModel> data = Collections.emptyList();

    public CategoryAdapter(ArrayList<MenuModel> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.item_menu, null);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            int width = display.getWidth();
            int height = display.getHeight();

            Glide.with(mContext)
                    .load(data.get(position).getImage())
//                    .centerCrop()
                    .override(width/2, height/4)
//                    .placeholder(R.drawable.loading_spinner)
                    .into(imageView);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}