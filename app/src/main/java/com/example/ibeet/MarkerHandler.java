package com.example.ibeet;

import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

class MarkerHandler {
    private static final MarkerHandler ourInstance = new MarkerHandler();

    ArrayList<MarkerOptions> beenLocations;

    static MarkerHandler getInstance() {
        return ourInstance;
    }

    private MarkerHandler() {
        beenLocations = new ArrayList<MarkerOptions>();
    }

    //Add a new marker to map. give location in parameters. Marker has custom marker with title "I have been here".
    public void setNewMarker(Location location){
        LatLng newLocat = new LatLng(location.getLatitude(), location.getLongitude());

        beenLocations.add(new MarkerOptions().position(newLocat).title("I have been here").icon(BitmapDescriptorFactory.fromResource(R.drawable.locationmarker)));
    }
}
