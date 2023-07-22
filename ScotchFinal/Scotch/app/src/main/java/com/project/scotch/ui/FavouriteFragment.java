package com.project.scotch.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.scotch.R;


public class FavouriteFragment extends Fragment {

    private final String[] names = {"Понравились вам", "Понравились вы"};
    private String cookie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager2);

        Bundle bundle = getArguments();
        if (bundle != null) {
            cookie = bundle.getString("cook");
        } else {
            System.out.println("bundle set = null");
        }
        System.out.println(cookie);

        MyAdapter adapter = new MyAdapter(getChildFragmentManager(),getLifecycle(), cookie);
        viewPager2.setAdapter(adapter);
        viewPager2.setSaveEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(names[position])).attach();

        return view;
    }

    public void onResume() {
        super.onResume();
        // Скрываем ProgressBar при отображении фрагмента
        ProgressBar progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

}