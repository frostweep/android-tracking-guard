package com.frostweepgames.apptracker.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Bundle;

import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.models.GPSModel;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;


/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class GetGPSController implements IController, LocationListener {

    private Activity _currentActivity;
    private Context _currentContext;

    private Location _currentLocation;
    private LocationManager _locationManager;
    private ArrayList<GPSModel> _currentGPSInfo;


    public GetGPSController(Activity currentActivity) {

        _currentActivity = currentActivity;
        _currentContext = _currentActivity.getApplicationContext();
        _currentGPSInfo = new ArrayList<GPSModel>();
    }


    public GetGPSController(Context context) {
        _currentContext = context;
        _currentGPSInfo = new ArrayList<GPSModel>();
    }

    public Object getContent() {

        if(!getGPS(LocationManager.NETWORK_PROVIDER))
            getGPS(LocationManager.GPS_PROVIDER);

        return _currentGPSInfo;
    }

    @SuppressLint("MissingPermission")
    public boolean getGPS(String provider) {

        if (_currentActivity != null) {
            if (!Helper.mayRequestPermission(_currentActivity, ACCESS_FINE_LOCATION, Constants.REQUEST_PERMISSION_VALUE)) {
                return false;
            }
            if (!Helper.mayRequestPermission(_currentActivity, ACCESS_COARSE_LOCATION, Constants.REQUEST_PERMISSION_VALUE)) {
                return false;
            }
        }

        try {
            _locationManager = (LocationManager) _currentContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = _locationManager.isProviderEnabled(provider);

            if (!isGPSEnabled) {
                return false;

            } else {
                _locationManager.requestLocationUpdates(provider, Constants.MIN_TIME_BW_UPDATES, Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                _currentLocation = _locationManager.getLastKnownLocation(provider);

                if(_currentLocation != null) {
                    GPSModel currGPSInfo = new GPSModel();
                    currGPSInfo.lat = Double.toString(_currentLocation.getLatitude());
                    currGPSInfo.lng = Double.toString(_currentLocation.getLongitude());
                    currGPSInfo.tm = Helper.getTimestampFromDate(Helper.getCurrentDate());
                    currGPSInfo.accuracy = Float.toString(_currentLocation.getAccuracy());

                    _currentGPSInfo.add(currGPSInfo);

                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        _currentLocation = location;
/*
        GPSModel currGPSInfo = new GPSModel();

        currGPSInfo.lat = Double.toString(_currentLocation.getLatitude());
        currGPSInfo.lng = Double.toString(_currentLocation.getLongitude());
        currGPSInfo.tm = Helper.getTimestampFromDate(Helper.getCurrentDate());
        currGPSInfo.accuracy = Float.toString(_currentLocation.getAccuracy());

        for(GPSModel model: _currentGPSInfo)
        {
            if(model.tm.equals(currGPSInfo.tm))
                return;
        }

        _currentGPSInfo.add(currGPSInfo); */
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}