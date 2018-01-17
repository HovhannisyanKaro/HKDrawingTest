package com.hk.paintme.hkdrawingtest.fragments.paint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hk.paintme.hkdrawingtest.DrawingView;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.controllers.DataController;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.utils.LogUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PaintFragment extends Fragment {

    @BindView(R.id.drawing)
    DrawingView drawView;
    @BindView(R.id.tb_colors)
    ToggleButton tbColors;
    @BindView(R.id.tb_brush)
    ToggleButton tbBrush;
    @BindView(R.id.tb_eraser)
    ToggleButton tbEraser;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.iv_image_for_paint)
    ImageView ivImageBackgroundForPaint;
    @BindView(R.id.pb_paint)
    ProgressBar pbPaint;
    @BindView(R.id.rl_image)
    RelativeLayout rlImage;
    @BindView(R.id.ll_tools)
    LinearLayout llTools;

    private Context context;
    Unbinder unbinder;
    private Dialog brushDialog;
    private LruCache<String, Bitmap> mMemoryCache;
    private static final String WHITE_COLOR = "#ffffff";

    private String lastColor = "#9b27b0";

    private static final String BITMAP_CASHED_KEY = "bitmap_cashed_key";


    private float smallBrush, mediumBrush, largeBrush;

    public PaintFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewController.getViewController().setPaintFragment(this);
        d("onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paint, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        initListeners();
        return view;
    }


    private void init() {
        ViewController.getViewController().checkInternetConnection();

        context = ViewController.getViewController().getContext();
        setImageFromDatabase();
        setBrushSizes();
        drawView.setBrushSize(smallBrush);
        if (pbPaint != null) {
            pbPaint.getIndeterminateDrawable().setColorFilter(Color.parseColor("#c24fc0"), PorterDuff.Mode.MULTIPLY);
        }
        initBitmapCashLogic();
        rlImage.setDrawingCacheEnabled(true);
    }

    private void initBitmapCashLogic() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(Bitmap bitmap) {
        if (mMemoryCache.get(BITMAP_CASHED_KEY) != null) {
            mMemoryCache.remove(BITMAP_CASHED_KEY);
        }
        mMemoryCache.put(BITMAP_CASHED_KEY, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private void setBrushSizes() {
        smallBrush = context.getResources().getInteger(R.integer.small_size);
        mediumBrush = context.getResources().getInteger(R.integer.medium_size);
        largeBrush = context.getResources().getInteger(R.integer.large_size);
    }

    private void setImageFromDatabase() {
        Glide.with(this)
                .load(DataController.getInstance().getCurrentImage().getImage())
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pbPaint.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbPaint.setVisibility(View.GONE);
                        drawView.setBackground(resource);
                        return false;
                    }
                })
                .into(ivImageBackgroundForPaint);

    }


    private void initListeners() {
        tbColors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    showColorsDialog();
            }
        });

        tbBrush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showBrushDialog();
                }
            }
        });

        tbEraser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showEraseDialog();
                }
            }
        });

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });
    }

    private void showColorsDialog() {

        brushDialog = new Dialog(context);
        brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        brushDialog.setContentView(R.layout.dialog_color_chooser);

        brushDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                resetTB();
            }
        });

        brushDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetTB();
            }
        });

        brushDialog.show();
        customPositionForDialog(brushDialog);
    }

    private void showBrushDialog() {
        brushDialog = new Dialog(context);
        brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        brushDialog.setContentView(R.layout.dialog_brush_chooser);
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);

        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                drawView.setErase(false);
                if(!lastColor.equals(WHITE_COLOR))
                    drawView.setColor(lastColor);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                drawView.setErase(false);
                if(!lastColor.equals(WHITE_COLOR))
                    drawView.setColor(lastColor);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                drawView.setErase(false);
                if(!lastColor.equals(WHITE_COLOR))
                    drawView.setColor(lastColor);
                brushDialog.dismiss();
            }
        });

        brushDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                resetTB();
            }
        });

        brushDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetTB();
            }
        });

        brushDialog.show();
        customPositionForDialog(brushDialog);
    }

    private void showEraseDialog() {
        final Dialog brushDialog = new Dialog(context);
        brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        brushDialog.setContentView(R.layout.dialog_brush_chooser);
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawView.setErase(true);
//                drawView.setBrushSize(smallBrush);
                fakeEraseMode(smallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawView.setErase(true);
//                drawView.setBrushSize(mediumBrush);
                fakeEraseMode(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawView.setErase(true);
//                drawView.setBrushSize(largeBrush);
//                brushDialog.dismiss();
                fakeEraseMode(largeBrush);
                brushDialog.dismiss();

            }
        });


        brushDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                resetTB();
            }
        });

        brushDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetTB();
            }
        });
        brushDialog.show();
        customPositionForDialog(brushDialog);
    }

    private void fakeEraseMode(float size) {
        drawView.setErase(false);
        drawView.setBrushSize(size);
        drawView.setColor(WHITE_COLOR);
//        imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
    }

    private void saveImage() {
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                customSave(rlImage);
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    private void customSave(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        String imgSaved = MediaStore.Images.Media.insertImage(
                context.getContentResolver(), returnedBitmap,
                UUID.randomUUID().toString() + ".png", "drawing");

        if (imgSaved != null) {
            Toast savedToast = Toast.makeText(context,
                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
            savedToast.show();

        } else {
            Toast unsavedToast = Toast.makeText(context,
                    "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
            unsavedToast.show();
//                }
        }

        ViewController.getViewController().loadAds();
    }

    private void customPositionForDialog(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.verticalMargin = 0.08F;
        window.setAttributes(wlp);
    }

    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        ImageButton imgView = (ImageButton) view;
        String color = view.getTag().toString();
        if (!color.equals(WHITE_COLOR)){
            lastColor = color;
        }
        d(color);
        drawView.setColor(color);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        brushDialog.dismiss();
    }

    private void resetTB() {
        if (tbColors.isChecked()) {
            tbColors.setChecked(false);
        }
        if (tbBrush.isChecked()) {
            tbBrush.setChecked(false);
        }
        if (tbEraser.isChecked()) {
            tbEraser.setChecked(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void d(String msg) {
        LogUtils.d(msg);
    }

    public void cashBitmap() {
        Bitmap bm = getViewBitmap(rlImage);
        if (bm != null) {
            addBitmapToMemoryCache(bm);
        }
    }

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public void getBitmapFromCash() {
        Bitmap bitmap = getBitmapFromMemCache(BITMAP_CASHED_KEY);
        if (bitmap != null) {
            drawView.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
        }
    }
}
