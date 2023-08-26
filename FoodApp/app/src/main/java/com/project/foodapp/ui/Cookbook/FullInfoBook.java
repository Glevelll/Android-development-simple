package com.project.foodapp.ui.Cookbook;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;

public class FullInfoBook extends Fragment {

    private FloatingActionButton ftb;
    private String title;
    private ImageView imageView;
    private TextView text1, text2, text3;
    private DatabaseHelper databaseHelper;
    private String name, ingredients, process;
    private byte[] photo;
    Bitmap bitmap;
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
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_info_book, container, false);
        ftb = view.findViewById(R.id.fab);
        imageView = view.findViewById(R.id.imageViewFIC);
        text1 = view.findViewById(R.id.name_FIC);
        text2 = view.findViewById(R.id.ingridientsFIC);
        text3 = view.findViewById(R.id.details_aboutFIC);

        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        InterstitialAd.load(requireContext(),"ca-app-pub-7302291319685068/8526903533", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
            transitionCounter = bundle.getInt("count");
        } else {
            System.out.println("Bundle = 0");
        }

        transitionCounter++;

        CookbookFragment cookbookFragment = new CookbookFragment();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "name",
                "ingredients",
                "process",
                "photo"
        };

        String selection = "name = ?";
        String[] selectionArgs = { title };

        Cursor cursor = db.query("CookBook", projection, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                process = cursor.getString(cursor.getColumnIndexOrThrow("process"));
                photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                if(photo != null) {
                    bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                }
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

            Bundle cardBundle = new Bundle();
            cardBundle.putInt("count", transitionCounter);
            cookbookFragment.setArguments(cardBundle);

            bottomNavigationView.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, cookbookFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (transitionCounter % 3 == 0) {
                loadInterstitialAd();
                System.out.println("Реклама!!!!");
                transitionCounter = 0;
            }
            System.out.println("ПЕРЕХОД 1: " + transitionCounter);
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