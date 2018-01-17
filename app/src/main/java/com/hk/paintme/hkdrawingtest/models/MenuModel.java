package com.hk.paintme.hkdrawingtest.models;

/**
 * Created by Lenovo on 21.06.2017.
 */

public class MenuModel extends ImageModel {

    private String title;

    public MenuModel (){

    }

    public MenuModel(String imageKey, String imageValue, String imageTitle) {
        super(imageKey, imageValue);
        this.title = imageTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
