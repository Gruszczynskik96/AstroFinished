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

public class UnitsActivity extends AppCompatActivity {

    private Button setFarenheit, setCelsius, setMiles, setKilometers;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.units_activity);

        sharedPref = getSharedPreferences("weather.xml", 0);

        setFarenheit = findViewById(R.id.btnFarenheit);
        setCelsius = findViewById(R.id.btnCelsius);
        setMiles = findViewById(R.id.btnMph);
        setKilometers = findViewById(R.id.btnKph);

        setFarenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putString("temperature_unit", "0");
                edit.commit();
                Toast.makeText(UnitsActivity.this, "Farenheit Set!", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(UnitsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        setCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putString("temperature_unit", "1");
                edit.commit();
                Toast.makeText(UnitsActivity.this, "Celsius Set!", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(UnitsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        setMiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putString("speed_unit", "0");
                edit.commit();
                Toast.makeText(UnitsActivity.this, "Miles Set!", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(UnitsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        setKilometers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putString("speed_unit", "1");
                edit.commit();
                Toast.makeText(UnitsActivity.this, "Kilometers Set!", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(UnitsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
