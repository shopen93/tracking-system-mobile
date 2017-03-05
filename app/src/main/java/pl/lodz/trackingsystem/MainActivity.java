package pl.lodz.trackingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.URLEncoder;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.lodz.trackingsystem.gps.GPSTracker;
import pl.lodz.trackingsystem.utils.Permissions;
import pl.lodz.trackingsystem.utils.ServerRequests;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button showCoordsButton;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this, 1);
            }
        });

        showCoordsButton = (Button) findViewById(R.id.button2);
        showCoordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                Toast.makeText(getApplicationContext(), "Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_LONG).show();
                ServerRequests.sendCoords(String.valueOf(latitude), String.valueOf(longitude));
            }
        });

        Permissions.getPermissions(this);
    }

}
