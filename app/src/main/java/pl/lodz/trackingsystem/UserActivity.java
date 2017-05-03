package pl.lodz.trackingsystem;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import pl.lodz.trackingsystem.utils.AppUtils;
import pl.lodz.trackingsystem.utils.Permissions;

public class UserActivity extends AppCompatActivity {

    private Button acceptButton;
    private EditText nameInput;
    private EditText interval;
    private CheckBox autoStart;

    private SharedPreferences settings;

    private final int waitTime = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Permissions.getPermissions(this);
        checkPermissions(); // we need to wait for permissions

        settings = getSharedPreferences(AppUtils.PREFS_NAME, 0);
        if(settings.getBoolean("AUTO_LOGIN", false)) {
            // auto start application
            nextAction();
        }

        nameInput = (EditText) findViewById(R.id.nameInput);

        interval = (EditText) findViewById(R.id.interval);

        autoStart = (CheckBox) findViewById(R.id.autoStart);

        acceptButton = (Button) findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameInput.getText().toString();
                int time = Integer.valueOf(interval.getText().toString());

                if(TextUtils.isEmpty(userName)) {
                    nameInput.setError(getString(R.string.user_name_error));
                    return;
                }
                if(time <= 0) {
                    interval.setError(getString(R.string.user_error_interval));
                    return;
                }

                saveUserName(userName);
                saveTime(time);
                saveAutoStart();
                nextAction();
            }
        });
    }

    private void nextAction() {
        Intent intent = new Intent(UserActivity.this, GpsService.class);
        intent.putExtra("LOGIN", settings.getString("LOGIN", ""));
        intent.putExtra("USER_NAME", settings.getString("USER_NAME", ""));
        intent.putExtra("USER_TIME", settings.getInt("USER_TIME", 5));
        finishAffinity();
        Toast.makeText(getApplicationContext(), getString(R.string.user_toast_info), Toast.LENGTH_SHORT).show();
        startService(intent);
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AppUtils.wait(waitTime); // if we still don't have permission we are waiting and trying one more time
            checkPermissions();
        }
    }

    private void saveUserName(String userName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("USER_NAME", userName);
        editor.commit();
    }

    private void saveTime(int interval) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("USER_TIME", interval);
        editor.commit();
    }

    private void saveAutoStart() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("AUTO_LOGIN", autoStart.isChecked());
        editor.commit();
    }
}
