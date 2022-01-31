package com.example.weatherinfo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class modal {
    String time,temper,wind;
    String image;
    int back;
    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public modal(String time, String temper, String wind, String image,int back) {
        this.time = time;
        this.temper = temper;
        this.wind = wind;
        this.image = image;
        this.back = back;

    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemper() {
        return temper;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
