package com.example.adefrancetp.models;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("country")
    private String country;
    @SerializedName("sunrise")
    private int sunrise;
    @SerializedName("sunset")
    private int sunset;

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public String getCountry() {
        return country;
    }
}
