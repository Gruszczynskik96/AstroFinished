package com.example.pangrett.astroweather;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class BasicWeatherFragment extends Fragment{

    private ImageView iconView;
    private TextView cityView;
    private TextView countryView;
    private TextView temperatureView;
    private TextView pressureView;
    private TextView descriptionView;

    Button refreshData;

    private SharedPreferences sharedPreferences;

    private String temperatureSettings;
    private String temperatureUnit;
    private String temperatureValue;
    private Drawable weatherIconDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_basic_weather, container, false);

        initiateTextViews(rootView);
        refreshData = rootView.findViewById(R.id.refreshData);
        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences();
                setWeatherBasicInfo();
            }
        });
        getSharedPreferences();
        setWeatherBasicInfo();
        return rootView;
    }

    private void getSharedPreferences() {
        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("weather.xml", 0);
        temperatureValue = sharedPreferences.getString("current_temperature", "NULL");
        int resource = getResources().getIdentifier("icon_" + sharedPreferences.getString("current_image_code", "44"), "drawable", Objects.requireNonNull(getContext()).getPackageName());
        weatherIconDrawable = getResources().getDrawable(resource, null);
        temperatureSettings = sharedPreferences.getString("temperature_unit", "NULL");
    }

    private void setWeatherBasicInfo() {
        changeUnit();
        cityView.setText(String.format(sharedPreferences.getString("city", "NULL")));
        countryView.setText(sharedPreferences.getString("country", "NULL"));
        iconView.setImageDrawable(weatherIconDrawable);
        temperatureView.setText(String.format("%s%s", temperatureValue, temperatureUnit));
        descriptionView.setText(sharedPreferences.getString("current_description", "NULL"));
        pressureView.setText(String.format("%shPa", sharedPreferences.getString("pressure", "NULL")));
    }

    public void initiateTextViews(View rootView){
        iconView = rootView.findViewById(R.id.iconView);
        cityView = rootView.findViewById(R.id.cityView);
        countryView = rootView.findViewById(R.id.countryView);
        temperatureView = rootView.findViewById(R.id.temperatureView);
        pressureView = rootView.findViewById(R.id.pressureView);
        descriptionView = rootView.findViewById(R.id.descriptionView);
    }

    private void changeUnit(){
        if(temperatureSettings.equals("0") || temperatureSettings.equals("NULL")){
            temperatureUnit = "°F";
        }
        else{
            temperatureUnit = "°C";
            temperatureValue = String.valueOf((int)((Integer.parseInt(temperatureValue)-32)/1.8));
        }
    }

    @Override
    public void onStart() {
        getSharedPreferences();
        setWeatherBasicInfo();
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        getSharedPreferences();
        setWeatherBasicInfo();
    }
}
