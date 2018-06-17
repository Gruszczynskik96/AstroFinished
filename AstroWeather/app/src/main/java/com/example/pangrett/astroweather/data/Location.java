package com.example.pangrett.astroweather.data;

import org.json.JSONObject;

public class Location implements JSONPopulator {

    private String city;
    private String country;

    @Override
    public void populate(JSONObject data) {
        this.city = data.optString("city");
        this.country = data.optString("country");
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
