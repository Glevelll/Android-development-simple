package com.project.scotch.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.scotch.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class SettingsFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    Button button_save;
    ImageButton imageButton_back;
    EditText about, city, name, age;
    RadioButton male, female;
    String cookie, loginS, nameS, nameS2, ageS, ageS2, genderS, genderS2, cityS, cityS2, phone_numberS, aboutS, aboutS2;
    Bitmap bitmap, bitmap2;
    ImageView photo;
    int id, ageServ;
    byte[] imageBytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        imageButton_back = view.findViewById(R.id.imageButton_back);
        button_save = view.findViewById(R.id.button_save);
        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = getArguments();
        if (bundle != null) {
            cookie = bundle.getString("cookie");
            loginS = bundle.getString("login");
            nameS = bundle.getString("name");
            ageS = bundle.getString("age");
            genderS = bundle.getString("gender");
            cityS = bundle.getString("city");
            phone_numberS = bundle.getString("phone_number");
            aboutS = bundle.getString("about");
            imageBytes = bundle.getByteArray("image");
        } else {
            System.out.println("bundle set = null");
        }

        if(imageBytes != null) {
            bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            Toast.makeText(requireActivity().getApplicationContext(),"Фото отсутствует", Toast.LENGTH_SHORT).show();
        }
        //хранение изначальных значений
        nameS2 = nameS;
        cityS2 = cityS;
        ageS2 = ageS;
        aboutS2 = aboutS;
        genderS2 = genderS;
        bitmap2 = bitmap;

        name = view.findViewById(R.id.nameField);
        age = view.findViewById(R.id.ageTextView);
        city = view.findViewById(R.id.cityField);
        about = view.findViewById(R.id.aboutTextView);
        male = view.findViewById(R.id.radioButton);
        female = view.findViewById(R.id.radioButton2);
        photo = view.findViewById(R.id.standard_photo);

        name.setText(nameS);
        age.setText(ageS);
        city.setText(cityS);
        about.setText(aboutS);
        if(Objects.equals(genderS, "male")){
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        if (bitmap != null) {
            photo.setImageBitmap(bitmap);
        }

        photo.setOnClickListener(v -> uploadImage());

        BottomNavigationView navBar = requireActivity().findViewById(R.id.nav_view);
        navBar.setVisibility(View.GONE);

        //проверка на изменение
        imageButton_back.setOnClickListener(v -> {
            boolean modified = false;
            if (name.getText().toString().isEmpty() || !name.getText().toString().equals(nameS2)) {
                Toast.makeText(requireActivity().getApplicationContext(),"Имя изменено. Сохраните изменения", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if (city.getText().toString().isEmpty() || !city.getText().toString().equals(cityS2)) {
                Toast.makeText(requireActivity().getApplicationContext(),"Город изменен. Сохраните изменения", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if (age.getText().toString().isEmpty() || !age.getText().toString().equals(ageS2)) {
                Toast.makeText(requireActivity().getApplicationContext(),"Возраст изменен. Сохраните изменения", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if ((male.isChecked() && Objects.equals(genderS2, "female")) || (female.isChecked() && Objects.equals(genderS2, "male"))){
                Toast.makeText(requireActivity().getApplicationContext(),"Пол изменен. Сохраните изменения", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if ((photo == null) || (bitmap != bitmap2) || (bitmap == null)){
                Toast.makeText(requireActivity().getApplicationContext(),"Фото изменено. Сохраните изменения", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if (!modified) {
                if(male.isChecked()){
                    genderS = "male";
                } else {
                    genderS = "female";
                }
                Bundle bundle1 = new Bundle();
                bundle1.putString("cookie", cookie);
                bundle1.putString("login", loginS);
                bundle1.putString("name", nameS);
                bundle1.putString("age", ageS);
                bundle1.putString("gender", genderS);
                bundle1.putString("city", cityS);
                bundle1.putString("phone_number", phone_numberS);
                bundle1.putString("about", aboutS);
                bundle1.putByteArray("image", imageBytes);
                profileFragment.setArguments(bundle1);
                System.out.println("bundle " + bundle1);

                TransitionManager.beginDelayedTransition(navBar,new Slide());
                navBar.setVisibility(View.VISIBLE);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .add(R.id.container, profileFragment)
                        .commit();
            }
        });

        button_save.setOnClickListener(v -> {
            boolean modified = false;
            if ((name.getText().toString().isEmpty()) || (name.getText().toString().length() > 20)) {
                Toast.makeText(requireActivity().getApplicationContext(),"Имя должно содеражать не более 20 символов", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if ((city.getText().toString().isEmpty()) || (city.getText().toString().length() > 20)) {
                Toast.makeText(requireActivity().getApplicationContext(),"Город должен сожержать не более 20 символов", Toast.LENGTH_SHORT).show();
                modified = true;
            }
            if ((age.getText().toString().isEmpty()) || (Integer.parseInt(age.getText().toString()) < 18) || (Integer.parseInt(age.getText().toString()) > 99)) {
                Toast.makeText(requireActivity().getApplicationContext(), "Возраст должен быть от 18 до 99", Toast.LENGTH_SHORT).show();
                modified = true;
            } if (bitmap == null) {
                Toast.makeText(requireActivity().getApplicationContext(),"Фото пустое", Toast.LENGTH_SHORT).show();
                modified = true;
            } if (!modified) {
                if(male.isChecked()){
                    genderS = "male";
                } else {
                    genderS = "female";
                }

                //Отправка на сервер
                try {
                    JSONObject data = new JSONObject();
                    if (!name.getText().toString().equals(nameS2)) {
                        data.put("name", name.getText().toString());
                        System.out.println("name отправлен");
                    }
                    if (!age.getText().toString().equals(ageS2)) {
                        data.put("age", Integer.parseInt(age.getText().toString()));
                        System.out.println("age отправлен");
                    }
                    if (!genderS.equals(genderS2)) {
                        data.put("gender", genderS);
                        System.out.println("gender отправлен");
                    }
                    if (!city.getText().toString().equals(cityS2)) {
                        data.put("city", city.getText().toString());
                        System.out.println("city отправлен");
                    }
                    if (!about.getText().toString().equals(aboutS2)) {
                        data.put("about", about.getText().toString());
                        System.out.println("about отправлен");
                    }
// создаем объект URL для отправки данных
                    URL url = new URL("http://45.143.93.102/users/current");
// создаем объект HttpURLConnection для отправки данных
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PATCH");
                    conn.setRequestProperty("Cookie", cookie);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
// отправляем данные на сервер
                    OutputStream os = conn.getOutputStream();
                    os.write(data.toString().getBytes());
                    os.flush();
                    os.close();
// получаем обновленные данные с сервера
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        // парсим полученный JSON-объект
                        JSONObject json = new JSONObject(response.toString());
                        id = json.getInt("id");
                        loginS = json.getString("login");
                        nameS = json.getString("name");
                        ageServ = json.getInt("age");
                        genderS = json.getString("gender");
                        cityS = json.getString("city");
                        phone_numberS = json.getString("phone_number");
                        aboutS = json.getString("about");
                        // выводим обновленные данные
                        System.out.println("Name: " + nameS);
                        System.out.println("Age: " + ageServ);
                        System.out.println("Gender: " + genderS);
                        System.out.println("City: " + cityS);
                        System.out.println("About: " + aboutS);
                    } else {
                        System.out.println("Error: " + responseCode);
                    }
                }catch (IOException | JSONException e){
                    Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    System.out.println("Соединение прервано");
                }

                if (!bitmap.equals(bitmap2)) {
                    System.out.println("Отправка: " + bitmap);
                    try {
                        URL url = new URL("http://45.143.93.102/photos/current");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");
                        conn.setRequestProperty("Cookie", cookie);
                        conn.setDoOutput(true);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        byte[] imageBytes = outputStream.toByteArray();

                        OutputStream os = conn.getOutputStream();
                        os.write(imageBytes);
                        os.close();

                        int responseCode = conn.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            System.out.println(responseCode);
                        } else {
                            System.out.println(responseCode);
                        }
                    } catch (IOException e) {
                        Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    System.out.println("bitmap отправлен");
                }


                Bundle bundle1 = new Bundle();
                bundle1.putString("cookie", cookie);
                bundle1.putString("login", loginS);
                bundle1.putString("name", nameS);
                bundle1.putString("age", String.valueOf(ageServ));
                bundle1.putString("gender", genderS);
                bundle1.putString("city", cityS);
                bundle1.putString("phone_number", phone_numberS);
                bundle1.putString("about", aboutS);
                bundle1.putByteArray("image", imageBytes);
                profileFragment.setArguments(bundle1);

                TransitionManager.beginDelayedTransition(navBar, new Slide());
                navBar.setVisibility(View.VISIBLE);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .add(R.id.container, profileFragment)
                        .commit();
            }
            Toast.makeText(requireActivity().getApplicationContext(),"Успешно", Toast.LENGTH_SHORT).show();

        });
        return view;
    }

    private void uploadImage() {
        // Получаем картинку из галереи
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Glide.with(this)
                    .load(uri) // image url
                    .centerCrop()
                    .into(photo);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            photo.setImageBitmap(bitmap);
        }
    }

}