package com.example.pangrett.astroweather.data;

import org.json.JSONObject;

public class Forecast implements JSONPopulator {

    private String codeImage;
    private String day;
    private String lowTemperature;
    private String highTemperature;
    private String description;


    @Override
    public void populate(JSONObject data) {
        this.codeImage = data.optString("code");
        this.day = data.optString("day");
        this.lowTemperature = data.optString("low");
        this.highTemperature = data.optString("high");
        this.description = data.optString("text");
    }

    public String getCodeImage() {
        return codeImage;
    }

    public String getDay() {
        return day;
    }

    public String getLowTemperature() {
        return lowTemperature;
    }

    public String getHighTemperature() {
        return highTemperature;
    }

    public String getDescription() {
        return description;
    }
}
