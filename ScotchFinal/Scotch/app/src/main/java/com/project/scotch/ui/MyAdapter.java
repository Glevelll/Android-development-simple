package com.project.scotch.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {

    private String cookie;

    public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String cookie) {
        super(fragmentManager, lifecycle);
        this.cookie = cookie;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new Tab1();
                Bundle args = new Bundle();
                args.putString("cook", cookie);
                tab1.setArguments(args);
                return tab1;
            case 1:
                Fragment tab2 = new Tab2();
                Bundle args2 = new Bundle();
                args2.putString("cook", cookie);
                tab2.setArguments(args2);
                return tab2;
            default:
                throw new IllegalArgumentException("Invalid position " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
