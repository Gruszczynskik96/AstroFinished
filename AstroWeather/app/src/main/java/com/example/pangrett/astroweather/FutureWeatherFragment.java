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

import java.util.Objects;

public class FutureWeatherFragment extends Fragment {

    private TextView[] tempHighs = new TextView[4];
    private TextView[] tempLows = new TextView[4];
    private ImageView[] imageViews = new ImageView[4];
    private TextView[] daysViews = new TextView[4];

    private Button refreshForecast;

    private SharedPreferences sharedPreferences;
    private int resource[] = new int[4];

    private String temperatureSettings;
    private String temperatureUnit;
    private Drawable[] weatherIconDrawables = new Drawable[4];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_future_weather, container, false);


        setFutureWeather(rootView);
        refreshForecast = rootView.findViewById(R.id.refreshForecast);
        refreshForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFutureWeather(rootView);
            }
        });


        return rootView;
    }

    public void setFutureWeather(ViewGroup rootView) {
        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("weather.xml", 0);
        temperatureSettings = sharedPreferences.getString("temperature_unit", "NULL");
        for(int i=0; i<3; i++){
            tempHighs[i] = rootView.findViewById(getResources().getIdentifier("tempHigh" + (i+1), "id", getContext().getPackageName()));
            tempLows[i] = rootView.findViewById(getResources().getIdentifier("tempLow" + (i+1), "id", getContext().getPackageName()));
            imageViews[i] = rootView.findViewById(getResources().getIdentifier("dayView" + (i+1), "id", getContext().getPackageName()));
            daysViews[i] = rootView.findViewById(getResources().getIdentifier("day" + (i+1) + "View", "id", getContext().getPackageName()));

            resource[i] = getResources().getIdentifier("icon_" + sharedPreferences.getString("image_code_" + (i + 1), "44"), "drawable", Objects.requireNonNull(getContext()).getPackageName());
            weatherIconDrawables[i] = getResources().getDrawable(resource[i], null);
            tempHighs[i].setText(String.format("%s%s", changeUnit(sharedPreferences.getString("high_temperature_" + (i + 1), "NULL")), temperatureUnit));
            tempLows[i].setText(String.format("%s%s", changeUnit(sharedPreferences.getString("low_temperature_" + (i + 1), "NULL")), temperatureUnit));
            imageViews[i].setImageDrawable(weatherIconDrawables[i]);
            daysViews[i].setText(sharedPreferences.getString("day_" + (i + 1), "NULL"));
        }
    }

    private String changeUnit(String temperatureValue){
        if(temperatureSettings.equals("0") || temperatureSettings.equals("NULL")){
            temperatureUnit = "°F";
        }
        else{
            temperatureUnit = "°C";
            temperatureValue = String.valueOf((int)((Integer.parseInt(temperatureValue)-32)/1.8));
        }
        return temperatureValue;
    }

}
