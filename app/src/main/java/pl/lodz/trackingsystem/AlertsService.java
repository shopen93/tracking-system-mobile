package pl.lodz.trackingsystem;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import pl.lodz.trackingsystem.utils.ServerRequests;

public class AlertsService extends Service {

    private Timer timer;
    private String login;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        login = intent.getStringExtra("LOGIN");
        startTimer(1);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimer(long time) {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ServerRequests.CheckMessageTask messageTask = new ServerRequests.CheckMessageTask(login, getApplicationContext());
                messageTask.execute((Void) null);
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
}
