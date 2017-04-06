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
import android.widget.EditText;

import pl.lodz.trackingsystem.utils.AppUtils;
import pl.lodz.trackingsystem.utils.Permissions;

public class UserActivity extends AppCompatActivity {

    private Button acceptButton;
    private EditText nameInput;

    private SharedPreferences settings;

    private final int waitTime = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Permissions.getPermissions(this);
        checkPermissions(); // we need to wait for permissions

        settings = getSharedPreferences(AppUtils.PREFS_NAME, 0);
        if(!"".equals(settings.getString("USER_NAME", ""))) {
            // already know user
            nextAction();
        }

        nameInput = (EditText) findViewById(R.id.nameInput);

        acceptButton = (Button) findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameInput.getText().toString();

                if(TextUtils.isEmpty(userName)) {
                    nameInput.setError(getString(R.string.user_name_error));
                    return;
                }

                saveUserName(userName);
                nextAction();
            }
        });
    }

    private void nextAction() {
        Intent intent = new Intent(UserActivity.this, GpsService.class);
        finishAffinity();
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
}
