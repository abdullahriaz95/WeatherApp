package com.abdullahriaz.weatherapp.Repository;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.abdullahriaz.weatherapp.WeatherApiInfo;
import com.abdullahriaz.weatherapp.models.Weather;
import com.abdullahriaz.weatherapp.network.RequestManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class WeatherRepository {
    private static final String TAG = WeatherRepository.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Application application;
    private MutableLiveData<Weather> mutableLiveData;

    public WeatherRepository(Application application) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application);
        this.application = application;
        mutableLiveData = new MutableLiveData<Weather>();
    }

    public void getCurrentWeatherDetails() {

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d(TAG, "onSuccess: Location Not Null" + location.getLatitude());
                    String url = WeatherApiInfo.getCompleteURL(String.valueOf((int) location.getLatitude()), String.valueOf((int) location.getLongitude()));
                    RequestManager.getInstance(application, mutableLiveData).getWeatherData(url);
                } else {
                    Log.d(TAG, "onSuccess: Location Not");
                }
            }
        });

    }


    public MutableLiveData<Weather> getWeatherMutableLiveData() {
        return mutableLiveData;
    }


}
