package com.project.scotch;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.scotch.ui.FavouriteFragment;
import com.project.scotch.ui.ProfileFragment;
import com.project.scotch.ui.SearchFragment;

public class Menu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    String cookie;
    public ProgressBar mProgressBar;

    ProfileFragment profileFragment = new ProfileFragment();
    SearchFragment searchFragment = new SearchFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        cookie = getIntent().getStringExtra("cookie");

        Bundle bundle = new Bundle();
        bundle.putString("cookie", cookie);
        profileFragment.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("cookie", cookie);
        searchFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString("cook", cookie);
        favouriteFragment.setArguments(bundle3);

        bottomNavigationView  = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_profile:
                    mProgressBar = findViewById(R.id.progress_bar);
                    View profileView = profileFragment.getView();
                    if (profileView == null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        profileView.setVisibility(View.VISIBLE);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).attach(profileFragment).commit();
                    View searchView = searchFragment.getView();
                    if (searchView != null) {
                        searchView.setVisibility(View.INVISIBLE);
                    }
                    View favView = favouriteFragment.getView();
                    if (favView != null) {
                        favView.setVisibility(View.INVISIBLE);
                    }
                    return true;
                case R.id.navigation_search:
                    mProgressBar = findViewById(R.id.progress_bar);
                    profileView = profileFragment.getView();
                    if (profileView != null) {
                        profileView.setVisibility(View.INVISIBLE);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,searchFragment).attach(searchFragment).commit();
                    searchView = searchFragment.getView();
                    if (searchView == null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        searchView.setVisibility(View.VISIBLE);
                    }
                    favView = favouriteFragment.getView();
                    if (favView != null) {
                        favView.setVisibility(View.INVISIBLE);
                    }
                    return true;
                case R.id.navigation_favourite:
                    mProgressBar = findViewById(R.id.progress_bar);
                    profileView = profileFragment.getView();
                    if (profileView != null) {
                        profileView.setVisibility(View.INVISIBLE);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,favouriteFragment).attach(favouriteFragment).commit();
                    searchView = searchFragment.getView();
                    if (searchView != null) {
                        searchView.setVisibility(View.INVISIBLE);
                    }
                    favView = favouriteFragment.getView();
                    if (favView == null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        favView.setVisibility(View.VISIBLE);
                    }
                    return true;
            }
            return false;
        });
    }
}