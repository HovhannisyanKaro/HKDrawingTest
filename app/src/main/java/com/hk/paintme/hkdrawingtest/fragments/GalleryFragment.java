package com.hk.paintme.hkdrawingtest.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hk.paintme.hkdrawingtest.R;
import com.hk.paintme.hkdrawingtest.adapters.ChooseImageAdapter;
import com.hk.paintme.hkdrawingtest.controllers.DataController;
import com.hk.paintme.hkdrawingtest.controllers.ViewController;
import com.hk.paintme.hkdrawingtest.fragments.draw.DrawFragment;
import com.hk.paintme.hkdrawingtest.fragments.paint.PaintFragment;
import com.hk.paintme.hkdrawingtest.interfacies.Constants;
import com.hk.paintme.hkdrawingtest.interfacies.OnPageSelectedListener;
import com.hk.paintme.hkdrawingtest.models.ImageModel;
import com.hk.paintme.hkdrawingtest.utils.LogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryFragment extends Fragment {

    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.toolbar_category)
    Toolbar toolbarCategory;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    Unbinder unbinder;
    @BindView(R.id.iv_category_go_back)
    ImageView ivCategoryGoBack;

    private ChooseImageAdapter adapter;
    private ArrayList<ImageModel> imagesData = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private ArrayList<ArrayList<String>> drawData;

    public GalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        ViewController.getViewController().startLoader();
        init(view);
        initListeners();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initListeners() {
        ivCategoryGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewController.getViewController().goBackFF();
            }
        });


    }

    private void init(View view) {
        ViewController.getViewController().checkInternetConnection();
        readDataFromFireBase();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        understandingCategory();
    }

    private void understandingCategory() {
        int colorRes = 0;
        String titleRes = "";
        Activity context = ViewController.getViewController().getContext();
        if (DataController.getInstance().isPaintMode()){
            int category = DataController.getInstance().getCategory();


            switch (category) {
                case Constants.CATEGORIES_ID.ANIMALS:
                    colorRes = ContextCompat.getColor(context, R.color.section_animal_color);
                    titleRes = context.getString(R.string.animals);
                    break;
                case Constants.CATEGORIES_ID.CARS:
                    colorRes = ContextCompat.getColor(context, R.color.section_cars_color);
                    titleRes = context.getString(R.string.cars);

                    break;
                case Constants.CATEGORIES_ID.NATURE:
                    colorRes = ContextCompat.getColor(context, R.color.section_nature_color);
                    titleRes = context.getString(R.string.nature);

                    break;
                case Constants.CATEGORIES_ID.PEOPLE:
                    colorRes = ContextCompat.getColor(context, R.color.section_people_color);
                    titleRes = context.getString(R.string.people);

                    break;
                case Constants.CATEGORIES_ID.OBJECTS:
                    colorRes = ContextCompat.getColor(context, R.color.section_objects_color);
                    titleRes = context.getString(R.string.objects);
                    break;
            }
        }else{
            colorRes = ContextCompat.getColor(context, R.color.section_nature_color);
            titleRes = context.getString(R.string.draw);
        }

        toolbarCategory.setBackgroundColor(colorRes);
        tvToolbarTitle.setText(titleRes);
    }


    private void readDataFromFireBase() {
        String url = Constants.BASE_URL;
        database = FirebaseDatabase.getInstance();

        if (DataController.getInstance().isPaintMode()) {
            int category = category = DataController.getInstance().getCategory();
            switch (category) {
                case 0:
                    url = url + "/" + Constants.CATEGORIES_STRING.ANIMALS;
                    break;
                case 1:
                    url = url + "/" + Constants.CATEGORIES_STRING.CARS;

                    break;
                case 2:
                    url = url + "/" + Constants.CATEGORIES_STRING.NATURE;

                    break;
                case 3:
                    url = url + "/" + Constants.CATEGORIES_STRING.PEOPLE;

                    break;
                case 4:
                    url = url + "/" + Constants.CATEGORIES_STRING.OBJECTS;
                    break;
            }
            myRef = database.getReferenceFromUrl(url);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        String image = snapshot.getValue(String.class);
                        LogUtils.d("onDataChange image  == " + image);
                        imagesData.add(new ImageModel(key, image));
                    }

                    setCategoryAdapter();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    LogUtils.d("onCancelled " + error.toException());

                }


            });
        } else {
            url = url + "/" + "draw" + "/";

            myRef = database.getReferenceFromUrl(url);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    if (drawData != null && !drawData.isEmpty()) {
                        drawData.clear();
                        drawData.trimToSize();
                        drawData = null;
                    }
                    drawData = new ArrayList<ArrayList<String>>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().getKey();
                        long mainCount = 0;
                        ArrayList<String> currDrawData = new ArrayList<String>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            long count = snapshot.getChildrenCount();
                            mainCount++;
                            currDrawData.add(snapshot1.getValue(String.class));
                            if (mainCount == count) {
                                String key = snapshot1.getKey();
                                String image = snapshot1.getValue(String.class);
                                LogUtils.d("onDataChange key  == " + key);
                                LogUtils.d("onDataChange image  == " + image);
                                imagesData.add(new ImageModel(key, image));
                            }
                        }
                        drawData.add(currDrawData);
                    }
                    setCategoryAdapter();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    LogUtils.d("onCancelled " + error.toException());

                }
            });
        }
    }

    private void setCategoryAdapter() {
        try{
            adapter = new ChooseImageAdapter(imagesData);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (ViewController.getViewController().checkInternetConnection()){
                        if (!DataController.getInstance().isPaintMode()) {
                            LogUtils.d(drawData.toString() + " size " + drawData.size());

                            DataController.getInstance().setDrawTutorialData(drawData.get(i));
                            ViewController.getViewController().addFragment(new DrawFragment());
                        } else {
                            DataController.getInstance().setCurrentImage(imagesData.get(i));
                            ViewController.getViewController().addFragment(new PaintFragment());
                        }
                    }
                }
            });
            ViewController.getViewController().stopLoader();
        }catch (Exception e){
            //Todo change this.
            ViewController.getViewController().stopLoader();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
