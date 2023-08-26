package com.project.foodapp.ui.Recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.foodapp.R;
import com.project.foodapp.databinding.FragmentRecipesBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    private FragmentRecipesBinding binding;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.frame);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecipesList recipesListFragment = new RecipesList();

        List<Card> cardList = new ArrayList<>();

        // Change the code here to add images from the drawable folder to the cards
        cardList.add(new Card(R.drawable.first, "Breakfast"));
        cardList.add(new Card(R.drawable.second, "Lunch"));
        cardList.add(new Card(R.drawable.third, "Dinner"));
        cardList.add(new Card(R.drawable.fourth, "Appetizer"));
        cardList.add(new Card(R.drawable.fivth, "Salad"));
        cardList.add(new Card(R.drawable.sixth, "Soup"));
        cardList.add(new Card(R.drawable.seventh, "Dessert"));
        cardList.add(new Card(R.drawable.eighth, "Vegan"));
        cardList.add(new Card(R.drawable.nineth, "Drink"));

        cardAdapter = new CardAdapter(cardList);
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnClickListener(position -> {
            Card card = cardList.get(position);
            String name = card.getName();

            Bundle bundle = new Bundle();
            bundle.putString("name", name);

            recipesListFragment.setArguments(bundle);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, recipesListFragment);
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