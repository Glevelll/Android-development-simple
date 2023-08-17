package com.project.foodapp.ui.Favorite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;

public class Tab1 extends Fragment {
    private TextView text1, text2, text3;
    private String name, ingredients, process;

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        text1 = view.findViewById(R.id.name_FIF);
        text2 = view.findViewById(R.id.ingridients2);
        text3 = view.findViewById(R.id.details_aboutFIF);

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
        } else {
            System.out.println("Bundle = null");
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("Favorite", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                process = cursor.getString(cursor.getColumnIndexOrThrow("process"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        text1.setText(name);
        text2.setText(ingredients);
        text3.setText(process);

        return view;
    }
}