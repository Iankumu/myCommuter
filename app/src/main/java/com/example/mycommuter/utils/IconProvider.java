package com.example.mycommuter.utils;

import com.example.mycommuter.R;

public class IconProvider {
 public static int getImageIcon(String weatherDescription){
            int weatherIcon ;
            switch(weatherDescription) {
                case "Thunderstorm":
                    weatherIcon = R.drawable.ic_thunder;

                    break;
                case "Drizzle":
                    weatherIcon = R.drawable.ic_drizzle_1;
                    break;
                case "Rain":
                    weatherIcon = R.drawable.ic_rainy_7;
                    break;
                case "Snow":
                    weatherIcon = R.drawable.ic_snowy_6;
                    break;
                case "Atmosphere":
                    weatherIcon = R.drawable.ic_night;
                    break;
                case "Clear":
                    weatherIcon = R.drawable.ic_sunny;
                    break;
                case "Clouds":
                    weatherIcon = R.drawable.ic_cloudy;
                    break;
                case "Extreme":
                    weatherIcon = R.drawable.ic_cloudy_night_3;
                    break;
                default:
                    weatherIcon = R.mipmap.ic_launcher;
            }
            return weatherIcon;

        }

    }

