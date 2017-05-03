package pl.lodz.trackingsystem.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import pl.lodz.trackingsystem.utils.ServerRequests;

public class GPSTracker extends Service implements LocationListener {

    /**
     * Param for requestLocationUpdate in sec
     * min time of update the location (it's not the real interval)
     */
    private final long minUpdateTime = 60;

    /**
     * Param for requestLocationUpdate in meters
     * min disctance of update the location
     */
    private final float minDiscance = 100;

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
     * Timer variable
     */
    private Timer timer;

    /**
     * Login
     */
    private String login = "";
    /**
     * User name
     */
    private String userName = "";

    /**
     * Constructor for class, initialize gps provider
     * @param context
     * @param timePeriod in minutes
     */
    public GPSTracker(Context context, long timePeriod, String login, String userName) {
        this.context = context;
        this.login = login;
        this.userName = userName;
        init(timePeriod);
    }

    /**
     * Method for stop listening gps signal
     */
    public void stopUsingGPSTracker() {
        if (locationManager != null) {
            try{
                locationManager.removeUpdates(GPSTracker.this);
                stopTimer();
            } catch (SecurityException e) {
                e.printStackTrace(); // TODO change for logger
            }
        }
    }

    /**
     * Method for initialize gps provider
     * @param timePeriod in minutes
     */
    private void init(long timePeriod) {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE); // get service from application context
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * minUpdateTime , minDiscance, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // updating our variables with last known position
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            startTimer(timePeriod);
        } catch (SecurityException e) {
          // we don't have access
            e.printStackTrace(); // TODO change for logger
        } catch (Exception e) {
            e.printStackTrace(); // TODO change for logger
        }
    }

    private void startTimer(long time) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // send coords to server every time when method is called
                ServerRequests.sendCoords(login, String.valueOf(latitude), String.valueOf(longitude), userName);
            }
        };

        // schedule our timer
        timer.schedule(task, 1000, time*60*1000);
    }

    private void stopTimer() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
