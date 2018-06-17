package com.example.pangrett.astroweather.data;

import org.json.JSONObject;

public class Channel implements JSONPopulator {

    private Item item;
    private Units units;
    private Location location;
    private Atmosphere atmosphere;
    private Wind wind;

    @Override
    public void populate(JSONObject data) {
        this.units = new Units();
        this.units.populate(data.optJSONObject("units"));

        this.item = new Item();
        this.item.populate(data.optJSONObject("item"));

        this.location = new Location();
        this.location.populate(data.optJSONObject("location"));

        this.atmosphere = new Atmosphere();
        this.atmosphere.populate(data.optJSONObject("atmosphere"));

        this.wind = new Wind();
        this.wind.populate(data.optJSONObject("wind"));
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    public Location getLocation() {
        return location;
    }

    public Wind getWind() {
        return wind;
    }
}
