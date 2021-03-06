package pl.lodz.trackingsystem;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import pl.lodz.trackingsystem.gps.GPSTracker;

public class GpsService extends Service {

    GPSTracker gps;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gps = new GPSTracker(this, intent.getIntExtra("USER_TIME", 5),
                intent.getStringExtra("LOGIN"), intent.getStringExtra("USER_NAME"));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        gps.stopUsingGPSTracker();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
