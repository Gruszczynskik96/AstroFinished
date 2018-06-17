package com.example.pangrett.astroweather.service;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.pangrett.astroweather.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class YahooWeatherService {

    private WeatherServiceCallback callback;
    private Exception error;
    private String locationSearch, latitudeSearch, longitudeSearch, optionSearch;
    private String YQL;

    public YahooWeatherService(WeatherServiceCallback callback, String location, String latitude, String longitude, String option)
    {
        this.callback = callback;
        this.locationSearch = location;
        this.latitudeSearch = latitude;
        this.longitudeSearch = longitude;
        this.optionSearch = option;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather() {
        /*this.locationSearch = location;
        this.latitudeSearch = latitude;
        this.longitudeSearch = longitude;
        this.optionSearch = option;*/
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                if(optionSearch.equals("1")) {
                    System.out.println(optionSearch);
                    System.out.println(locationSearch);
                    YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", locationSearch);
                }
                else{
                    System.out.println(optionSearch);
                    System.out.println(longitudeSearch);
                    System.out.println(latitudeSearch);
                    String searchLatLong = latitudeSearch + "," + longitudeSearch;
                    System.out.println(searchLatLong);
                    YQL = String.format("select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(%s)\")", searchLatLong);
                }
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();

                } catch (MalformedURLException e) {
                    error = e;
                } catch (IOException e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s == null && error != null) {
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);
                    JSONObject queryResults = data.optJSONObject("query");
                    int count = queryResults.optInt("count");
                    if (count == 0) {
                        callback.serviceFailure(new LocationWeatherException("No weather information found"));
                        return;
                    } else if (queryResults.optJSONObject("results").optJSONObject("channel").optJSONObject("location") == null) {
                        callback.serviceFailure(new LocationWeatherException("Problem with infromation for current localization"));
                        return;
                    } else {
                        Channel channel = new Channel();
                        channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                        callback.serviceSuccess(channel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
