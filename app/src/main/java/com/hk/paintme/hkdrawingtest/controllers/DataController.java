package com.hk.paintme.hkdrawingtest.controllers;

import com.hk.paintme.hkdrawingtest.models.ImageModel;

import java.util.ArrayList;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class DataController {

    private static DataController dataController = null;
    private int category;
    private ImageModel currentImage;
    private boolean isPaintMode;
    private ArrayList<String> drawTutorialData;
    private boolean loaderRunning;


    private DataController (){}

    public static DataController getInstance(){
        if (dataController==null){
            dataController =  new DataController();
        }
        return dataController;
    }


    public boolean isLoaderRunning() {
        return loaderRunning;
    }

    public void setLoaderRunning(boolean loaderRunning) {
        this.loaderRunning = loaderRunning;
    }

    public ArrayList<String> getDrawTutorialData() {
        return drawTutorialData;
    }

    public void setDrawTutorialData(ArrayList<String> drawTutorialData) {
        this.drawTutorialData = drawTutorialData;
    }

    public boolean isPaintMode() {
        return isPaintMode;
    }

    public void setPaintMode(boolean paintMode) {
        isPaintMode = paintMode;
    }

    public ImageModel getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(ImageModel currentImage) {
        this.currentImage = currentImage;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
