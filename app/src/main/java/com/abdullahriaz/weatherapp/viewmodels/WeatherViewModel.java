package com.abdullahriaz.weatherapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abdullahriaz.weatherapp.Repository.WeatherRepository;
import com.abdullahriaz.weatherapp.models.Weather;

public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;
    private MutableLiveData<Weather> getCurrentWeather;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(application);
        getCurrentWeather = weatherRepository.getWeatherMutableLiveData();
    }

    public MutableLiveData<Weather> getWeatherDetails() {
        return getCurrentWeather;
    }

    public void updateWeather() {
        weatherRepository.getCurrentWeatherDetails();
    }
}
