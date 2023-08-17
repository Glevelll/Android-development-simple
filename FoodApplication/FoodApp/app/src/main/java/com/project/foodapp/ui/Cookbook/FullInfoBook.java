package com.project.foodapp.ui.Cookbook;

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
import android.widget.ImageView;
import android.widget.TextView;

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
    private String name, ingredients, process, photo;
    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
        } else {
            System.out.println("Bundle = 0");
        }

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
            bottomNavigationView.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, cookbookFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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