package com.example.cobus.simpleweather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class JsonParser {
    JSONObject mWeatherData;

    public JsonParser(String JSONString) {
        try{
            mWeatherData = new JSONObject(JSONString);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getMaxTemp(){
        String Temp = null;
        try{
            Temp = mWeatherData.getJSONObject("main").getString("temp_max");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return Integer.toString((int) Math.round(Double.parseDouble(Temp)/10));
    }

    public String getMinTemp(){
        String Temp = null;
        try{
            Temp = mWeatherData.getJSONObject("main").getString("temp_min");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return Integer.toString((int) Math.round(Double.parseDouble(Temp)/10));
    }

    public String getLocation(){
        String country = null;
        String area = null;
        try{
            country = mWeatherData.getJSONObject("sys").getString("country");
            Locale locale = new Locale("", country);
            country = locale.getDisplayCountry();
            area = mWeatherData.getString("name");

        }catch (JSONException e){
            e.printStackTrace();
        }
        return area + ", " + country;
    }

    public int getIcon(){
        int id = 0;
        try{
            String description = mWeatherData.getJSONArray("weather").getJSONObject(0).getString("description");
            switch (description){
                case "clear sky": id = R.drawable.art_clear;
                    break;
                case "few clouds": id = R.drawable.art_light_clouds;
                    break;
                case "scattered clouds": id = R.drawable.art_clouds;
                    break;
                case "broken clouds": id = R.drawable.art_clouds;
                    break;
                case "shower rain": id = R.drawable.art_light_rain;
                    break;
                case "rain": id = R.drawable.art_rain;
                    break;
                case "thunderstorm": id = R.drawable.art_storm;
                    break;
                case "snow": id = R.drawable.art_snow;
                    break;
                case "mist": id = R.drawable.art_fog;
                    break;
                default: id = 0;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return id;
    }


}
