package com.example.pangrett.astroweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangrett.astroweather.data.Atmosphere;
import com.example.pangrett.astroweather.data.Channel;
import com.example.pangrett.astroweather.data.Item;
import com.example.pangrett.astroweather.data.Location;
import com.example.pangrett.astroweather.data.Wind;
import com.example.pangrett.astroweather.service.WeatherServiceCallback;
import com.example.pangrett.astroweather.service.YahooWeatherService;

import java.text.SimpleDateFormat;
import java.lang.Thread;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback{

    private YahooWeatherService service;
    private SharedPreferences sharedPreferencesInfo, sharedPreferencesWeather;
    private String cityToSearch, latitudeToSearch, longitudeToSearch, optionSearch;

    TextView latitudeView, longitudeView;

    ViewPager fViewPager;
    PagerAdapter fPagerAdapter;

    public void showLongAndLati() {
        SharedPreferences sharedPref = getSharedPreferences("weather.xml", 0);
        latitudeView.setText(sharedPref.getString("latitude", String.valueOf(getResources().getString(R.string.latitude))));
        longitudeView.setText(sharedPref.getString("longitude", String.valueOf(getResources().getString(R.string.longitude))));
    }

    private boolean isTablet(Configuration config) {
        boolean xlarge = ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpYahoo();
    }

    public void initViews(){
        latitudeView = findViewById(R.id.viewLatitude);
        longitudeView = findViewById(R.id.viewLongtitude);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        setUpYahoo();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void setUpConfig(){
        Configuration config = getResources().getConfiguration();
        if (isTablet(config)) {
            if (config.orientation == 2) {
                setContentView(R.layout.activity_main_tablet_vertical);
            } else if (config.orientation == 1) {
                setContentView(R.layout.activity_main_tablet_horizontal);
            }

        } else {
            if (config.orientation == 2) {
                setContentView(R.layout.activity_main_phone_vertical);
                fViewPager = findViewById(R.id.viewPager);
                fPagerAdapter = new FragmentsPagesAdapter(getSupportFragmentManager());
                fViewPager.setAdapter(fPagerAdapter);
            } else if (config.orientation == 1) {
                setContentView(R.layout.activity_main_phone_horizontal);
                fViewPager = findViewById(R.id.viewPager);
                fPagerAdapter = new FragmentsPagesAdapter(getSupportFragmentManager());
                fViewPager.setAdapter(fPagerAdapter);
            }
        }
    }

    public void setUpYahoo(){
        sharedPreferencesInfo = getSharedPreferences("infoValues.xml", 0);
        cityToSearch = sharedPreferencesInfo.getString("City", String.valueOf(getResources().getString(R.string.City)));
        latitudeToSearch = sharedPreferencesInfo.getString("Latitude", String.valueOf(getResources().getString(R.string.Latitude)));
        longitudeToSearch = sharedPreferencesInfo.getString("Longitude", String.valueOf(getResources().getString(R.string.Longitude)));
        optionSearch = sharedPreferencesInfo.getString("From_City", String.valueOf(getResources().getString(R.string.From_City)));
        System.out.println(latitudeToSearch + "," + longitudeToSearch);

        service = new YahooWeatherService(this, cityToSearch, latitudeToSearch, longitudeToSearch, optionSearch);
        service.refreshWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.Units:
                startActivity(new Intent(this, UnitsActivity.class));
                return true;

            case R.id.Favourites:
                startActivity(new Intent(this, FavouritesActivity.class));
                return true;

            case R.id.Exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        Item item = channel.getItem();
        Location location = channel.getLocation();
        Atmosphere atmosphere = channel.getAtmosphere();
        Wind wind = channel.getWind();
        sharedPreferencesWeather = getSharedPreferences("weather.xml", 0);
        SharedPreferences.Editor editor = sharedPreferencesWeather.edit();

        editor.putString("city", location.getCity());
        editor.putString("country", location.getCountry());
        editor.putString("wind_direction", wind.getDirection());
        editor.putString("wind_speed", wind.getSpeed());
        editor.putString("humidity", atmosphere.getHumidity());
        editor.putString("pressure", atmosphere.getPressure());
        editor.putString("longitude", item.getLongitude());
        editor.putString("latitude", item.getLatitude());
        editor.putString("visibility", atmosphere.getVisibility());
        editor.putString("current_image_code", item.getCondition().getCode());
        editor.putString("current_temperature", item.getCondition().getTemperature());
        editor.putString("current_description", item.getCondition().getDescription());

        for (int i = 1; i < 4; i++) {
            editor.putString("image_code_" + i, item.getForecast(i).getCodeImage());
            editor.putString("day_" + i, item.getForecast(i).getDay());
            editor.putString("high_temperature_" + i, item.getForecast(i).getHighTemperature());
            editor.putString("low_temperature_" + i, item.getForecast(i).getLowTemperature());
            editor.putString("description_" + i, item.getForecast(i).getDescription());
        }
        editor.apply();

        setUpConfig();
        initViews();
        showLongAndLati();
    }

    @Override
    public void serviceFailure(Exception exception) {
        setUpConfig();
        initViews();
        showLongAndLati();
        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}