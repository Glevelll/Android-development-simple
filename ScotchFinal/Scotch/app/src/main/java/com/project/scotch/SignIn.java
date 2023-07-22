package com.project.scotch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignIn extends AppCompatActivity {

    Button btnOK;
    TextView forgotPass;
    EditText loginET, passwordET;
    String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginET = findViewById(R.id.loginField);
        passwordET = findViewById(R.id.passwordField);

        btnOK = findViewById(R.id.button);
        forgotPass = findViewById(R.id.forgotPass);

        //регистрация
        forgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
        });

        //ОК
        btnOK.setOnClickListener(v -> {
            try {
                if(getPost(loginET.getText().toString().replace(" ", ""), passwordET.getText().toString())) {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    intent.putExtra("cookie", cookie);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignIn.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(SignIn.this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

    }

    //Отправка данных на сервер
    private boolean getPost(String login, String password) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url = "http://45.143.93.102/sessions";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json");

        String urlParameters = String.format("{\"login\" : \"%s\", \"password\" : \"%s\"}", login, password);

        System.out.println(urlParameters);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        if(responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);

            cookie = con.getHeaderField("Set-Cookie");
            System.out.println(cookie);
            
            loginET.setText("");
            passwordET.setText("");
            return true;
        } else {
            return false;
        }
    }
}