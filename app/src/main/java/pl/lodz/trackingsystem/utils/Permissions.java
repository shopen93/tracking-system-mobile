package pl.lodz.trackingsystem.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class Permissions {

    private static String[] permissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};

    public static void getPermissions(Activity activity) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, 0);
        }
    }

}
