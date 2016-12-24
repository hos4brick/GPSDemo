package com.example.ryan.gpsdemo.activities.color;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryan.gpsdemo.R;
import com.example.ryan.gpsdemo.activities.menu.MainMenuActivity;
import com.gps.demo.model.event.EventModel;

import java.util.Date;

import callback.ViewCallBackInterface;

public class GpsDemoActivity extends AppCompatActivity implements LocationListener, OnClickListener,
        ViewCallBackInterface{
    public static final int BUTTON_MENU_TAG = 0;
    public static final String EVENT_MODEL_KEY = "EVENT_MODEL_KEY";

    private static final int FINE_LOCATION_PERMISSION_REQEUST = 8383;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 24601;

    private BroadcastReceiver broadcastReceiver = null;

    LocationManager locationManager;
    boolean enabled;
    Location location;
    String provider;
    double latitude = 0;
    double longitude = 0;
    TextView latitudeField;
    TextView longitudeField;
    ImageView colorBox;

    EventModel eventModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_demo_activity_layout);

        latitudeField = (TextView) findViewById(R.id.gps_lat_text_view);
        longitudeField = (TextView) findViewById(R.id.gps_long_text_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        colorBox = (ImageView) findViewById(R.id.color_box);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_PERMISSION_REQEUST);

            // should we explain?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            }
        } else {
            locationEnabled();
        }

        Intent intent = getIntent();

        broadcastReceiver = new GpsDemoBroadcastReceiver(this);

        if (intent.hasExtra(EVENT_MODEL_KEY)) {
            String eventKey = intent.getStringExtra(EVENT_MODEL_KEY);
            eventModel = MainMenuActivity.getApplicationModel().getEvent(eventKey);
            eventModel.start(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION_REQEUST:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationEnabled();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        locationUpdate(location);
        System.out.println("+++++++++++++++++++++TIME!!!: " + location.getTime() + " " + ((new Date()).getTime()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method sub ... what?
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    private void removeLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onClick(View view) {
        Object viewTag = view.getTag();

        if (viewTag instanceof Integer) {
            int intViewTag = (int) viewTag;

            switch (intViewTag) {
                case BUTTON_MENU_TAG:
                    handleMenuButtonClick();
                    break;
            }
        }
    }

    private void handleMenuButtonClick() {
        Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
        startActivity(mainMenuIntent);
    }

    private void locationUpdate(Location location) {
        this.location = location;
        locationUpdate();
    }
    public void locationUpdate() {

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            latitudeField.setText(String.valueOf(latitude));
            longitudeField.setText(String.valueOf(longitude));

            if (null != eventModel) {
                String hexColor = eventModel.getHexColor(latitude, longitude);
                updateScreenColor(hexColor);
            }

        } else {
            latitudeField.setText("Location not available");
            longitudeField.setText("Location not available");
        }
    }

    private void updateScreenColor(String hexColor) {
        int androidColorCode = Color.parseColor("#" + hexColor);
        colorBox.setBackgroundColor(androidColorCode);
    }




    @Override
    public void update(CallBackCommand command) {
        Intent intent = new Intent();
        intent.setAction(command.name());
        this.sendBroadcast(intent);
    }

    private void locationEnabled() {


        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
        }
        locationUpdate(location);
    }
}