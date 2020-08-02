package com.example.mycommuter.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class GlideBindingAdapter {
    @BindingAdapter("imageresource")
    public static void setImageResource(ImageView imageView, String weatherDescription) {
        if (weatherDescription != null) {
            imageView.setBackgroundResource(IconProvider.getImageIcon(weatherDescription));
        }
    }
}
