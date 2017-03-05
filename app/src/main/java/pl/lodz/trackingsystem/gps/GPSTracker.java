package pl.lodz.trackingsystem.gps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import pl.lodz.trackingsystem.utils.AppUtils;
import pl.lodz.trackingsystem.utils.ServerRequests;

public class GPSTracker extends Service implements LocationListener {

    /**
     * Param used in waiting methods
     */
    private final int waitTime = 5000;

    /**
     * Context from Action class
     */
    private final Context context;

    /**
     * Android LocationManager variable
     */
    private LocationManager locationManager;

    /**
     * Android Location variable
     */
    private Location location;

    /**
     * Last updated latitude value
     */
    private double latitude;

    /**
     * Last updated longitude value;
     */
    private double longitude;

    /**
     * Constructor for class, initialize gps provider
     * @param context
     * @param timePeriod in minutes
     */
    public GPSTracker(Context context, long timePeriod) {
        this.context = context;
        init(timePeriod);
    }

    /**
     * Method for initialize gps provider
     * @param timePeriod in minutes
     */
    public void init(long timePeriod) {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE); // get service from application context

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AppUtils.wait(waitTime); // if we still don't have permission we are waiting and trying one more time
                init(timePeriod);
            }

            // we got access to use gps so we are setting listener on gps
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * timePeriod, 100, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // updating our variables with last known position
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace(); // TODO change for logger
        }
    }

    /**
     * Method for stop listening gps signal
     */
    public void stopUsingGPSTracker() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AppUtils.wait(waitTime);
                stopUsingGPSTracker();
            }

            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    // below are methods from interface

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // update our location
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        ServerRequests.sendCoords(String.valueOf(latitude), String.valueOf(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
