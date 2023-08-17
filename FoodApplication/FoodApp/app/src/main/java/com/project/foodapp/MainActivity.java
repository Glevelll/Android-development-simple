package com.project.foodapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.foodapp.databinding.ActivityMainBinding;
import com.project.foodapp.ui.Cookbook.AddRecipe;
import com.project.foodapp.ui.Cookbook.CookbookFragment;
import com.project.foodapp.ui.Cookbook.FullInfoBook;
import com.project.foodapp.ui.Favorite.FavoriteFragment;
import com.project.foodapp.ui.Favorite.FullInfoFav;
import com.project.foodapp.ui.Recipes.FullInfo;
import com.project.foodapp.ui.Recipes.RecipesFragment;
import com.project.foodapp.ui.Recipes.RecipesList;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    RecipesFragment recipesFragment = new RecipesFragment();
    FavoriteFragment favoriteFragment = new FavoriteFragment();
    CookbookFragment cookbookFragment = new CookbookFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView  = findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,recipesFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_recipes:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, recipesFragment).attach(recipesFragment).commit();
                    return true;
                case R.id.navigation_favorite:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, favoriteFragment).attach(favoriteFragment).commit();
                    return true;
                case R.id.navigation_cookbook:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, cookbookFragment).attach(cookbookFragment).commit();
                    return true;
            }
                return false;
            });
        }
    }