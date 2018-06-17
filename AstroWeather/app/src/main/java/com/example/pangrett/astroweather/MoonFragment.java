package com.example.pangrett.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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


public class MoonFragment extends Fragment {

    TextView showView;
    String date;
    int refreshTime=1;

    Thread t2;

    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    AstroCalculator astroCalculator;
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;

    private int getTimeZone(){
        Calendar c = Calendar.getInstance();

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();

        if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        return offset / 1000 / 60 / 60;
    }

    public void showText() {
        showView.setText(
                "Moonrise:\n" + astroCalculator.getMoonInfo().getMoonrise() +
                        "\nMoonset:\n" + astroCalculator.getMoonInfo().getMoonset() +
                        "\nNext Full Moon:\n" + astroCalculator.getMoonInfo().getNextFullMoon() +
                        "\nNext New Moon:\n" + astroCalculator.getMoonInfo().getNextNewMoon() +
                        "\nAge:\n" + astroCalculator.getMoonInfo().getAge() +
                        "\nIllumination:\n" + astroCalculator.getMoonInfo().getIllumination()
        );
    }

    public void getRefreshTime(){
        if(getActivity() != null){
            SharedPreferences sharedPref = getActivity().getSharedPreferences("infoValues.xml", 0);
            refreshTime = Integer.valueOf(sharedPref.getString("Refresh", String.valueOf(getActivity().getString(R.string.Refresh))));
        }

    }

    public void astroInit() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("weather.xml", 0);

        date = df.format(Calendar.getInstance().getTime());

        astroDateTime = new AstroDateTime(
                Integer.valueOf(date.substring(0, date.indexOf("."))),
                Integer.valueOf(date.substring(date.indexOf(".") + 1, date.lastIndexOf("."))),
                Integer.valueOf(date.substring(date.lastIndexOf(".") + 1, date.indexOf(" "))),
                Integer.valueOf(date.substring(date.indexOf(" ") + 1, date.indexOf(":"))),
                Integer.valueOf(date.substring(date.indexOf(":") + 1, date.lastIndexOf(":"))),
                Integer.valueOf(date.substring(date.lastIndexOf(":") + 1, date.length())),
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
                R.layout.fragment_moon, container, false);
        showView = rootView.findViewById(R.id.moonInfo);

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
