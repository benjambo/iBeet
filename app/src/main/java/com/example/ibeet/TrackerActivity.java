package com.example.ibeet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class TrackerActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private int markerCount = 0;
    MarkerOptions mo;
    Marker marker;
    LocationManager locationManager;
    private Button center;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(0,0)).title("My Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.locationmarker));
        requestLocation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        marker = mMap.addMarker(mo);

        // Add a marker over Metropolia and move the camera
        LatLng metropolia = new LatLng(60.258617, 24.844468);
        mMap.addMarker(new MarkerOptions().position(metropolia).title("Metropolia, here I was made!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(metropolia));
    }

    @Override
    public void onLocationChanged(Location location) {

        final LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        marker.setPosition(myCoordinates);
        //MarkerHandler.getInstance().setNewMarker(myCoordinates);
        mMap.addMarker(new MarkerOptions().position(myCoordinates).title("olin tässä aijemmin"));


        //Button to center view on the current location
        center = findViewById(R.id.buttoncenter);
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomlvl = 16.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, zoomlvl));
            }
        });


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

}
