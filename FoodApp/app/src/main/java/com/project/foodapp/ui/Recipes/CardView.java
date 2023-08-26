package com.project.foodapp.ui.Recipes;

import android.graphics.Bitmap;

public class CardView {
    private String name, ingredients, process;
    private byte[] photoStr;
    private Bitmap photo;

    public CardView(String name, String ingredients, String process, byte[] photoStr, Bitmap photo) {
        this.name = name;
        this.ingredients = ingredients;
        this.process = process;
        this.photoStr = photoStr;
        this.photo = photo;
    }

    public byte[] getPhotoStr() {
        return photoStr;
    }

    public void setPhotoStr(byte[] photoStr) {
        this.photoStr = photoStr;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
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
