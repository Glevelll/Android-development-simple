package com.project.foodapp.ui.Cookbook;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
            if (imageButton == null){
                Toast.makeText(requireActivity().getApplicationContext(), "Photo is empty", Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                database = databaseHelper.getWritableDatabase();
                String name = text1.getText().toString();
                String ingredients = text2.getText().toString();
                String process = text3.getText().toString();

                // Convert the selected image to a string
                Drawable drawable = imageButton.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] byteArray = baos.toByteArray();
                String photo = Base64.encodeToString(byteArray, Base64.DEFAULT);

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
        });

        // Add click listener to the image button
        imageButton.setOnClickListener(v -> {
            // Open the gallery to select a photo
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });

        return view;
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