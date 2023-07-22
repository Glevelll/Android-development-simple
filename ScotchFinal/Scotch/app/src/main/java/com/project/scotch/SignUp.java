package com.project.scotch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button btnOK, btnCancel;
    EditText login, password, name, city, phone_number, about;
    String gender, age;
    ImageView standard_photo;
    private Bitmap bitmap;

    int startAge = 18;
    int endAge = 99;
    int arrayLength = endAge - startAge + 1;
    String[] ages = new String[arrayLength];

    String[] genders = {"Муж", "Жен"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        for (int i = 0; i < arrayLength; i++) {
            ages[i] = String.valueOf(startAge + i);
        }

        btnOK = findViewById(R.id.OK);
        btnCancel = findViewById(R.id.button);
        login = findViewById(R.id.loginField);
        password = findViewById(R.id.passwordField);
        name = findViewById(R.id.nameField);
        city = findViewById(R.id.cityField);
        phone_number = findViewById(R.id.phoneField);
        about = findViewById(R.id.aboutField);
        standard_photo = findViewById(R.id.standart_photo);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ages);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.age);
        spinner.setAdapter(adapter1);
        spinner.setPrompt("Age");
        spinner.setSelection(0);
        age = spinner.getSelectedItem().toString();

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner2 = findViewById(R.id.gender);
        spinner2.setAdapter(adapter2);
        spinner2.setPrompt("Gender");
        spinner2.setSelection(0);
        if (spinner2.getSelectedItemPosition() == 0) {
            gender = "male";
        }
        if (spinner2.getSelectedItemPosition() == 1) {
            gender = "female";
        }

        //ОК
        btnOK.setOnClickListener(v -> {
            if ((login.getText().toString().length() < 3) || (login.getText().toString().length() > 25)) {
                Toast.makeText(SignUp.this, "Логин должен быть от 3 до 25 символов", Toast.LENGTH_SHORT).show();
            }
            if ((password.getText().toString().length() < 6) || (password.getText().toString().length() > 100)) {
                Toast.makeText(SignUp.this, "Пароль должен быть от 6 до 100 символов", Toast.LENGTH_SHORT).show();
            }
            if ((name.getText().toString().isEmpty()) || (name.getText().toString().length() > 20)) {
                Toast.makeText(SignUp.this, "Имя должно быть не более 20 символов", Toast.LENGTH_SHORT).show();
            }
            if ((city.getText().toString().isEmpty()) || (city.getText().toString().length() > 20)) {
                Toast.makeText(SignUp.this, "Город должен быть не более 20 символов", Toast.LENGTH_SHORT).show();
            }
            if (((phone_number.getText().toString().charAt(0) == '+') && !(phone_number.getText().toString().length() == 12))
                    || ((phone_number.getText().toString().charAt(0) == '8') && !(phone_number.getText().toString().length() == 11))) {
                Toast.makeText(SignUp.this, "Некорректный номер телефона", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUp.this, "Успешно", Toast.LENGTH_SHORT).show();
                try {
                    System.out.println("\nTesting - Send Http POST request");
                    // Отправляем данные на сервер
                    UploadTask task = new UploadTask(login.getText().toString(), password.getText().toString(), name.getText().toString(),
                            Integer.parseInt(age), gender, city.getText().toString(), phone_number.getText().toString(), about
                            .getText().toString(), bitmap);
                    task.execute();
                } catch (Exception e) {
                    Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                login.setText("");
                password.setText("");
                name.setText("");
                spinner.setSelection(0);
                spinner2.setSelection(0);
                city.setText("");
                phone_number.setText("+7");
                about.setText("");
            }
        });

        //Закрыть
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
        });

        //Вставка картинки
        standard_photo.setOnClickListener(view -> uploadImage());
    }

    //Отправка данных на сервер
    private class UploadTask extends AsyncTask<Void, Void, String> {

        private final String login;
        private final String password;
        private final String name;
        private final int age;
        private final String gender;
        private final String city;
        private final String phone_number;
        private final String about;
        private final Bitmap bitmap;

        public UploadTask(String login, String password, String name, int age, String gender, String city,
                          String phone_number, String about, Bitmap bitmap) {
            this.login = login;
            this.password = password;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.city = city;
            this.phone_number = phone_number;
            this.about = about;
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            try {
                // Создаем HTTP клиент
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                // Создаем JSON объект
                JSONObject json = new JSONObject();
                json.put("login", login);
                json.put("password", password);
                json.put("name", name);
                json.put("age", age);
                json.put("gender", gender);
                json.put("city", city);
                json.put("phone_number", phone_number);
                json.put("about", about);

                // Создаем multipart запрос
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("json", json.toString(), RequestBody.create(MediaType.parse("application/json"), json.toString()))
                        .addFormDataPart("image", "image.jpg",
                                RequestBody.create(MediaType.parse("image/jpeg"), getFileBytes(bitmap)))
                        .build();

                // Создаем запрос
                Request request = new Request.Builder()
                        .url("http://45.143.93.102/users")
                        .post(requestBody)
                        .build();

                // Отправляем запрос
                Response response = client.newCall(request).execute();

                // Получаем ответ
                result = Objects.requireNonNull(response.body()).string();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error: " + e.getMessage();
                Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Ответ: " + result);
        }
    }

    private void uploadImage() {
        // Получаем картинку из галереи
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Glide.with(this)
                    .load(uri) // image url
                    .centerCrop()
                    .into(standard_photo);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            standard_photo.setImageBitmap(bitmap);
        }
    }

    private byte[] getFileBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }
}