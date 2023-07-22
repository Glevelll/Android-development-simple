package com.project.scotch.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.project.scotch.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Full_InfoFragment extends Fragment {

    private ImageView imageView;
    private String phone;
    private String urlTg;
    private String urlWhatsApp;

    public Full_InfoFragment() {}

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_info, container, false);

        ImageButton button_back = view.findViewById(R.id.button_back);
        imageView = view.findViewById(R.id.imageView1);
        TextView name_profileinfo = view.findViewById(R.id.name_profileinfo);
        TextView age_profileinfo = view.findViewById(R.id.age_profileinfo);
        TextView gender_profle = view.findViewById(R.id.details_gender);
        TextView city_profile = view.findViewById(R.id.details_city);
        ImageButton tg = view.findViewById(R.id.tg);
        ImageButton whatsApp = view.findViewById(R.id.whatsApp);
        TextView about_profile = view.findViewById(R.id.details_about);

        Bundle bundle = getArguments();

        if (bundle == null) {
            Log.d("Full_InfoFragment", "Bundle is null");
            return view;
        }

        String cookie = bundle.getString("cookie");
        Bitmap photo = bundle.getParcelable("photo");
        phone = bundle.getString("phone");
        name_profileinfo.setText(bundle.getString("name"));
        age_profileinfo.setText(bundle.getString("age"));
        gender_profle.setText(bundle.getString("gender"));
        city_profile.setText(bundle.getString("city"));
        about_profile.setText(bundle.getString("about"));

        Glide.with(view.getContext()).load(photo).into(imageView);

        if(phone != null){
            if(isTelegramNumber(phone)){
                tg.setVisibility(View.VISIBLE);
                urlTg = "https://telegram.me/+" + phone.replace(" ", "");
            }
            if(isWhatsAppNumber(phone)){
                whatsApp.setVisibility(View.VISIBLE);
                urlWhatsApp = "https://wa.me/" + phone.replace(" ", "").replace("+", "");
            }
        }

        tg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlTg));
            startActivity(intent);
        });

        whatsApp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlWhatsApp));
            startActivity(intent);
        });

        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("cookie", cookie);
        searchFragment.setArguments(bundle1);

        button_back.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down)
                .remove(Full_InfoFragment.this)
                .addToBackStack(null)
                .commit());
        return view;
    }

    public static boolean isTelegramNumber(String phoneNumber) {
        // Проверка номера телефона на соответствие формату Telegram
        String telegramPattern = "^\\+?[0-9]{1,3}\\s?[0-9]{3}\\s?[0-9]{3}\\s?[0-9]{2}\\s?[0-9]{2}$";
        Pattern pattern = Pattern.compile(telegramPattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isWhatsAppNumber(String phoneNumber) {
        // Проверка номера телефона на соответствие формату WhatsApp
        String whatsappPattern = "^\\+?[0-9]{1,3}\\s?[0-9]{2,3}\\s?[0-9]{2,3}\\s?[0-9]{2}\\s?[0-9]{2}$";
        Pattern pattern = Pattern.compile(whatsappPattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
