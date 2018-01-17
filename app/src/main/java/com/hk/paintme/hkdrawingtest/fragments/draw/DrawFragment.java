package com.hk.paintme.hkdrawingtest.fragments.draw;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hk.paintme.hkdrawingtest.DrawingView;
import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.adapters.LearnToDrawStepsAdapter;
import com.hk.paintme.hkdrawingtest.controllers.DataController;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.interfacies.OnPageSelectedListener;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DrawFragment extends Fragment implements OnPageSelectedListener {

    Unbinder unbinder;
    @BindView(R.id.tb_draw_check_state)
    ToggleButton tbDrawCheckState;
    @BindView(R.id.vp_learn_to_draw)
    ViewPager vpLearnToDraw;
    @BindView(R.id.rl_draw_tutorial_draw_mode)
    RelativeLayout rlDrawMode;
    @BindView(R.id.ll_tools)
    LinearLayout llTools;
    @BindView(R.id.rg_sections)
    RadioGroup rgSections;
    @BindView(R.id.tb_colors)
    ToggleButton tbColors;
    @BindView(R.id.tb_brush)
    ToggleButton tbBrush;
    @BindView(R.id.tb_eraser)
    ToggleButton tbEraser;
    @BindView(R.id.drawing)
    DrawingView drawView;
    @BindView(R.id.rl_tools_container)
    LinearLayout rlToolsContainer;
    @BindView(R.id.iv_save_draw)
    ImageView ivSave;

    private LearnToDrawStepsAdapter adapter;
    private Dialog brushDialog;
    private float smallBrush, mediumBrush, largeBrush;
    private float smallBrushErase, mediumBrushErase, largeBrushErase;
    private Context context;


    public DrawFragment() {
    }



    @Override
    public void onResume() {
        super.onResume();
        ViewController.getViewController().setDrawFragment(this);
        context = ViewController.getViewController().getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = ViewController.getViewController().getContext();
        init();
        initListeners();
        return view;
    }

    private void init() {
        ViewController.getViewController().checkInternetConnection();
        setAdapter();
        setBrushSizes();
        createCustomRadioGroup();

        drawView.setBrushSize(smallBrush);
        llTools.setVisibility(View.GONE);
    }

    private void setBrushSizes() {
        smallBrush = context.getResources().getInteger(R.integer.small_size_draw);
        mediumBrush = context.getResources().getInteger(R.integer.medium_size_draw);
        largeBrush = context.getResources().getInteger(R.integer.large_size_draw);

        smallBrushErase = context.getResources().getInteger(R.integer.small_size);
        mediumBrushErase = context.getResources().getInteger(R.integer.medium_size);
        largeBrushErase = context.getResources().getInteger(R.integer.large_size);
    }

    private void setAdapter() {
        adapter = new LearnToDrawStepsAdapter(this, DataController.getInstance().getDrawTutorialData());
        vpLearnToDraw.setAdapter(adapter);
    }

    private void createCustomRadioGroup() {
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 16, 8, 16);
        int length = DataController.getInstance().getDrawTutorialData().size();
        final RadioButton[] rb = new RadioButton[length];
        RadioGroup rg = new RadioGroup(context); //create the RadioGroup
        rg.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < length; i++) {
            rb[i] = new RadioButton(context);
            rb[i].setId(i + 100);
            rb[i].setButtonDrawable(R.drawable.selector_radio_button);
            rb[i].setLayoutParams(layoutParams);
            rg.addView(rb[i]);
        }
        rg.setLayoutParams(layoutParams);
        rlToolsContainer.addView(rg);//you add the whole RadioGroup to the layout
    }

    private void initListeners() {
        tbDrawCheckState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rlDrawMode.setVisibility(View.VISIBLE);
                    llTools.setVisibility(View.VISIBLE);
                    rlToolsContainer.setVisibility(View.GONE);

                } else {
                    rlDrawMode.setVisibility(View.GONE);
                    llTools.setVisibility(View.GONE);
                    rlToolsContainer.setVisibility(View.VISIBLE);
                }
            }
        });

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

    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        ImageButton imgView = (ImageButton) view;
        String color = view.getTag().toString();
        drawView.setColor(color);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        brushDialog.dismiss();
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

    private void saveImage() {
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //save drawing
                drawView.setDrawingCacheEnabled(true);

                String imgSaved = MediaStore.Images.Media.insertImage(
                        context.getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "drawing");


                if (imgSaved != null) {
                    Toast savedToast = Toast.makeText(context,
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                    ViewController.getViewController().loadAds();
                } else {
                    Toast unsavedToast = Toast.makeText(context,
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }

                drawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    private void showEraseDialog() {
        final Dialog brushDialog = new Dialog(context);
        brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        brushDialog.setContentView(R.layout.dialog_brush_chooser);
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(smallBrushErase);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(mediumBrushErase);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(largeBrushErase);
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

    private void customPositionForDialog(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.verticalMargin = 0.08F;
        window.setAttributes(wlp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPageSelected(View view, int position) {

    }
}
