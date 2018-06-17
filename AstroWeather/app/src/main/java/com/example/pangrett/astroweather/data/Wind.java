package com.example.pangrett.astroweather.data;

import org.json.JSONObject;

public class Wind implements JSONPopulator {
    private String direction;
    private String speed;

    @Override
    public void populate(JSONObject data) {
        this.direction = data.optString("direction");
        this.speed = data.optString("speed");
    }

    public String getDirection() {
        return direction;
    }

    public String getSpeed() {
        return speed;
    }
}
