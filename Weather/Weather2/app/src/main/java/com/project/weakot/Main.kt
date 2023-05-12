package com.project.weakot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.net.URL

class Main : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView

    private val API_KEY = "8c5998f18ccf9118f1d56e8db5ea65e0"
    private val API_URL = "https://api.openweathermap.org/data/2.5/weather?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_field)
        button = findViewById(R.id.main_but)
        textView1 = findViewById(R.id.res)
        textView2 = findViewById(R.id.res2)
        textView3 = findViewById(R.id.res3)
        textView4 = findViewById(R.id.res4)

        button.setOnClickListener {
            val city = editText.text.toString().trim()
            if(city.isEmpty()){
                Toast.makeText(this, R.string.emptyInput, Toast.LENGTH_LONG).show()
            }
            else {
                val url = "$API_URL$city&appid=$API_KEY"
                Thread {
                    try {
                        val apiResponse = URL(url).readText()
                        val jsonObject = JSONObject(apiResponse)
                        val temperature = jsonObject.getJSONObject("main").getDouble("temp") - 273
                        val feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like") - 273
                        val windSpeed = jsonObject.getJSONObject("wind").getDouble("speed")
                        val weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
                        runOnUiThread {
                            textView1.text = "Temperature: ${temperature.toInt()} °C"
                            textView2.text = "Feels like: ${feelsLike.toInt()} °C"
                            textView3.text = "Speed of wind: ${windSpeed.toInt()}"
                            textView4.text = "Weather: $weather"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }
    }
}