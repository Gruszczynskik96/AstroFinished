package com.example.pangrett.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private final static String VALUE_REGEX = "^-?\\d*\\.\\d+$|^-?\\d+$";
    private final static String TIME_REGEX = "\\d+";
    TextView editLatitude, editLongitude, editRefresh, editCity;
    Button saveAll, saveCity;
    SharedPreferences sharedPrefInfo, sharedPrefWeather;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("savedCity", String.valueOf(editCity.getText()));
        outState.putString("savedLatitude", String.valueOf(editLatitude.getText()));
        outState.putString("savedLongitude", String.valueOf(editLongitude.getText()));
        outState.putString("savedRefresh", String.valueOf(editRefresh.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        editCity = findViewById(R.id.editCity);
        editLatitude = findViewById(R.id.editLatitude);
        editLongitude = findViewById(R.id.editLongtitude);
        editRefresh = findViewById(R.id.editClockRefresh);

        saveAll = findViewById(R.id.btnSave);
        saveCity = findViewById(R.id.buttonSaveCity);

        if (savedInstanceState != null) {
            editCity.setText(savedInstanceState.getString("savedCity"));
            editLatitude.setText(savedInstanceState.getString("savedLatitude"));
            editLongitude.setText(savedInstanceState.getString("savedLongitude"));
            editRefresh.setText(savedInstanceState.getString("savedRefresh"));
        }

        sharedPrefInfo = getSharedPreferences("infoValues.xml", 0);
        sharedPrefWeather = getSharedPreferences("weather.xml", 0);
        editCity.setText(sharedPrefInfo.getString("City", String.valueOf(getResources().getString(R.string.City))));
        editLatitude.setText(sharedPrefInfo.getString("Latitude", String.valueOf(getResources().getString(R.string.Latitude))));
        editLongitude.setText(sharedPrefInfo.getString("Longitude", String.valueOf(getResources().getString(R.string.Longitude))));
        editRefresh.setText(sharedPrefInfo.getString("Refresh", String.valueOf(getResources().getString(R.string.Refresh))));

        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkValue1, checkValue2, checkValue3;

                checkValue1 = String.valueOf(editLatitude.getText());
                checkValue2 = String.valueOf(editLongitude.getText());
                checkValue3 = String.valueOf(editRefresh.getText());

                checkValues(checkValue1, checkValue2, checkValue3);

                SharedPreferences.Editor edit = sharedPrefWeather.edit();
                SharedPreferences.Editor edit2 = sharedPrefInfo.edit();
                edit.putString("fromcity", "0");
                edit2.putString("From_City", "0");
                edit.commit();
                edit2.commit();

                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        saveCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkValue;

                checkValue = String.valueOf(editCity.getText());
                saveCity(checkValue);

                SharedPreferences.Editor edit = sharedPrefWeather.edit();
                SharedPreferences.Editor edit2 = sharedPrefInfo.edit();
                edit.putString("fromcity", "1");
                edit2.putString("From_City", "1");
                edit.commit();
                edit2.commit();

                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

    }

    public void checkValues(String latitude, String longitude, String refresh){
        if(!latitude.equals("") && !longitude.equals("0") && !refresh.equals("")){
            if(latitude.matches(VALUE_REGEX) && longitude.matches(VALUE_REGEX) && refresh.matches(TIME_REGEX)){
                if((Double.valueOf(latitude) >= -90 && Double.valueOf(latitude) <= 90) && (Double.valueOf(longitude) >= -180 && Double.valueOf(longitude) <= 180)
                        && (Integer.valueOf(refresh) > 0 && Integer.valueOf(refresh) <= 60)){
                        SharedPreferences.Editor edit = sharedPrefWeather.edit();
                        SharedPreferences.Editor edit2 = sharedPrefInfo.edit();
                        edit.putString("latitude", latitude);
                        edit.putString("longitude", longitude);
                        edit2.putString("Latitude", latitude);
                        edit2.putString("Longitude", longitude);
                        edit2.putString("Refresh", refresh);
                        edit.commit();
                        edit2.commit();
                }
                else{
                        Toast.makeText(SettingsActivity.this, "Data are out of range!", Toast.LENGTH_LONG).show(); }
            }
            else{
                Toast.makeText(SettingsActivity.this, "Those data aren't good!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(SettingsActivity.this, "Data empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void saveCity(String checkValue){
        SharedPreferences.Editor edit = sharedPrefInfo.edit();
        SharedPreferences.Editor edit2 = sharedPrefWeather.edit();
        edit.putString("City", checkValue);
        edit2.putString("city", checkValue);
        edit.commit();
        edit2.commit();
    }
}
