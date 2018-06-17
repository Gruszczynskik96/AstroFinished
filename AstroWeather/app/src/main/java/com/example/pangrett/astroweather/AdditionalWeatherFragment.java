package com.example.pangrett.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class AdditionalWeatherFragment extends Fragment {

    private TextView windView;
    private TextView directionView;
    private TextView humidityView;
    private TextView visibilityView;
    private Button refreshAdditional;

    private SharedPreferences sharedPreferences;

    private String speedSettings;
    private String speedUnit;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_additional_weather, container, false);

        initTextViews(rootView);
        refreshAdditional = rootView.findViewById(R.id.refreshAdditonal);
        refreshAdditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences();
                setAdditionalInfo();
            }
        });
        getSharedPreferences();
        setAdditionalInfo();
        return rootView;
    }

    private void initTextViews(View rootView){
        windView = rootView.findViewById(R.id.windView);
        directionView = rootView.findViewById(R.id.directionView);
        humidityView = rootView.findViewById(R.id.humidityView);
        visibilityView = rootView.findViewById(R.id.visibilityView);
    }

    private void getSharedPreferences(){
        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("weather.xml", 0);
        speedSettings = sharedPreferences.getString("speed_unit", "NULL");
    }

    private void setAdditionalInfo(){
        windView.setText(String.format("Wind Speed: %s %s", setUnit(sharedPreferences.getString("wind_speed", "NULL")), speedUnit));
        directionView.setText(String.format("Wind Direction: %s", sharedPreferences.getString("wind_direction", "NULL")));
        humidityView.setText(String.format("Humidity: %s %%", sharedPreferences.getString("humidity", "NULL")));
        visibilityView.setText(String.format("Visibility: %s %%", sharedPreferences.getString("visibility", "NULL")));
    }

    private String setUnit(String windSpeed){
        if(speedSettings.equals("0") || speedSettings.equals("NULL")){
            speedUnit = "mph";
        }
        else{
            speedUnit = "kmh";
            windSpeed = String.valueOf((int)(Double.parseDouble(windSpeed) * 1.61));
        }
        return windSpeed;
    }
}
