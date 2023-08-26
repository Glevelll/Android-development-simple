package com.project.foodapp.ui.Cookbook;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddRecipe extends Fragment {

    FloatingActionButton ftb;
    ImageView imageButton;
    EditText text1, text2, text3;
    Button btn;
    private SQLiteDatabase database;
    private static final int GALLERY_REQUEST_CODE = 1;  // Add constant for gallery request code
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    int transitionCounter = 0;
    byte[] photo = null;
    //Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        ftb = view.findViewById(R.id.fab);
        text1 = view.findViewById(R.id.recipe_name);
        text2 = view.findViewById(R.id.ingredients);
        text3 = view.findViewById(R.id.instructions);
        imageButton = view.findViewById(R.id.standart_photo);
        btn = view.findViewById(R.id.save);
        CookbookFragment cookbookFragment = new CookbookFragment();

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        transitionCounter++;
        System.out.println(transitionCounter);

        if (transitionCounter % 3 == 0) {
            loadInterstitialAd();
            transitionCounter = 0;
        }


        btn.setOnClickListener(v -> {
            if(text1.getText().toString().isEmpty()){
                Toast.makeText(requireActivity().getApplicationContext(), "Field 1 is empty", Toast.LENGTH_SHORT).show();
            }
            if(text2.getText().toString().isEmpty()){
                Toast.makeText(requireActivity().getApplicationContext(), "Field 2 is empty", Toast.LENGTH_SHORT).show();
            }
            if(text3.getText().toString().isEmpty()){
                Toast.makeText(requireActivity().getApplicationContext(), "Field 3 is empty", Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                database = databaseHelper.getWritableDatabase();
                String name = text1.getText().toString();
                String ingredients = text2.getText().toString();
                String process = text3.getText().toString();

                // Check if there is an image in the image button
                if (imageButton.getDrawable() != null) {
                    // Convert the selected image to a string
                    Drawable drawable = imageButton.getDrawable();
                    Bitmap bitmap = null;
                    if (drawable instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        photo = baos.toByteArray();

                        if (photo.length > 2000000) {
                            Toast.makeText(requireActivity().getApplicationContext(), "More than 2 mb", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (drawable instanceof VectorDrawable) {
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                                drawable.getIntrinsicHeight(),
                                Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                    }
                }

                // Запись данных в таблицу CookBook
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("ingredients", ingredients);
                values.put("process", process);
                values.put("photo", photo);

                database.insert("CookBook", null, values);
                Toast.makeText(requireActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                bottomNavigationView.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, cookbookFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ftb.setOnClickListener(v -> {
            bottomNavigationView.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, cookbookFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (transitionCounter % 3 == 0) {
                loadInterstitialAd();
            }
            transitionCounter++;
        });

        // Add click listener to the image button
        imageButton.setOnClickListener(v -> {
            // Open the gallery to select a photo
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            Uri imageUri = data.getData();
            // Set the image in the image button
            imageButton.setImageURI(imageUri);
        }
    }
}