package pl.lodz.trackingsystem;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AlertsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO start timer and make requests
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
