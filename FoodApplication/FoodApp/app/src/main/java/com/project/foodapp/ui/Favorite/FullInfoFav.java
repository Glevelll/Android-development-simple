package com.project.foodapp.ui.Favorite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;
import com.project.foodapp.ui.Cookbook.CookbookFragment;

public class FullInfoFav extends Fragment {

    private FloatingActionButton ftb;
    private String title;
    private ImageView imageView;
    private TextView text1, text2, text3;
    private DatabaseHelper databaseHelper;
    private EditText editText;
    private Button btn;
    private String name, ingredients, process, photo, notes;
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
        View view = inflater.inflate(R.layout.fragment_full_info_fav, container, false);
        ftb = view.findViewById(R.id.fab2);
        imageView = view.findViewById(R.id.imageViewFIF);
        text1 = view.findViewById(R.id.name_FIR);
        text2 = view.findViewById(R.id.ingridientsFIR);
        text3 = view.findViewById(R.id.details_aboutFIR);
        btn = view.findViewById(R.id.save_button);
        editText = view.findViewById(R.id.input_text);

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
        } else {
            System.out.println("Bundle = 0");
        }

        FavoriteFragment favoriteFragment = new FavoriteFragment();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "name",
                "ingredients",
                "process",
                "photo",
                "notes"
        };

        String selection = "name = ?";
        String[] selectionArgs = { title };

        Cursor cursor = db.query("Favorite", projection, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                process = cursor.getString(cursor.getColumnIndexOrThrow("process"));
                photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"));
                notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));
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
        editText.setText(notes);

        view.setKeepScreenOn(true);

        btn.setOnClickListener(c -> {
            String newNotes = editText.getText().toString();

            SQLiteDatabase dbUpdate = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("notes", newNotes);

            String updateSelection = "name = ?";
            String[] updateSelectionArgs = { title };

            int updateCount = dbUpdate.update("Favorite", values, updateSelection, updateSelectionArgs);
            if (updateCount == 0) {
                // Запись не существует, вставляем новую
                values.put("name", title);
                long newRowId = dbUpdate.insert("Favorite", null, values);
            }

            dbUpdate.close();
        });

        ftb.setOnClickListener(v -> {
            bottomNavigationView.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, favoriteFragment);
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