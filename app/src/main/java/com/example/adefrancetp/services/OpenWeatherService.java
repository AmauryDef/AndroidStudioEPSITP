package com.example.adefrancetp.services;

import android.graphics.drawable.Drawable;

import com.example.adefrancetp.models.Global;
import com.example.adefrancetp.models.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @GET("weather/")
    Call<Global> getWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid, @Query("units") String units, @Query("lang") String lang );

    @GET("weather/")
    Call<Global> getWeatherByCity(@Query("q") String q,@Query("appid") String appid, @Query("units") String units, @Query("lang") String lang);
}
