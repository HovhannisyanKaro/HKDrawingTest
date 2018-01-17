package com.hk.paintme.hkdrawingtest.interfacies;

/**
 * Created by Hovhannisyan.Karo on 05.08.2017.
 */

public interface Constants {

    String BASE_URL = "https://whereami-a9068.firebaseio.com/paint_me/";

    int WRITE_EXTERNAL_REQUEST = 21;

    interface CATEGORIES_ID {
        int ANIMALS = 0;
        int CARS = 1;
        int NATURE = 2;
        int PEOPLE = 3;
        int OBJECTS = 4;
    }

    interface CATEGORIES_STRING{
        String BASE = "paint_me";
        String ANIMALS = "animals";
        String CARS = "cars";
        String NATURE = "nature";
        String PEOPLE = "people";
        String OBJECTS = "objects";
    }



}
