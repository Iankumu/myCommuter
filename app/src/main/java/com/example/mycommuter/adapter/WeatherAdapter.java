package com.example.mycommuter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommuter.R;

import com.example.mycommuter.model.Weather;
import com.example.mycommuter.utils.IconProvider;

import com.squareup.picasso.Picasso;


import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {
    private List<Weather> weather;
    Context context;


    public WeatherAdapter(Context context, List<Weather> weather) {
        this.context = context;
        this.weather = weather;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView date;
        public TextView description;
        public TextView temp, feelslike;
        public ImageView weatherimg;
        private CardView cardView;


        public MyViewHolder(View v) {
            super(v);
            this.cardView = v.findViewById(R.id.cardViewWeatherCard);
            this.date = (TextView) v.findViewById(R.id.textViewCardDate);
            this.description = (TextView) v.findViewById(R.id.textViewCardWeatherDescription);
            this.temp = (TextView) v.findViewById(R.id.textViewCardCurrentTemp);
            this.feelslike = v.findViewById(R.id.textViewCardfeelslike);
            this.weatherimg=v.findViewById(R.id.imageViewCardWeatherIcon);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecastlist, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.MyViewHolder holder, int position) {

        final Weather current_weather = weather.get(position);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.date.setText(current_weather.getDate());
        holder.description.setText(current_weather.getDescription());
        holder.temp.setText(current_weather.getTemp());
        holder.feelslike.setText(current_weather.getFeels_like());
        String cweather=current_weather.getMain();
        holder.weatherimg.setBackgroundResource(IconProvider.getImageIcon(cweather));

//        Picasso.get().load(IconProvider.getImageIcon(cweather)).into(holder.weatherimg);
    }

    @Override
    public int getItemCount() {

        return weather.size();

    }

}
