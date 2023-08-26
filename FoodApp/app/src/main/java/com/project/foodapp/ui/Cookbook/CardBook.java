package com.project.foodapp.ui.Cookbook;

import android.graphics.Bitmap;

public class CardBook {
    private String name;
    private Bitmap photo;

    public CardBook(String name, Bitmap photo) {
        this.name = name;
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
