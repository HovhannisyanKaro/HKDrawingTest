package com.hk.paintme.hkdrawingtest.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class GM {

    public static void changeTextFond(Context context, TextView tv) {
        Typeface myFont = Typeface.createFromAsset(context.getAssets(), "fonts/fun_raiser.ttf");
        tv.setTypeface(myFont);
    }

    public static void rippleEfect(View view) {
        MaterialRippleLayout.on(view)
                .rippleDuration(500)
                .rippleAlpha((float) 0.1)
                .rippleOverlay(true)
                .rippleColor(Color.WHITE)
                .create();
    }

    public static StateListDrawable makeSelector(int code) {
        int color = Color.YELLOW;
        switch (code){
            case 0:
                color = Color.GREEN;
                break;
            case 1:
                color = Color.GRAY;
                break;
            case 2:
                color = Color.RED;
                break;
            case 3:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.YELLOW;
                break;
        }
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(800);
        res.setAlpha(80);
        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
        res.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        return res;
    }
}
