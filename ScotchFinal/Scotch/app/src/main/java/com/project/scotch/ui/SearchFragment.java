package com.project.scotch.ui;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
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
import java.util.List;

public class SearchFragment extends Fragment {
    Button clear, apply;
    EditText minage, maxage, city;
    TextView textView4;
    RadioButton radioButton1, radioButton2;
    ImageButton imageButton_like, imageButton_dis, imageButton_info, button;
    Full_InfoFragment full_infoFragment = new Full_InfoFragment();

    private arrayAdapter arrayAdapter;
    List<Cards> rowItems;
    Cards item;
    int counter = 0;
    int countUser = 0;
    int countF = 0;
    private String cookie, genderF;
    byte[] imageBytes;
    Bitmap photo;
    ArrayList<Integer> listId = new ArrayList<>();
    ArrayList<String> listLogin = new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> listAge = new ArrayList<>();
    ArrayList<String> listGender = new ArrayList<>();
    ArrayList<String> listCity = new ArrayList<>();
    ArrayList<String> listPhone = new ArrayList<>();
    ArrayList<String> listAbout = new ArrayList<>();
    ArrayList<Bitmap> listPhoto = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            cookie = bundle.getString("cookie");
        } else {
            System.out.println("bundle set = null");
        }

        try {
            URL url = new URL("http://45.143.93.102/users/count");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            countUser = jsonResponse.getInt("user_count");
            System.out.println("Количество пользователей на сервере: " + countUser);
        } catch (Exception e) {
            Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        textView4 = view.findViewById(R.id.textView4);
        counter = 0;
        rowItems = new ArrayList<>();
        imageButton_like = view.findViewById(R.id.imageButton_like);
        imageButton_dis = view.findViewById(R.id.imageButton_dis);
        imageButton_info = view.findViewById(R.id.imageButton_info);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> showDialog(view));

        cleaning();

        try {
            URL url = new URL("http://45.143.93.102/users/all");
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

        getPhoto();
        updateCard(countUser - 1, view);

        return view;
    }

    public void onResume() {
        super.onResume();
        // Скрываем ProgressBar при отображении фрагмента
        ProgressBar progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showDialog(View view) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        clear = dialog.findViewById(R.id.clear);
        apply = dialog.findViewById(R.id.apply);
        minage = dialog.findViewById(R.id.minage);
        maxage = dialog.findViewById(R.id.maxage);
        city = dialog.findViewById(R.id.city);

        radioButton1 = dialog.findViewById(R.id.radioButton1);
        radioButton2 = dialog.findViewById(R.id.radioButton2);

        clear.setOnClickListener(v -> {
            minage.setText("18");
            maxage.setText("99");
            city.setText("");
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            genderF = null;
        });

        apply.setOnClickListener(v -> {
            countF = 0;
            if ((Integer.parseInt(minage.getText().toString()) < 18) || (Integer.parseInt(maxage.getText().toString()) > 99)) {
                minage.setText("18");
                maxage.setText("99");
                Toast.makeText(requireActivity().getApplicationContext(), "Возраст должен быть от 18 до 99", Toast.LENGTH_SHORT).show();
            }
            if (Integer.parseInt(minage.getText().toString()) > Integer.parseInt(maxage.getText().toString())) {
                minage.setText("18");
                maxage.setText("99");
                Toast.makeText(requireActivity().getApplicationContext(), "Максимальный возраст не может быть меньше минимального", Toast.LENGTH_SHORT).show();
            } else {
                if (minage.getText().toString().equals("")) {
                    minage.setText("18");
                }
                if (maxage.getText().toString().equals("")) {
                    maxage.setText("99");
                }

                if (radioButton1.isChecked()) {
                    genderF = "male";
                } else if (radioButton2.isChecked()) {
                    genderF = "female";
                }

                cleaning();

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("min_age", Integer.parseInt(minage.getText().toString()));
                    jsonObject.put("max_age", Integer.parseInt(maxage.getText().toString()));
                    jsonObject.put("gender", genderF);
                    jsonObject.put("city", city.getText().toString());

                    // создание объекта URL и отправка GET-запроса
                    URL url = new URL("http://45.143.93.102/users/filter?=" + jsonObject);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Cookie", cookie);

                    System.out.println(url);
                    // обработка ответа
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
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
                        Log.d(TAG, userInfo);
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
                            Log.d(TAG, "Ответ после получения " + id + " " + loginStr + " " + nameStr + " " + ageStr + " " + genderStr + " " + cityStr + " " + phone_numberStr + " " + aboutStr);
                            listId.add(id);
                            listLogin.add(loginStr);
                            listName.add(nameStr);
                            listAge.add(ageStr);
                            listGender.add(genderStr);
                            listCity.add(cityStr);
                            listPhone.add(phone_numberStr);
                            listAbout.add(aboutStr);
                            countF++;
                            System.out.println("Листы " + listName + " " + listAge + " " + listGender + " " + countF);
                        }
                        Toast.makeText(requireActivity().getApplicationContext(), "Применено", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Response code: " + responseCode);
                    }
                    connection.disconnect();
                } catch (IOException | JSONException e) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e(TAG, "Error occurred while getting user info");
                }

                getPhoto();

                rowItems.clear();
                arrayAdapter.clear();

                updateCard(countF, view);
                genderF = null;
            }
            Toast.makeText(requireActivity().getApplicationContext(), "Применено", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        //Обработчик жестов
        final GestureDetector gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //Если пользователь провел палец вниз, то скрываем диалог
                if (e1.getY() < e2.getY()) {
                    dialog.dismiss();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        dialog.getWindow().getDecorView().setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return true;
        });
    }

    public void getPhoto(){
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
    }

    public void updateCard(int count, View view){
        counter = 0;
        for (int j = 0; j < count; j++) {
            item = new Cards("@+id/name_profile", "@+id/age_profile", "@+id/gender_profile", listPhoto.get(j));
            item.setName(listName.get(j));
            item.setAge(listAge.get(j));
            item.setGender(listGender.get(j));
            rowItems.add(item);
        }

        arrayAdapter = new arrayAdapter(getActivity(), R.layout.card, rowItems);
        SwipeFlingAdapterView flingContainer = view.findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                counter = counter + 1;
                if (counter == count) {
                    textView4.setVisibility(View.VISIBLE);
                    imageButton_like.setVisibility(View.GONE);
                    imageButton_dis.setVisibility(View.GONE);
                    imageButton_info.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                pressLike();
                counter = counter + 1;
                if (counter == count) {
                    textView4.setVisibility(View.VISIBLE);
                    imageButton_like.setVisibility(View.GONE);
                    imageButton_dis.setVisibility(View.GONE);
                    imageButton_info.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {
                    // Появление кнопок
                    textView4.setVisibility(View.VISIBLE);
                    imageButton_like.setVisibility(View.VISIBLE);
                    imageButton_dis.setVisibility(View.VISIBLE);
                    imageButton_info.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        imageButton_like.setOnClickListener(v -> {
            pressLike();
            flingContainer.getTopCardListener().selectRight();
        });

        imageButton_dis.setOnClickListener(v -> flingContainer.getTopCardListener().selectLeft());

        imageButton_info.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("cookie", cookie);
            bundle1.putParcelable("photo", listPhoto.get(counter));
            bundle1.putString("name", listName.get(counter));
            bundle1.putString("age", listAge.get(counter));
            bundle1.putString("gender", listGender.get(counter));
            bundle1.putString("city", listCity.get(counter));
            bundle1.putString("about", listAbout.get(counter));
            full_infoFragment.setArguments(bundle1);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down)
                    .add(R.id.container, full_infoFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    public void pressLike(){
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
            String jsonParams = "{\"liked_user\":" + listId.get(counter) + "}";
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
    }

    public void cleaning(){
        listId.clear();
        listLogin.clear();
        listName.clear();
        listAge.clear();
        listGender.clear();
        listCity.clear();
        listPhone.clear();
        listAbout.clear();
        listPhoto.clear();
        item = null;
    }
}