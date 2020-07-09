package com.abdullahriaz.weatherapp;

public class WeatherApiInfo {
    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String WEATHER_API_APP_ID = "e7ef24b0317ec51bb823e76e9a0ef965";

    public static String getCompleteURL(String latitude, String longitude) {
        return WEATHER_API_URL + "?lat=" + latitude + "&lon=" + longitude + ",&APPID=" + WEATHER_API_APP_ID;
    }

}
