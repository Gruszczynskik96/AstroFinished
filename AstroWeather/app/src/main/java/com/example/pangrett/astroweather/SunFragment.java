package com.example.pangrett.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class SunFragment extends Fragment {

    TextView showView;
    String date;
    int refreshTime=1;
    double latitudeValue=0;
    double longitudeValue=0;

    int year;
    int month;
    int day;
    int hours;
    int minutes;
    int seconds;

    Calendar calendarValues;

    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    AstroCalculator astroCalculator;
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;

    Thread t2;

    public void showText() {
        showView.setText(
                "Sunrise:\n" + astroCalculator.getSunInfo().getSunrise() +
                        "\nSunset:\n" + astroCalculator.getSunInfo().getSunset() +
                        "\nAzimuth rise:\n" + astroCalculator.getSunInfo().getAzimuthRise() +
                        "\nAzimuth set:\n" + +astroCalculator.getSunInfo().getAzimuthSet() +
                        "\nTwilight morning:\n" + astroCalculator.getSunInfo().getTwilightMorning() +
                        "\nTwilight evening:\n" + astroCalculator.getSunInfo().getTwilightEvening()
        );
    }

    public void getRefreshTime(){
        if(getActivity() != null){
            SharedPreferences sharedPref = getActivity().getSharedPreferences("infoValues.xml", 0);
            refreshTime = Integer.valueOf(sharedPref.getString("Refresh", String.valueOf(getActivity().getString(R.string.Refresh))));
        }
    }

    private int getTimeZone(){
        Calendar c = Calendar.getInstance();

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();

        if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        return offset / 1000 / 60 / 60;
    }

    public void astroInit() {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("weather.xml", 0);

            date = df.format(Calendar.getInstance().getTime());

            astroDateTime = new AstroDateTime(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    Calendar.getInstance().get(Calendar.SECOND),
                    getTimeZone(),
                    false
            );

            location = new AstroCalculator.Location(
                    Double.valueOf(sharedPref.getString("latitude", String.valueOf(getResources().getString(R.string.latitude)))),
                    Double.valueOf(sharedPref.getString("longitude", String.valueOf(getResources().getString(R.string.longitude))))
            );

            astroCalculator = new AstroCalculator(
                    astroDateTime,
                    location
            );
    }

    @Override
    public void onStart() {
        astroInit();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun, container, false);
        showView = rootView.findViewById(R.id.sunInfo);

        astroInit();

        showText();

        t2 = new Thread() {

            @Override
            public void run() {
                try {

                    while (!isInterrupted()) {
                        getRefreshTime();
                        Thread.sleep(refreshTime*1000);
                        if(getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showText();
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t2.start();

        return rootView;
    }


    @Override
    public void onStop(){
        getRefreshTime();
        super.onStop();
    }
}
