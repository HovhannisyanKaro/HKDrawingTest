package com.hk.paintme.hkdrawingtest.models;

import java.io.Serializable;

/**
 * Created by Hovhannisyan.Karo on 21.06.2017.
 */

public class ImageModel implements Serializable{

    private String key;
    private String image;

    public ImageModel() {
    }

    public ImageModel(String key, String image) {
        this.key = key;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return "ImageModel{" +
                "key='" + key + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
