package com.example.cobus.simpleweather;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

final class NetworkManager {
    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private final static String LATITUDE = "lat";
    private final static String LONGITUDE = "lon";
    private final static String API_KEY_QUERY = "appid";
    private final static String API_KEY = "71b35bc21aab484b3a31c30e1a5a3537";
    private final static String UNITS = "units";
    private final static String UNIT_TYPE = "metric";

    static URL buildUrl(String latitude, String longitude){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LATITUDE, latitude)
                .appendQueryParameter(LONGITUDE, longitude)
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .appendQueryParameter(UNITS, UNIT_TYPE)
                .build();

        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    static String getApiData(URL url) throws IOException{
       HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try{
            InputStream stream = connection.getInputStream();

            Scanner scanner = new Scanner(stream).useDelimiter("\\A");

            if(scanner.hasNext()){
                return scanner.next();
            }
            return null;
        }finally {
            connection.disconnect();
        }
    }
}
