package com.project.foodapp.ui.Recipes;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.foodapp.BuildConfig;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;

public class FullInfo extends Fragment {

    private ImageView imageView;
    private TextView text1, text2, text3;
    private FloatingActionButton ftb;
    private DatabaseHelper databaseHelper;
    private String title, section;
    private String name, ingredients, process;
    private byte[] photo;
    private Bitmap bitmap;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    int transitionCounter;

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
        View view = inflater.inflate(R.layout.fragment_full_info, container, false);
        imageView = view.findViewById(R.id.imageViewFIR);
        text1 = view.findViewById(R.id.name_FIR);
        text2 = view.findViewById(R.id.ingridientsFIR);
        text3 = view.findViewById(R.id.details_aboutFIR);
        ftb = view.findViewById(R.id.fab);
        RecipesList recipesList = new RecipesList();

        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
            section = bundle.getString("section");
            transitionCounter = bundle.getInt("count");
        } else {
            System.out.println("Bundle = 0");
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "name",
                "section",
                "ingredients",
                "process",
                "photo"
        };

        String selection = "name = ?";
        String[] selectionArgs = { title };

        Cursor cursor = db.query("Recipes", projection, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                process = cursor.getString(cursor.getColumnIndexOrThrow("process"));
                photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        text1.setText(name);
        text2.setText(ingredients);
        text3.setText(process);

        view.setKeepScreenOn(true);

        ftb.setOnClickListener(v -> {
            transitionCounter++;
            Bundle cardBundle = new Bundle();
            cardBundle.putString("name", section);
            cardBundle.putInt("count", transitionCounter);
            recipesList.setArguments(cardBundle);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, recipesList);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            view.setKeepScreenOn(false);

            transitionCounter++;
            if (transitionCounter % 3 == 0) {
                loadInterstitialAd();
                System.out.println("РЕКЛАМА!!!!!!!");
                transitionCounter = 0;
            }
            System.out.println("Переход 2: " + transitionCounter);
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
}