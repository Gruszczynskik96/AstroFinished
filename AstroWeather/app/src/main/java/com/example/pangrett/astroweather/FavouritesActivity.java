package com.example.pangrett.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pangrett.astroweather.database.Database;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class FavouritesActivity extends AppCompatActivity {

    private EditText cityEditText;
    private Button addFav, delFav;
    private Database database;
    private Cursor data;
    private List<String> cityList;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        initElements();
        displayCityList();

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityEditText.getText().toString();
                if (cityEditText.length() != 0) {
                    if (!cityList.contains(cityName)) {
                        addData(cityName);
                        cityEditText.setText("");
                        displayCityList();
                    } else {
                        Toast.makeText(FavouritesActivity.this, "This city already exists!", LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FavouritesActivity.this, "Empty name!", LENGTH_SHORT).show();
                }
            }
        });

        delFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityEditText.getText().toString();
                if (cityEditText.length() != 0) {
                    if (cityList.contains(cityName)) {
                        deleteData(cityName);
                        cityEditText.setText("");
                        displayCityList();
                    } else {
                        Toast.makeText(FavouritesActivity.this, "This city does not exists", LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FavouritesActivity.this, "Empty name!", LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = listView.getItemAtPosition(position).toString();
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.putString("City", cityName);
                edit.putString("From_City", "1");
                edit.commit();
                Intent myIntent = new Intent(FavouritesActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void initElements() {
        cityEditText = findViewById(R.id.favouriteText);
        listView = findViewById(R.id.citiesView);
        cityList = new ArrayList<>();
        database = new Database(this);
        addFav = findViewById(R.id.btnAddFav);
        delFav = findViewById(R.id.btnDelFav);
        sharedPref = getSharedPreferences("infoValues.xml", 0);
    }

    private void displayCityList() {
        data = database.getListContents();
        cityList.clear();
        while (data.moveToNext()) {
            cityList.add(data.getString(1));
        }
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_view_row, cityList);
        listView.setAdapter(arrayAdapter);
    }

    public void addData(String record) {
        boolean result = database.insertData(record);

        if (result) {
            Toast.makeText(FavouritesActivity.this, "Successful!", LENGTH_SHORT).show();
        } else {
            Toast.makeText(FavouritesActivity.this, "Error!", LENGTH_SHORT).show();
        }
    }

    public void deleteData(String record){
        boolean result = database.deleteData(record);

        if (result) {
            Toast.makeText(FavouritesActivity.this, "Successful!", LENGTH_SHORT).show();
        } else {
            Toast.makeText(FavouritesActivity.this, "Error!", LENGTH_SHORT).show();
        }
    }
}
