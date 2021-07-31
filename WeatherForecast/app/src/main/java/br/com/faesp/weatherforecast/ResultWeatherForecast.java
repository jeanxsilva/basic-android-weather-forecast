package br.com.faesp.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class ResultWeatherForecast extends AppCompatActivity {
    private TextView textDegree;
    private TextView textRain;
    private TextView textClouds;
    private TextView textDescription;
    private TextView textTime;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_weather_forecast);
        ArrayList<WeatherForecast> weatherForecastList = (ArrayList<WeatherForecast>) getIntent().getSerializableExtra("ListWeather");

        textDegree = (TextView) findViewById(R.id.textDegree);
        textRain = (TextView) findViewById(R.id.textRain);
        textClouds = (TextView) findViewById(R.id.textClouds);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textTime = (TextView) findViewById(R.id.textTime);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        if(weatherForecastList.size() != 0){
            if(weatherForecastList.get(0).temperature < 10){
                textDegree.setTextColor(Color.rgb(128, 198, 255));
            }else if(weatherForecastList.get(0).temperature > 10 && weatherForecastList.get(0).temperature < 25){
                textDegree.setTextColor(Color.rgb(255, 164, 128 ));
            }else{
                textDegree.setTextColor(Color.rgb(227, 186, 84));
            }

            textDegree.setText(String.valueOf(weatherForecastList.get(0).temperature) + "º");
            textDescription.setText(weatherForecastList.get(0).description);
            textRain.setText(String.valueOf(weatherForecastList.get(0).precip));
            textClouds.setText(String.valueOf(weatherForecastList.get(0).cloud_coverage) + "%");
            textTime.setText(String.valueOf(weatherForecastList.get(0).time));
        }
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultWeatherForecast.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}