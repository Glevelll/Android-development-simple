package com.project.foodapp.ui.Favorite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;
import com.project.foodapp.databinding.FragmentFavoriteBinding;
import com.project.foodapp.ui.Recipes.Card;
import com.project.foodapp.ui.Recipes.CardAdapter;
import com.project.foodapp.ui.Recipes.CardView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private RecyclerView recyclerView;
    private CardFavAdapter cardFavAdapter;
    private Button btn;
    private DatabaseHelper databaseHelper;
    int transitionCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btn = requireActivity().findViewById(R.id.delete);

        Bundle bundle = getArguments();
        if (bundle != null) {
            transitionCounter = bundle.getInt("count");

        } else {
            System.out.println("Bundle = 0");
        }

        FullInfoFav fullInfoFav = new FullInfoFav();

        recyclerView = root.findViewById(R.id.frame2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<CardFav> cardFav = new ArrayList<>();

        // Get the data from the Favorite table
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("Favorite", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                cardFav.add(new CardFav(name, bitmap));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        cardFavAdapter = new CardFavAdapter(cardFav);
        recyclerView.setAdapter(cardFavAdapter);

        cardFavAdapter.setOnDeleteInfoClickListener(position -> {
            CardFav card = cardFav.get(position);
            SQLiteDatabase deleteDb = databaseHelper.getWritableDatabase();
            deleteDb.delete("Favorite", "name=?", new String[]{card.getName()});
            deleteDb.close();

            cardFav.remove(position);
            cardFavAdapter.notifyItemRemoved(position);
        });

        cardFavAdapter.setOnClickListener((v, position) -> {
            CardFav clickedCard = cardFav.get(position);
            String clickedName = clickedCard.getName();

            Bundle cardBundle = new Bundle();
            cardBundle.putString("name", clickedName);
            cardBundle.putInt("count", transitionCounter);
            fullInfoFav.setArguments(cardBundle);

            System.out.println("Переход 2: " + transitionCounter);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fullInfoFav);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}