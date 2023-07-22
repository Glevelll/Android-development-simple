package com.project.scotch.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.project.scotch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Tab1 extends Fragment {
    private String cookie;
    public static final String str = "Title";
    Full_InfoFragment full_infoFragment = new Full_InfoFragment();
    ArrayList<Integer> listId = new ArrayList<>();
    ArrayList<Integer> listId2 = new ArrayList<>();
    ArrayList<String> listLogin = new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> listAge = new ArrayList<>();
    ArrayList<String> listGender = new ArrayList<>();
    ArrayList<String> listCity = new ArrayList<>();
    ArrayList<String> listPhone = new ArrayList<>();
    ArrayList<String> listAbout = new ArrayList<>();
    byte[] imageBytes;
    Bitmap photo;
    ArrayList<Bitmap> listPhoto = new ArrayList<>();
    ArrayList<Integer> listIds = new ArrayList<>();

    public Tab1() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            cookie = bundle.getString("cook");
        } else {
            System.out.println("bundle set = null");
        }
        System.out.println(cookie);

        GridLayout cardsLayout = view.findViewById(R.id.cards_layout);
        cardsLayout.setColumnCount(2);

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
                    String loginStr = json.getString("login");
                    String nameStr = json.getString("name");
                    int ageInt = json.getInt("age");
                    String genderStr1 = json.getString("gender");
                    String cityStr = json.getString("city");
                    String phone_numberStr = json.getString("phone_number");
                    String aboutStr = json.getString("about");
                    String ageStr = Integer.toString(ageInt);
                    String genderStr = genderStr1;
                    if (genderStr.equals("male")) {
                        genderStr = "Муж";
                    } else {
                        genderStr = "Жен";
                    }
                    System.out.println("Ответ после получения " + id + " " + loginStr + " " + nameStr + " " + ageStr + " " + genderStr + " " + cityStr + " " + phone_numberStr + " " + aboutStr);
                    listId.add(id);
                    listLogin.add(loginStr);
                    listName.add(nameStr);
                    listAge.add(ageStr);
                    listGender.add(genderStr);
                    listCity.add(cityStr);
                    listPhone.add(phone_numberStr);
                    listAbout.add(aboutStr);
                    System.out.println("Листы " + listName + " " + listAge + " " + listGender);
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
                    listId2.add(id);
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

        for (int i = 0; i < listId.size(); i++) {
            String id = String.valueOf(listId.get(i));
            String url = "http://45.143.93.102//photos/" + id;
            try {
                URL photoUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) photoUrl.openConnection();
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
                if (imageBytes != null) {
                    photo = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                } else {
                    Toast.makeText(requireActivity().getApplicationContext(), "Фото не загружено", Toast.LENGTH_SHORT).show();
                }
                // добавляем фото в arrayList
                listPhoto.add(photo);
            } catch (IOException e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                System.out.println("Соединение прервано");
            }
        }

        int row = 0;
        int column = 0;
        for (Integer id : listId) {
            if (listId2.contains(id)) {
                listIds.add(id);
            }
        }
        for (int i = 0; i < listPhoto.size(); i++) {
            View cardView = inflater.inflate(R.layout.user_tab2, container, false);
            ImageButton dell = cardView.findViewById(R.id.del_us);
            ImageView photoUser = cardView.findViewById(R.id.imageView2);
            ImageButton like = cardView.findViewById(R.id.like_tab2);
            CardView ccard2 = cardView.findViewById(R.id.ccard2);
            like.setImageResource(R.drawable.like_red);

            Glide.with(this).load(listPhoto.get(i)).into(photoUser);

            cardsLayout.addView(cardView, new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(column)));
            //именно тут прописываем конопки, почему? хз так работает
            int finalI = i;
            dell.setOnClickListener(v -> {
                dell.setImageResource(R.drawable.no_red);
                like.setImageResource(R.drawable.liketab2);
                ObjectAnimator animator = ObjectAnimator.ofFloat(ccard2, "alpha", 1f, 0f);
                animator.setDuration(500); // Длительность анимации в миллисекундах
                animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Интерполятор для плавности анимации
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        deleteLike(finalI);
                        ccard2.setVisibility(View.GONE); // Удаление элемента после окончания анимации
                        listId.remove(finalI);
                        listLogin.remove(finalI);
                        listName.remove(finalI);
                        listAge.remove(finalI);
                        listGender.remove(finalI);
                        listCity.remove(finalI);
                        listPhone.remove(finalI);
                        listAbout.remove(finalI);
                        listPhoto.remove(finalI);// будут еще listы -> надо делать такое удаление.

                        refreshGridLayout();
                        // думаю тут добавить код, чтобы в сервере удалялся пользователь, которого отклонил юзер.
                    }
                });
                animator.start();
            });

            photoUser.setOnClickListener(v -> {
                Bundle bundle1 = new Bundle();
                bundle1.putString("cookie", cookie);
                bundle1.putParcelable("photo", listPhoto.get(finalI));
                bundle1.putString("name", listName.get(finalI));
                bundle1.putString("age", listAge.get(finalI));
                bundle1.putString("gender", listGender.get(finalI));
                bundle1.putString("city", listCity.get(finalI));
                bundle1.putString("about", listAbout.get(finalI));
                if(listIds.contains(listId.get(finalI))){
                    bundle1.putString("phone", listPhone.get(finalI));
                }
                full_infoFragment.setArguments(bundle1);
                
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down)
                        .add(R.id.container, full_infoFragment)
                        .addToBackStack(null)
                        .commit();
            });
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
        return view;
    }

    private void refreshGridLayout() {
        // Очистить GridLayout
        GridLayout cardsLayout = getView().findViewById(R.id.cards_layout);
        cardsLayout.removeAllViews();

        int row = 0;
        int column = 0;
        for (int i = 0; i < listPhoto.size(); i++) {
            View cardView = LayoutInflater.from(getActivity()).inflate(R.layout.user_tab2, null); // Используйте getActivity() для получения контекста
            ImageButton dell = cardView.findViewById(R.id.del_us);
            ImageView photoUser = cardView.findViewById(R.id.imageView2);
            ImageButton like= cardView.findViewById(R.id.like_tab2);
            CardView ccard2 = cardView.findViewById(R.id.ccard2);
            like.setImageResource(R.drawable.like_red);

            Glide.with(this).load(listPhoto.get(i)).into(photoUser);

            cardsLayout.addView(cardView, new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(column)));
            int finalI = i;

            dell.setOnClickListener(v -> {
                dell.setImageResource(R.drawable.no_red);
                like.setImageResource(R.drawable.liketab2);
                ObjectAnimator animator = ObjectAnimator.ofFloat(ccard2, "alpha", 1f, 0f);
                animator.setDuration(500); // Длительность анимации в миллисекундах
                animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Интерполятор для плавности анимации
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        deleteLike(finalI);
                        ccard2.setVisibility(View.GONE);
                        listId.remove(finalI);
                        listLogin.remove(finalI);
                        listName.remove(finalI);
                        listAge.remove(finalI);
                        listGender.remove(finalI);
                        listCity.remove(finalI);
                        listPhone.remove(finalI);
                        listAbout.remove(finalI);
                        listPhoto.remove(finalI);
                        refreshGridLayout();
                    }
                });
                animator.start();
            });

            photoUser.setOnClickListener(v -> {
                Bundle bundle1 = new Bundle();
                bundle1.putString("cookie", cookie);
                bundle1.putParcelable("photo", listPhoto.get(finalI));
                bundle1.putString("name", listName.get(finalI));
                bundle1.putString("age", listAge.get(finalI));
                bundle1.putString("gender", listGender.get(finalI));
                bundle1.putString("city", listCity.get(finalI));
                bundle1.putString("about", listAbout.get(finalI));
                if(listIds.contains(listId.get(finalI))){
                    bundle1.putString("phone", listPhone.get(finalI));
                }
                full_infoFragment.setArguments(bundle1);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down)
                        .add(R.id.container, full_infoFragment)
                        .addToBackStack(null)
                        .commit();
            });
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }

    public void deleteLike(int finalI){
        try {
            // Создаем объект URL для отправки DELETE-запроса на сервер
            URL url = new URL("http://45.143.93.102/likes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Cookie", cookie);

            // Устанавливаем заголовок Content-Type в значение "application/json"
            con.setRequestProperty("Content-Type", "application/json");

            // Создаем объект JSONObject и добавляем в него значение переменной id
            String jsonParams = "{\"liked_user\":" + listId.get(finalI) + "}";
            byte[] postData = jsonParams.getBytes();

            // Отправляем данные на сервер в виде JSON-строки
            OutputStream os = con.getOutputStream();
            os.write(postData);
            os.flush();
            os.close();

            // Получаем ответ от сервера в виде InputStream
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);
        } catch (Exception e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}