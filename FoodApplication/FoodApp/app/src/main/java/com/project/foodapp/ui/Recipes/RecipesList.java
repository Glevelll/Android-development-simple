package com.project.foodapp.ui.Recipes;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;
import com.project.foodapp.ui.Favorite.CardFav;
import com.project.foodapp.ui.Favorite.CardFavAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipesList extends Fragment {
    private RecyclerView recyclerView;
    private CardViewAdapter cardViewAdapter;
    FloatingActionButton ftb;
    private DatabaseHelper databaseHelper;
    private String title, name, ingredients, process, photo;
    private Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ftb = view.findViewById(R.id.fab);

        FullInfo fullInfo = new FullInfo();
        RecipesFragment recipesFragment = new RecipesFragment();
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("name");
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
                photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"));
                Bitmap bitmap = stringToBitmap(photo);
                cardList.add(new CardView(name, ingredients, process, photo, bitmap));
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
            String clickedPhoto = clickedCard.getPhotoStr();

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
            fullInfo.setArguments(cardBundle);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fullInfo);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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

    public Bitmap stringToBitmap(String base64String) {
        try {
            // Remove the "data:image/jpeg;base64," prefix
            String encodedImage = base64String.substring(base64String.indexOf(",") + 1);

            // Decode the Base64 string into byte array
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

            // Create a Bitmap object from the byte array
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}