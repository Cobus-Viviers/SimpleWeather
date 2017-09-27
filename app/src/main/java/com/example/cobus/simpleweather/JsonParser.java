package com.example.cobus.simpleweather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class JsonParser {
    private JSONObject mWeatherData;
    private List<WeatherDataItem> mWeatherDataItems;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    JsonParser(String JSONString) {
        try{
            if(JSONString == null || JSONString.equals(""))
                return;
            mWeatherData = new JSONObject(JSONString);
            mWeatherDataItems = null;
            mWeatherDataItems = new ArrayList<>();
            populateWeatherDataItems();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    //Converts the 3 hour interval data from the Json string to WeatherDataItem objects
    //Each object represents a day
    private void populateWeatherDataItems(){
        try {
            JSONArray weatherList = mWeatherData.getJSONArray("list");

            String currentDate = getDateFromDateString(
                    weatherList.getJSONObject(0).getString("dt_txt"));

            //Create WeatherDataItems
            double currentMin = 990;
            double currentMax = -990;
            int icon = getIcon(weatherList.getJSONObject(0));

            for (int i = 0; i < weatherList.length()-1; i++){
                String nextDate =
                        getDateFromDateString(weatherList.getJSONObject(i +1).getString("dt_txt"));
                double max = getMaxTemp(weatherList.getJSONObject(i));
                double min = getMinTemp(weatherList.getJSONObject(i));
                if(currentMin > min)
                    currentMin = min;
                if(currentMax < max)
                    currentMax = max;

                if(!currentDate.equals(nextDate)){
                    mWeatherDataItems.add(new WeatherDataItem
                            (currentDate, DATE_FORMAT, currentMax, currentMin, icon));
                    currentMax = -99;
                    currentMin = 99;
                    icon = getIcon(weatherList.getJSONObject(i+1));
                    currentDate = nextDate;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    private static String getDateFromDateString(String dateString){
        if(dateString == null || dateString.equals(""))
            return null;
        return dateString.substring(0,10);
    }

    private double getMaxTemp(JSONObject obj){
        double Temp = -99;
        try{
            Temp = obj.getJSONObject("main").getDouble("temp_max");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return Temp;
    }

    private double getMinTemp(JSONObject obj){
        double temperature = 99;
        try{
            temperature = obj.getJSONObject("main").getDouble("temp_min");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return temperature;
    }

    String getLocation(){
        if(!hasData())
            return null;
        String country = null;
        String area = null;
        try{
             country = mWeatherData.getJSONObject("city").getString("country");
             area = mWeatherData.getJSONObject("city").getString("name");

             Locale locale = new Locale("", country);
             country = locale.getDisplayCountry();

        }catch (JSONException e){
            e.printStackTrace();
        }
        return area + ", " + country;
    }

    private int getIcon(JSONObject obj){
        int id = 0;
        try{
            String description = obj.getJSONArray("weather").getJSONObject(0).getString("icon");
            description = description.substring(0,2);
            switch (description){
                case "01": id = R.drawable.art_clear;
                    break;
                case "02": id = R.drawable.art_light_clouds;
                    break;
                case "03":
                case "04": id = R.drawable.art_clouds;
                    break;
                case "09": id = R.drawable.art_light_rain;
                    break;
                case "10": id = R.drawable.art_rain;
                    break;
                case "11": id = R.drawable.art_storm;
                    break;
                case "13": id = R.drawable.art_snow;
                    break;
                case "50": id = R.drawable.art_fog;
                    break;
                default: id = R.drawable.art_clear;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return id;
    }

    WeatherDataItem getTodaysWeather(){
        if(!hasData())
            return null;
        return mWeatherDataItems.get(0);
    }

    WeatherDataItem[] getWeatherDataItems(){
        if(!hasData())
            return null;
        return mWeatherDataItems.toArray(new WeatherDataItem[mWeatherDataItems.size()]);
    }

    boolean hasData(){
        return mWeatherDataItems.size() > 0;
    }

}
