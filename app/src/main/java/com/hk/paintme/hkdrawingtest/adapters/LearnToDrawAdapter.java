package com.hk.paintme.hkdrawingtest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.models.ImageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hovhannisyan.Karo on 25.06.2017.
 */

public class LearnToDrawAdapter extends RecyclerView.Adapter<LearnToDrawAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ImageModel>data = Collections.emptyList();


    public LearnToDrawAdapter (Context context, ArrayList<ImageModel>data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_learn_to_draw, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context)
                .load(data.get(position).getImage())
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
