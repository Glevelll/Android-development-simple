package com.project.scotch.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class Tab2 extends Fragment {
    public static final String str = "Title";
    Full_InfoFragment full_infoFragment = new Full_InfoFragment();
    ArrayList<Integer> listId = new ArrayList<>();
    ArrayList<Integer> listId2 = new ArrayList<>();
    ArrayList<Integer> listIds = new ArrayList<>();
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
    private String cookie;

    public Tab2() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            cookie = bundle.getString("cook");
        } else {
            System.out.println("bundle set = null");
        }
        System.out.println(cookie);

        GridLayout cardsLayout = view.findViewById(R.id.cards_layout);
        cardsLayout.setColumnCount(2);

        LinearLayout linearLayout = view.findViewById(R.id.scroll);


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

        // Создаем и добавляем на экран карточки
        int row = 0;
        int column = 0;
        for (Integer id : listId) {
            if (listId2.contains(id)) {
                listIds.add(id);
            }
        }
        for (int i = 0; i < listPhoto.size(); i++) {
            View cardView = inflater.inflate(R.layout.user_tab2, container,false);
            ImageButton dell = cardView.findViewById(R.id.del_us);
            ImageView photoUser = cardView.findViewById(R.id.imageView2);
            ImageButton like= cardView.findViewById(R.id.like_tab2);
            if (listIds.contains(listId.get(i))){
                like.setImageResource(R.drawable.like_red);
            } else {
                like.setImageResource(R.drawable.liketab2);
            }

            Glide.with(this).load(listPhoto.get(i)).into(photoUser);

            cardsLayout.addView(cardView, new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(column)));
            int finalI = i;
            dell.setOnClickListener(v -> {
                dell.setImageResource(R.drawable.no_red);
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
                like.setImageResource(R.drawable.liketab2);
                dell.setImageResource(R.drawable.no_black);
            });

            like.setOnClickListener(v -> {
                like.setImageResource(R.drawable.like_red);
                try {
                    URL obj = new URL("http://45.143.93.102/likes");
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // добавляем cookie в заголовок запроса
                    con.setRequestProperty("Cookie", cookie);

                    // устанавливаем метод запроса и параметры
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    // создаем JSON объект и добавляем в него параметр id
                    String jsonParams = "{\"liked_user\":" + listId.get(finalI) + "}";
                    byte[] postData = jsonParams.getBytes();

                    // записываем параметры в выходной поток
                    OutputStream os = con.getOutputStream();
                    os.write(postData);
                    os.flush();
                    os.close();

                    // отправляем запрос и получаем ответ
                    int responseCode = con.getResponseCode();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // выводим результат
                    System.out.println("Response code: " + responseCode);
                    System.out.println("Response body: " + response);
                } catch (IOException e) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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

        /////////////// новое добавление карт сверху 2 таба
        for (int i = 0; i < listId.size(); i++) {
            if(listIds.contains(listId.get(i))) {
                View cardView = inflater.inflate(R.layout.mutual_like_card, container, false);
                ImageView photoUser = cardView.findViewById(R.id.imageView2);
                linearLayout.addView(cardView);
                Glide.with(this).load(listPhoto.get(i)).into(photoUser);
                int finalI = i;

                photoUser.setOnClickListener(v -> {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("cookie", cookie);
                    bundle1.putParcelable("photo", listPhoto.get(finalI));
                    bundle1.putString("name", listName.get(finalI));
                    bundle1.putString("age", listAge.get(finalI));
                    bundle1.putString("gender", listGender.get(finalI));
                    bundle1.putString("city", listCity.get(finalI));
                    bundle1.putString("about", listAbout.get(finalI));
                    bundle1.putString("phone", listPhone.get(finalI));
                    full_infoFragment.setArguments(bundle1);

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down)
                            .add(R.id.container, full_infoFragment)
                            .addToBackStack(null)
                            .commit();
                });
            }
        }








        return view;
    }

}