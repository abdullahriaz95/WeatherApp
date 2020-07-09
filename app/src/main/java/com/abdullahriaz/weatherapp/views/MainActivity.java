package com.abdullahriaz.weatherapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.abdullahriaz.weatherapp.databinding.ActivityMainBinding;
import com.abdullahriaz.weatherapp.models.Weather;
import com.abdullahriaz.weatherapp.viewmodels.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    WeatherViewModel weatherViewModel;
    int temperature;
    String currentScale = "F";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (!checkPermissions()) {
            requestPermissions();
        }
        if (!checkGPSStatus(this)) {
            Toast.makeText(this, "Turn your GPS ON", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        weatherViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(WeatherViewModel.class);

        weatherViewModel.getWeatherDetails().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather != null) {
                    if (currentScale.equals("F")) {
                        temperature = (int) weather.getTemp();
                        binding.tempDegree.setText(temperature + "°" + currentScale);
                        Log.d(TAG, "onChanged: " + weather.toString());
                    } else {
                        double temp = temperature * (9f / 5) + 32;
                        binding.tempDegree.setText(temperature + "°" + currentScale);
                    }


                }
            }
        });
        weatherViewModel.updateWeather();


        binding.btnChangeScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScale(binding);
            }
        });


        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherViewModel.updateWeather();
//                changeDayNightTheme();
            }
        });
    }

    private void changeScale(ActivityMainBinding binding) {
        if (currentScale.equals("F")) {
            float temp = (temperature - 32) * (5f / 9);
            currentScale = "C";
            binding.tempDegree.setText((int) temp + "°" + currentScale);
            binding.btnChangeScale.setText("F");
            temperature = (int) temp;
        } else {
            double temp = temperature * (9f / 5) + 32;
            currentScale = "F";
            binding.tempDegree.setText((int) temp + "°" + currentScale);
            binding.btnChangeScale.setText("C");
            temperature = (int) temp;
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == 101) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!checkGPSStatus(this)) {
                        Toast.makeText(this, "Turn your GPS ON", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }
            }
        }
    }


    public static boolean checkGPSStatus(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }


}
