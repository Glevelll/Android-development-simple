package com.project.scotch.ui;

import android.graphics.Bitmap;

public class Cards {
    private String name;
    private String age;
    private String gender;
    private Bitmap imageView;

    public Cards(String name, String age, String gender, Bitmap imageView) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.imageView = imageView;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Bitmap getImageView() {
        return imageView;
    }

    public void setImageView(Bitmap imageView) {
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


