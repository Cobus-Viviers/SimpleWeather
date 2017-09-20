package com.example.cobus.simpleweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final String LOG_TAG = "Main_InfoLog";
    TextView mDateTextView;
    TextView mMaxTempTextView;
    TextView mMinTempTextView;
    TextView mLocationTextView;
    ImageView mWeatherImageView;
    TextView mErrorTextView;
    LinearLayout mDataContainerLayout;
    ProgressBar mMainProgressBar;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDateTextView = (TextView) findViewById(R.id.tv_date);
        mMaxTempTextView = (TextView) findViewById(R.id.tv_max_temp);
        mMinTempTextView = (TextView) findViewById(R.id.tv_min_temp);
        mWeatherImageView = (ImageView) findViewById(R.id.iv_weather_image);
        mLocationTextView = (TextView) findViewById(R.id.tv_location);
        mErrorTextView = (TextView) findViewById(R.id.tv_error_message);
        mDataContainerLayout = (LinearLayout)findViewById(R.id.ll_container_layout_data);
        mMainProgressBar = (ProgressBar)findViewById(R.id.pb_main);

        showProgressBar();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(1000 * 60 * 15 );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //Check for permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(!connectionResult.hasResolution()){
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("lovc", "location: " + location);
        if(location == null){
            showError();
            return;
        }
        URL url = NetworkManager.buildUrl(Double.toString(location.getLatitude()),
                Double.toString(location.getLongitude()));
        new NetworkAsycTask().execute(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                                mLocationRequest, this);
                    onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
                    showData();
                }
                else{
                    showError();
                }
        }
    }

    class NetworkAsycTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected String doInBackground(URL... params) {
            String json = null;
            try{
                json = NetworkManager.getApiData(params[0]);
            }catch (IOException e){
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            displayWeatherDataFromJson(s);
        }
    }

    private void displayWeatherDataFromJson(String Json){
        if(Json == null || Json.equals("")){
            showError();
            return;
        }
        JsonParser parser = new JsonParser(Json);

        String temperature = String.format(getResources().getString(R.string.temperature),
                "Max", parser.getMaxTemp());
        mMaxTempTextView.setText(temperature);

        temperature = String.format(getResources().getString(R.string.temperature),
                "Min", parser.getMinTemp());
        mMinTempTextView.setText(temperature);
        mDateTextView.setText(getTodaysDate());
        mLocationTextView.setText(parser.getLocation());
        mWeatherImageView.setImageResource(parser.getIcon());
        showData();
    }
    private String getTodaysDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();
        return "TODAY, " + dateFormat.format(date).toUpperCase();
    }

    private void showError(){
        mErrorTextView.setVisibility(View.VISIBLE);
        mDataContainerLayout.setVisibility(View.INVISIBLE);
        mMainProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showData(){
        mDataContainerLayout.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMainProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar(){
        mDataContainerLayout.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMainProgressBar.setVisibility(View.VISIBLE);
    }

}




