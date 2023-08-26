package com.project.foodapp.ui.Cookbook;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.foodapp.DatabaseHelper;
import com.project.foodapp.R;
import com.project.foodapp.databinding.FragmentCookbookBinding;
import com.project.foodapp.ui.Favorite.CardFav;
import com.project.foodapp.ui.Favorite.CardFavAdapter;
import com.project.foodapp.ui.Favorite.FullInfoFav;

import java.util.ArrayList;
import java.util.List;

public class CookbookFragment extends Fragment {

    private FragmentCookbookBinding binding;

    CardView cardView;
    ImageView imageView;
    TextView text1;
    private RecyclerView recyclerView;
    private CardBookAdapter cardBookAdapter;
    FloatingActionButton btnact;
    private Button btn;
    private DatabaseHelper databaseHelper;
    Bitmap bitmap;
    int transitionCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCookbookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = root.findViewById(R.id.imageViewFIC);
        text1 = root.findViewById(R.id.name_FIC);
        btnact = root.findViewById(R.id.fab);

        Bundle bundle = getArguments();
        if (bundle != null) {
            transitionCounter = bundle.getInt("count");
        } else {
            System.out.println("Bundle = 0");
        }

        FullInfoBook fullInfoBook = new FullInfoBook();
        AddRecipe addRecipe = new AddRecipe();

        recyclerView = root.findViewById(R.id.frame3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<CardBook> cardList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("CookBook", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                if(photo != null) {
                    bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                }
                cardList.add(new CardBook(name, bitmap));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        cardBookAdapter = new CardBookAdapter(cardList);
        recyclerView.setAdapter(cardBookAdapter);

        cardBookAdapter.setOnDeleteInfoClickListener(position -> {
            CardBook card = cardList.get(position);
            SQLiteDatabase deleteDb = databaseHelper.getWritableDatabase();
            deleteDb.delete("CookBook", "name=?", new String[]{card.getName()});
            deleteDb.close();

            cardList.remove(position);
            cardBookAdapter.notifyItemRemoved(position);
        });

        cardBookAdapter.setOnClickListener((v, position) -> {
            CardBook clickedCard = cardList.get(position);
            String clickedName = clickedCard.getName();

            Bundle cardBundle = new Bundle();
            cardBundle.putString("name", clickedName);
            cardBundle.putInt("count", transitionCounter);
            fullInfoBook.setArguments(cardBundle);

            System.out.println("Переход 2: " + transitionCounter);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fullInfoBook);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        btnact.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,addRecipe)
                .commit());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}