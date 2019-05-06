package com.example.ibeet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class TrackerActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    MarkerOptions mo;
    Marker currentPosMarker;
    LocationManager locationManager;
    private Button center;
    private TextView totalDist;
    private TextView avSpd;
    MarkerHandler beenLocats= new MarkerHandler();

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
        mo = new MarkerOptions().position(new LatLng(0,0)).title("My Current Location");
        requestLocation();
        totalDist = findViewById(R.id.totaldistance);
        avSpd = findViewById(R.id.averagespeed);

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
        currentPosMarker = mMap.addMarker(mo);

        // Add a marker over Metropolia and move the camera
        LatLng metropolia = new LatLng(60.258617, 24.844468);
        mMap.addMarker(new MarkerOptions().position(metropolia).title("Metropolia, here I was made!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(metropolia));

        if (beenLocats.locationsArray.size()<2){} else{
            for(int i=1; i<=beenLocats.locationsArray.size();i++){
                mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(beenLocats.locationsArray.get(i).latitude, beenLocats.locationsArray.get(i).longitude),
                        new LatLng(beenLocats.locationsArray.get(i-1).latitude, beenLocats.locationsArray.get(i-1).longitude))
                        .width(5).color(Color.GREEN));
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        final LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        //marker is the current position of the device
        //Reset current position
        currentPosMarker.setPosition(myCoordinates);

            //Leave a custom marker at recent location
            //mMap.addMarker(new MarkerOptions().position(myCoordinates).title("I have been here").icon(BitmapDescriptorFactory.fromResource(R.drawable.locationmarker)));

        //Draw line between last location and current location
        if(beenLocats.getCurrentIndex()==0){} else {
            mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(beenLocats.getLocationLat(),beenLocats.getLocationLng()), new LatLng (myCoordinates.latitude, myCoordinates.longitude))
                    .width(5).color(Color.RED));
        }
        //Call for distance calculation yeet
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
