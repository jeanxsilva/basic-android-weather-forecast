package br.com.faesp.weatherforecast;

import android.os.Build;
import android.text.format.Time;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class WeatherForecast implements Serializable {
    public String time;
    public String description;
    public double temperature;
    public double precip;
    public double cloud_coverage;

    public WeatherForecast(double temperature, double precip, double cloud_coverage, String description) {
        Date timeNow = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timeNow);
        this.time = date;
        this.temperature = temperature;
        this.precip = precip;
        this.cloud_coverage = cloud_coverage;

        this.description = description;
    }
}