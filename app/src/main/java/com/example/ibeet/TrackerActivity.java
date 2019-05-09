package com.example.ibeet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TrackerActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    MarkerOptions mo;
    Marker currentPosMarker;
    LocationManager locationManager;
    private Button center;
    private TextView totalDist;
    private TextView avSpd;
    private SharedPreferences myPreffs;
    private long backPressedTime;
    private Toast backToast;
    MarkerHandler beenLocats= new MarkerHandler();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPreffs = getSharedPreferences("com.example.ibeet.DATES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreffs.edit();
        float oldAvSpeed = myPreffs.getFloat("thisSessionAverageSpeed", 0);
        editor.putFloat("lastSessionAverageSpeed", oldAvSpeed);
        editor.commit();

        setContentView(R.layout.activity_tracker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(0,0)).title("My Current Location");
        requestLocation();
        totalDist = findViewById(R.id.totaldistance);
        avSpd = findViewById(R.id.averagespeed);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentPosMarker = mMap.addMarker(mo);

        // Add a marker over Metropolia and move the camera
        LatLng metropolia = new LatLng(60.258617, 24.844468);
        mMap.addMarker(new MarkerOptions().position(metropolia).title("Metropolia, here I was made!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(metropolia));
    }

    @Override
    public void onLocationChanged(Location location) {
        final LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        //marker is the current position of the device
        //Reset current position
        currentPosMarker.setPosition(myCoordinates);

        //Draw line between last location and current location
        if(beenLocats.getCurrentIndex()==0){} else {
            mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(beenLocats.getLocationLat(),beenLocats.getLocationLng()), new LatLng (myCoordinates.latitude, myCoordinates.longitude))
                    .width(5).color(Color.RED));
        }
        //Call for distance calculation
        beenLocats.setNewMarker(location);

        //Button to center view on the current location
        center = findViewById(R.id.buttoncenter);
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomlvl = 16.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, zoomlvl));
            }
        });

        totalDist.setText(beenLocats.getTotalDistance());
        avSpd.setText(beenLocats.getAverageSpeed());
    }


    @Override
    protected void onPause() {
        super.onPause();

        myPreffs = getSharedPreferences("com.example.ibeet.DATES", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = myPreffs.edit();
        float old = myPreffs.getFloat("allTimeDistanceTravelled", 0);
        editor.putFloat("sessionDistanceTravelled", beenLocats.getTotalDistanceFloat());
        editor.putFloat("allTimeDistanceTravelled", old + beenLocats.getTotalDistanceFloat());
        editor.putFloat("thisSessionAverageSpeed", beenLocats.getAverageSpeedFloat());
        editor.commit();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 10000, 10, this);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {

                        case R.id.nav_food:
                            Intent foodActivity = new Intent(TrackerActivity.this, FoodActivity.class);
                            startActivity(foodActivity);
                            break;

                        case R.id.nav_profile:
                            Intent profileActivity = new Intent(TrackerActivity.this, ProfileActivity.class);
                            startActivity(profileActivity);
                            break;

                        case R.id.nav_map:
                            break;
                    }
                    return false;
                }
            };

    //Back press setup
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent exitApp = new Intent(Intent.ACTION_MAIN);
            exitApp.addCategory(Intent.CATEGORY_HOME);
            exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exitApp);
            return;
        } else {
            backToast = Toast.makeText(TrackerActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
