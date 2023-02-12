package com.project.sky;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_field);
        Button button = findViewById(R.id.main_but);
        textView = findViewById(R.id.res);
        textView2 = findViewById(R.id.res2);
        textView3 = findViewById(R.id.res3);
        textView4 = findViewById(R.id.res4);

        button.setOnClickListener(view -> {
            if(editText.getText().toString().trim().equals("")){
                Toast.makeText(MainActivity.this, R.string.emptyInput, Toast.LENGTH_LONG).show();
            }
            else {
                String city = editText.getText().toString();
                String key = "8c5998f18ccf9118f1d56e8db5ea65e0";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key;
                new GetURLData().execute(url);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetURLData extends AsyncTask<String, String, String>{
        @SuppressLint("SetTextI18n")
        protected void onPreExecute(){
            super.onPreExecute();
            textView.setText("Loading...");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                return  buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                textView.setText("Temperature: " + (int) (jsonObject.getJSONObject("main").getDouble("temp") - 273));
                textView2.setText("Feels like: " + (int) (jsonObject.getJSONObject("main").getDouble("feels_like") - 273));
                textView3.setText("Wind speed: " + (int) (jsonObject.getJSONObject("wind").getDouble("speed")));
                textView4.setText("Weather: " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}