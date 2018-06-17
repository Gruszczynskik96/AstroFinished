package com.example.pangrett.astroweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentsPagesAdapter extends FragmentStatePagerAdapter {

    public FragmentsPagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BasicWeatherFragment();
            case 1:
                return new AdditionalWeatherFragment();
            case 2:
                return new FutureWeatherFragment();
            case 3:
                return new SunFragment();
            case 4:
                return new MoonFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
