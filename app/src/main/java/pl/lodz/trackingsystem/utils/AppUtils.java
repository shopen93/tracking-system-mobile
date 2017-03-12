package pl.lodz.trackingsystem.utils;

import android.os.Handler;

public class AppUtils {

    public static final String PREFS_NAME = "TrackingSystemFile";

    public static final String MONIT_APP = "MONIT";

    public static final String ADMIN_APP = "ADMIN";

    private static Handler handler = new Handler();

    public static void wait(int ms) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // just wait and do nothing
            }
        }, ms);
    }

}
