package com.project.foodapp.ui.Recipes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;

public class FullInfo extends Fragment {

    private ImageView imageView;
    private TextView text1, text2, text3;
    private FloatingActionButton ftb;
    private DatabaseHelper databaseHelper;
    private String title, section;
    private String name, ingredients, process, photo;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
            section = bundle.getString("section");
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
                photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"));
                bitmap = stringToBitmap(photo);
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
            cardBundle.putString("name", section);
            recipesList.setArguments(cardBundle);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, recipesList);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            view.setKeepScreenOn(false);
        });

        return view;
    }

    public Bitmap stringToBitmap(String base64String) {
        try {
            // Удаляем префикс "data:image/jpeg;base64,"
            String encodedImage = base64String.substring(base64String.indexOf(",") + 1);

            // Декодируем строку Base64 в массив байт
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

            // Создаем объект Bitmap из массива байт
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}