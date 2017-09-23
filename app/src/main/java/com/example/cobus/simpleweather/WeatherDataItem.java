package com.example.cobus.simpleweather;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeatherDataItem {
    private String mDate;
    private double mMaxTemp;
    private double mMinTemp;
    private int mIcon;
    private String mDateFormat;

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmMaxTemp() {
        if(mMaxTemp == -990)
            return Integer.toString(-99);
        return Integer.toString((int) Math.round(mMaxTemp/10));
    }

    public void setmMaxTemp(double mMaxTemp) {
        this.mMaxTemp = mMaxTemp;
    }

    public String getmMinTemp() {
        if(mMinTemp == 990)
            return Integer.toString(99);
        return Integer.toString((int) Math.round(mMinTemp/10));
    }

    public void setmMinTemp(double mMinTemp) {
        this.mMinTemp = mMinTemp;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public WeatherDataItem(String mDate, String mDateFromat, double mMaxTemp, double mMinTemp, int mIcon) {
        this.mDate = mDate;
        this.mMaxTemp = mMaxTemp;
        this.mMinTemp = mMinTemp;
        this.mIcon = mIcon;
        this.mDateFormat = mDateFromat;

    }

    public String getDayOfWeek() throws WeatherItemException{
        try{


            DateFormat dataItemFormat = new SimpleDateFormat(mDateFormat, Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();
            Log.i("pointy", mDate);
            if(mDate.equals(dataItemFormat.format(calendar.getTime())))
                return "Today";
            calendar.add(Calendar.DATE, 1);
            if(mDate.equals(dataItemFormat.format(calendar.getTime())))
                return "Tomorrow";
            calendar.setTime(dataItemFormat.parse(mDate));
            DateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            return weekDayFormat.format(calendar.getTime());
        }catch (IllegalArgumentException | ParseException e){

            Log.i("pointerr", e.getMessage());
            e.printStackTrace();
            throw new WeatherItemException("Invalid Date Format Provided");
        }

    }

    public class WeatherItemException extends Exception{
        public WeatherItemException(String message) {
            super(message);
        }
    }
}
