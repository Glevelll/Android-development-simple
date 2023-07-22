package com.project.scotch.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.project.scotch.SignIn;
import com.transitionseverywhere.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.divider.MaterialDivider;
import com.project.scotch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class ProfileFragment extends Fragment {
    TextView about, city, gender, login, phone, age,
            name, mylike,countmylike,sentlike,countsentlike;
    int id, ageInt;
    ConstraintLayout layout, layout_like;
    ImageView photo, imageheartone, imagehearttwo;
    CardView cardViewFirst, cardViewSecond;
    Button settings, sign_out;
    MaterialDivider materialDivider;
    String cookie, loginStr, nameStr, ageStr, genderStr, cityStr, phone_numberStr, aboutStr;
    byte[] imageBytes;
    Bitmap bitmap;
    ArrayList<Integer> listIdP = new ArrayList<>();
    ArrayList<Integer> listIdG = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SettingsFragment settingsFragment = new SettingsFragment();
        SearchFragment searchFragment = new SearchFragment();

        //Нужна оптимиация по времени перехода с окон поиска и избранного на профиль
        Bundle bundle = getArguments();
        if (bundle != null) {
            cookie = bundle.getString("cookie");
            loginStr = bundle.getString("login");
            nameStr = bundle.getString("name");
            ageStr = bundle.getString("age");
            genderStr = bundle.getString("gender");
            cityStr = bundle.getString("city");
            phone_numberStr = bundle.getString("phone_number");
            aboutStr = bundle.getString("about");
            imageBytes = bundle.getByteArray("image");
        } else {
            System.out.println("bundle = null");
        }

        try {
            URL url = new URL("http://45.143.93.102/users/current");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // получение данных ответа
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                // обработка данных ответа
                String userInfo = response.toString();
                System.out.println(responseCode + " " + userInfo);
                String[] parts = userInfo.split("\\{", 2);

                String jsonStr = "{" + parts[1];
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    id = json.getInt("id");
                    loginStr = json.getString("login");
                    nameStr = json.getString("name");
                    ageInt = json.getInt("age");
                    genderStr = json.getString("gender");
                    cityStr = json.getString("city");
                    phone_numberStr = json.getString("phone_number");
                    aboutStr = json.getString("about");

                    ageStr = Integer.toString(ageInt);

                } catch (JSONException e) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    System.out.println("Запрос не обработан");
                }
            } else {
                System.out.println(responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            System.out.println("Соединение прервано");
        }

        try {
            URL url = new URL("http://45.143.93.102//photos/current");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Content-Type", "image/jpeg");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            imageBytes = outputStream.toByteArray();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            System.out.println("Соединение прервано");
        }

        System.out.println(loginStr + " " + nameStr + " " + ageStr + " " + genderStr + " " + cityStr + " " + phone_numberStr + " " + aboutStr + " " + Arrays.toString(imageBytes));

        Bundle bundle1 = new Bundle();
        bundle1.putString("cookie", cookie);
        bundle1.putString("login", loginStr);
        bundle1.putString("name", nameStr);
        bundle1.putString("age", ageStr);
        bundle1.putString("gender", genderStr);
        bundle1.putString("city", cityStr);
        bundle1.putString("phone_number", phone_numberStr);
        bundle1.putString("about", aboutStr);
        bundle1.putByteArray("image", imageBytes);
        settingsFragment.setArguments(bundle1);
        searchFragment.setArguments(bundle1);

        if(imageBytes != null) {
            bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            Toast.makeText(requireActivity().getApplicationContext(),"Фото не загружено", Toast.LENGTH_SHORT).show();
        }

        login = view.findViewById(R.id.login_profile);
        name = view.findViewById(R.id.name_profile);
        age = view.findViewById(R.id.age_profile);
        gender = view.findViewById(R.id.details_gender);
        city = view.findViewById(R.id.details_city);
        phone = view.findViewById(R.id.phone_profile);
        about = view.findViewById(R.id.details_about);
        photo = view.findViewById(R.id.imageView1);

        if (bitmap != null) {
            photo.setImageBitmap(bitmap);
        }
        login.setText(loginStr);
        name.setText(nameStr);
        age.setText(ageStr);
        if(Objects.equals(genderStr, "male")){
            genderStr = "Муж";
        } else {
            genderStr = "Жен";
        }
        gender.setText(genderStr);
        city.setText(cityStr);
        phone.setText(phone_numberStr);
        about.setText(aboutStr);

        listIdG.clear();
        listIdP.clear();

        try {
            URL url = new URL("http://45.143.93.102/users/liked");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // получение данных ответа
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                // обработка данных ответа
                String userInfo = response.toString();
                System.out.println(responseCode + " " + userInfo);
                JSONArray jsonArray = new JSONArray(userInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    int id = json.getInt("id");
                    listIdP.add(id);
                }
            } else {
                System.out.println(responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            System.out.println("Соединение прервано");
        } catch (JSONException e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            System.out.println("Запрос не обработан");
        }

        try {
            URL url = new URL("http://45.143.93.102/users/liked_by");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // получение данных ответа
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                // обработка данных ответа
                String userInfo = response.toString();
                System.out.println(responseCode + " " + userInfo);
                JSONArray jsonArray = new JSONArray(userInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    int id = json.getInt("id");
                    listIdG.add(id);
                }
            } else {
                System.out.println(responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            System.out.println("Соединение прервано");
        } catch (JSONException e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            System.out.println("Запрос не обработан");
        }


        //first card
        city = view.findViewById(R.id.details_city);
        gender = view.findViewById(R.id.details_gender);
        about = view.findViewById(R.id.details_about);
        settings = view.findViewById(R.id.settings);
        sign_out = view.findViewById(R.id.sign_out);
        layout = view.findViewById(R.id.layoutCardProfile);
        cardViewFirst = view.findViewById(R.id.cardviewprofile);
        photo = view.findViewById(R.id.imageView1);
        login = view.findViewById(R.id.login_profile);
        phone = view.findViewById(R.id.phone_profile);
        age = view.findViewById(R.id.age_profile);
        name = view.findViewById(R.id.name_profile);
        materialDivider = view.findViewById(R.id.materialDivider);

        //second card
        cardViewSecond = view.findViewById(R.id.received_likes);
        layout_like = view.findViewById(R.id.layoutlike);
        mylike = view.findViewById(R.id.mylike);
        imageheartone = view.findViewById(R.id.imageheartone);
        countmylike = view.findViewById(R.id.countmylike);
        sentlike = view.findViewById(R.id.sentlike);
        imagehearttwo = view.findViewById(R.id.imagehearttwo);
        countsentlike = view.findViewById(R.id.countsentlike);

        countmylike.setText(String.valueOf(listIdG.size()));
        countsentlike.setText(String.valueOf(listIdP.size()));

        cardViewSecond.setOnClickListener(v -> {
            int visible = (mylike.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
            int gone = (sentlike.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
            TransitionManager.beginDelayedTransition(layout_like, new Fade().setDuration(600));
            mylike.setVisibility(visible);
            imageheartone.setVisibility(visible);
            countmylike.setVisibility(visible);
            sentlike.setVisibility(gone);
            imagehearttwo.setVisibility(gone);
            countsentlike.setVisibility(gone);
        });

        settings.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,settingsFragment)
                .commit());

        sign_out.setOnClickListener(v -> {
            try {
                // Создаем объект URL для отправки DELETE-запроса на сервер
                URL url = new URL("http://45.143.93.102/sessions");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setRequestProperty("Cookie", cookie);

                // Получаем ответ от сервера в виде InputStream
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (Exception e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Intent intent = new Intent(getActivity(), SignIn.class);
            startActivity(intent);
            requireActivity().finish();
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        // Скрываем ProgressBar при отображении фрагмента
        ProgressBar progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
