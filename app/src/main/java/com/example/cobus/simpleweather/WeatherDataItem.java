package com.example.cobus.simpleweather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class WeatherDataItem {
    private String mDate;
    private double mMaxTemp;
    private double mMinTemp;
    private int mIcon;
    private String mDateFormat;

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    String getMaxTemp() {
        if(mMaxTemp == -990)
            return Integer.toString(-99);
        return Integer.toString((int) Math.round(mMaxTemp/10));
    }

    public void setMaxTemp(double mMaxTemp) {
        this.mMaxTemp = mMaxTemp;
    }

    String getMinTemp() {
        if(mMinTemp == 990)
            return Integer.toString(99);
        return Integer.toString((int) Math.round(mMinTemp/10));
    }

    public void setMinTemp(double mMinTemp) {
        this.mMinTemp = mMinTemp;
    }

    int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    WeatherDataItem(String date, String dateFormat, double maxTemp, double minTemp, int icon) {
        mDate = date;
        mMaxTemp = maxTemp;
        mMinTemp = minTemp;
        mIcon = icon;
        mDateFormat = dateFormat;

    }

    String getDayOfWeek() throws WeatherItemException{
        try{
            DateFormat dataItemFormat = new SimpleDateFormat(mDateFormat, Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();

            if(mDate.equals(dataItemFormat.format(calendar.getTime())))
                return "Today";

            calendar.add(Calendar.DATE, 1);
            if(mDate.equals(dataItemFormat.format(calendar.getTime())))
                return "Tomorrow";

            calendar.setTime(dataItemFormat.parse(mDate));
            DateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            return weekDayFormat.format(calendar.getTime());

        }catch (IllegalArgumentException | ParseException e){
            e.printStackTrace();
            throw new WeatherItemException("Invalid Date Format Provided");
        }

    }

    class WeatherItemException extends Exception{
        WeatherItemException(String message) {
            super(message);
        }
    }
}
