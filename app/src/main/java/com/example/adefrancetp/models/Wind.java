package com.example.adefrancetp.models;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    private float speed;
    @SerializedName("deg")
    private float deg;

    public float getDeg() {
        return deg;
    }

    public float getSpeed() {
        return speed;
    }
}
