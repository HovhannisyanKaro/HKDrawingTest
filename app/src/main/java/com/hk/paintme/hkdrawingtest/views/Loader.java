package com.hk.paintme.hkdrawingtest.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.DataController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hk.paintme.hkdrawingtest.interfacies.Constants.CATEGORIES_ID.ANIMALS;
import static com.hk.paintme.hkdrawingtest.interfacies.Constants.CATEGORIES_ID.CARS;
import static com.hk.paintme.hkdrawingtest.interfacies.Constants.CATEGORIES_ID.NATURE;
import static com.hk.paintme.hkdrawingtest.interfacies.Constants.CATEGORIES_ID.OBJECTS;
import static com.hk.paintme.hkdrawingtest.interfacies.Constants.CATEGORIES_ID.PEOPLE;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class Loader extends RelativeLayout {

    @BindView(R.id.iv_rotate_ic)
    ImageView ivAnimation;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.rl_loader)
    RelativeLayout rlLoader;
    private Context context;
    Unbinder unbinder;
    private boolean runAnimation = true;
    private Animation animRotate;
    private Animation fade_out;


    public Loader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        initListeners();
    }

    private void initListeners() {
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
            }
        });


        btnStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAnimation();
            }
        });
    }

    private void init() {
        View view = inflate(context, R.layout.layout_loader, this);
        unbinder = ButterKnife.bind(this, view);

        animRotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        animRotate.setInterpolator((new LinearInterpolator()));
        fade_out = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        animRotate.setRepeatCount(Animation.INFINITE);


        fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivAnimation.setVisibility(INVISIBLE);
                rlLoader.setVisibility(GONE);
                resetAnimations();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setColorByType(int type) {
        int colorResId = 0;
        switch (type) {
            case ANIMALS:
                colorResId = ContextCompat.getColor(context, R.color.section_animal_color);
                break;
            case CARS:
                colorResId = ContextCompat.getColor(context, R.color.section_cars_color);
                break;
            case NATURE:
                colorResId = ContextCompat.getColor(context, R.color.section_nature_color);
                break;
            case PEOPLE:
                colorResId = ContextCompat.getColor(context, R.color.section_people_color);
                break;
            case OBJECTS:
                colorResId = ContextCompat.getColor(context, R.color.section_objects_color);
                break;
            default:
                break;
        }
        if (colorResId != 0){
            rlLoader.setBackgroundColor(colorResId);
        }
    }

    public void startAnimation() {
        DataController.getInstance().setLoaderRunning(true);
        setColorByType(DataController.getInstance().getCategory());
        rlLoader.setVisibility(VISIBLE);
        ivAnimation.startAnimation(animRotate);
    }

    public void stopAnimation() {
        ivAnimation.startAnimation(fade_out);
    }

    private void resetAnimations() {
        animRotate.cancel();
        fade_out.cancel();
        DataController.getInstance().setLoaderRunning(false);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
