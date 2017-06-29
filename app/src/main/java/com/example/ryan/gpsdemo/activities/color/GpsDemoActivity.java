package com.example.ryan.gpsdemo.activities.color;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ryan.gpsdemo.R;
import com.example.ryan.gpsdemo.activities.menu.MainMenuActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gps.demo.model.event.EventModel;

import callback.ViewCallBackInterface;

import static com.example.ryan.gpsdemo.R.string.FINE_LOCATION_PERMISSION_REQEUST;

public class GpsDemoActivity extends AppCompatActivity implements OnClickListener,
        ViewCallBackInterface {
    public static final int BUTTON_MENU_TAG = 0;
    public static final String EVENT_MODEL_KEY = "EVENT_MODEL_KEY";

    private static final int REQUEST_ACCESS_FINE_LOCATION = 8383;

    private static final long LOCATION_REQUEST_INTERVAL = 5000;
    private static final long LOCATION_REQUEST_FASTEST_INTERVAL = 500;

    private BroadcastReceiver broadcastReceiver = null;

    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LocationRequest locationRequest = null;
    private LocationCallback locationCallback = null;

    private int accessFineLocationPermissions = PackageManager.PERMISSION_DENIED;

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    locationUpdate(location);
                }
            }
        };

        colorBox = (ImageView) findViewById(R.id.color_box);

        requestPermissions();

        Intent intent = getIntent();

        broadcastReceiver = new GpsDemoBroadcastReceiver(this);

        if (intent.hasExtra(EVENT_MODEL_KEY)) {
            String eventKey = intent.getStringExtra(EVENT_MODEL_KEY);
            eventModel = MainMenuActivity.getApplicationModel().getEvent(eventKey);
            eventModel.start(this);
        }
    }

    public void requestPermissions() {
        accessFineLocationPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (PackageManager.PERMISSION_DENIED == accessFineLocationPermissions) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        } else {
            locationEnabled();
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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

        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build());


        locationSettingsResponseTask.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLastLocation();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            ResolvableApiException resolvable = (ResolvableApiException) e;
//                            resolvable.startResolutionForResult(GpsDemoActivity.this,
//                                    REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException sendEx) {
//                            // Ignore the error.
//                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });


//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria, false);
//
//        enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        if (!enabled) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
    }

    public void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions();
            return;
        }

        fusedLocationProviderClient.getLastLocation().
                addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            locationUpdate(location);
                        }
                    }
                });
    }
}