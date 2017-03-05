package pl.lodz.trackingsystem.utils;

import android.os.Handler;

public class AppUtils {

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
