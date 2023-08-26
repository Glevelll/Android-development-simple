package com.project.foodapp.ui.Recipes;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.ump.ConsentForm;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;
import com.project.foodapp.ui.Favorite.CardFav;
import com.project.foodapp.ui.Favorite.CardFavAdapter;

import org.w3c.dom.Text;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecipesList extends Fragment {
    private RecyclerView recyclerView;
    private CardViewAdapter cardViewAdapter;
    private FloatingActionButton ftb;
    private DatabaseHelper databaseHelper;
    private String title, name, ingredients, process;
    private byte[] photo;
    private AdView mAdView;
    Bitmap bitmap;

    int transitionCounter;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, "Не загружено");
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ftb = view.findViewById(R.id.fab);

        MobileAds.initialize(requireContext(), initializationStatus -> {});

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FullInfo fullInfo = new FullInfo();
        RecipesFragment recipesFragment = new RecipesFragment();
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
            transitionCounter = bundle.getInt("count");
        } else {
            System.out.println("Bundle = 0");
        }


        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.frame1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<CardView> cardList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "name",
                "section",
                "ingredients",
                "process",
                "photo"
        };

        String selection = "section = ?";
        String[] selectionArgs = { title };

        Cursor cursor = db.query("Recipes", projection, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                process = cursor.getString(cursor.getColumnIndexOrThrow("process"));
                photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                if (photo != null) {
                    bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                    cardList.add(new CardView(name, ingredients, process, photo, bitmap));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        cardViewAdapter = new CardViewAdapter(cardList);
        recyclerView.setAdapter(cardViewAdapter);

        cardViewAdapter.setOnAddInfoClickListener(position -> {
            CardView clickedCard = cardList.get(position);
            String clickedName = clickedCard.getName();
            String clickedIngredients = clickedCard.getIngredients();
            String clickedProcess = clickedCard.getProcess();
            byte[] clickedPhoto = clickedCard.getPhotoStr();

            SQLiteDatabase db1 = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", clickedName);
            values.put("ingredients", clickedIngredients);
            values.put("process", clickedProcess);
            values.put("photo", clickedPhoto);
            values.put("notes", "");
            db1.insert("Favorite", null, values);
            System.out.println(db1);
            db1.close();

        });

        cardViewAdapter.setOnClickListener(position -> {
            CardView clickedCard = cardList.get(position);
            String clickedName = clickedCard.getName();

            Bundle cardBundle = new Bundle();
            cardBundle.putString("name", clickedName);
            cardBundle.putString("section", title);
            cardBundle.putInt("count", transitionCounter);
            fullInfo.setArguments(cardBundle);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fullInfo);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            transitionCounter++;
            if (transitionCounter % 3 == 0) {
                loadInterstitialAd();
                System.out.println("РЕКЛАМА!!!!!!!");
                transitionCounter = 0;
            }
            System.out.println("Переход 1: " + transitionCounter);
        });

        ftb.setOnClickListener(v -> {
            bottomNavigationView.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, recipesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }

    private void loadInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}