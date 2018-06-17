package com.example.pangrett.astroweather.service;

import com.example.pangrett.astroweather.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
