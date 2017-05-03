package pl.lodz.trackingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import pl.lodz.trackingsystem.utils.AppUtils;

public class ApplicationSelection extends AppCompatActivity {

    private Button acceptButton;
    private RadioGroup radioGroup;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_selection);

        settings = getSharedPreferences(AppUtils.PREFS_NAME, 0);
        if(settings.getBoolean("AUTO_LOGIN", false)) {
            // auto start application
            nextAction();
        }

        acceptButton = (Button) findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chosenRadio = radioGroup.getCheckedRadioButtonId();

                if(R.id.monitRadio == chosenRadio) {
                    // we will monit this device and send data to server
                    saveApplicationMode(AppUtils.MONIT_APP);
                } else {
                    // we are admin, we only receive monits from server
                    saveApplicationMode(AppUtils.ADMIN_APP);
                }

                nextAction();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.selectionGroup);
    }

    private void nextAction() {
        Intent intent = new Intent(ApplicationSelection.this, LoginActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    private void saveApplicationMode(String appMode) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("APPLICATION_MODE", appMode);
        editor.commit();
    }

}
