package com.example.pangrett.astroweather.data;

import org.json.JSONObject;

public class Condition implements JSONPopulator {

    private String code;
    private String temperature;
    private String description;
    private String date;

    @Override
    public void populate(JSONObject data) {
        this.code = data.optString("code");
        this.temperature = data.optString("temp");
        this.description = data.optString("text");
        this.date = data.optString("date");
    }

    public String getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }
}
