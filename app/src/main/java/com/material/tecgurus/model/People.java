package com.material.tecgurus.model;

import android.graphics.drawable.Drawable;

public class People {

    public int image;
    public Drawable imageDrw;
    public String name;
    public String email;
    public boolean section = false;

    public People() {
    }

    public People(String name, boolean section) {
        this.name = name;
        this.section = section;
    }


    @Override
    public String toString() {
        return super.toString();
    }


}
