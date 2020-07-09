package com.abdullahriaz.weatherapp.network;

import android.app.Application;
import android.net.UrlQuerySanitizer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.abdullahriaz.weatherapp.models.Weather;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestManager {
    private static RequestManager requestManager;
    private Application application;
    private static final String TAG = RequestManager.class.getSimpleName();
    private MutableLiveData<Weather> weatherMutableLiveData;


    public static RequestManager getInstance(Application application, MutableLiveData<Weather> mutableLiveData) {
        if (requestManager == null) {
            requestManager = new RequestManager(application, mutableLiveData);
        }
        return requestManager;
    }

    public RequestManager(Application application, MutableLiveData<Weather> mutableLiveData) {
        this.application = application;
        this.weatherMutableLiveData = mutableLiveData;
    }

    public void getWeatherData(String url) {
        RequestQueue queue = Volley.newRequestQueue(application);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject mainObject = (JSONObject) jsonObject.get("main");
                            Log.d(TAG, "mainObject: " + mainObject);
                            Weather weather = new Gson().fromJson(mainObject.toString(), Weather.class);
                            Log.d(TAG, "weather: " + weather.toString());

                            weatherMutableLiveData.postValue(weather);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onError: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
