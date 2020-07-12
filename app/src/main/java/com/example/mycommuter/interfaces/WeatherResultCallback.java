package com.example.mycommuter.interfaces;


import com.example.mycommuter.model.Weather;

import java.util.List;

public interface WeatherResultCallback {
    void getWeather(List<Weather> weather);
}
