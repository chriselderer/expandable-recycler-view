package com.elder.expandablerecyclerviewdemo.models;

import android.graphics.drawable.Drawable;

/**
 * Created by chris on 2017-06-13.
 */

public class Animal
{
    //================================================================================
    // region VARIABLES

    private Drawable picture;
    private String name;
    private int speed;
    private int fluffFactor;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public Animal(Drawable picture, String name, int breed, int fluffFactor)
    {
        this.name = name;
        this.picture = picture;
        this.speed = breed;
        this.fluffFactor = fluffFactor;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region GETTER & SETTER

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable dogPicture) {
        this.picture = dogPicture;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getFluffFactor() {
        return fluffFactor;
    }

    public void setFluffFactor(int fluffFactor) {
        this.fluffFactor = fluffFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // endregion
    //================================================================================

}
