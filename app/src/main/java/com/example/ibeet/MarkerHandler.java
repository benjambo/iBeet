package com.example.ibeet;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

class MarkerHandler {
    private static final MarkerHandler ourInstance = new MarkerHandler();
    ArrayList<MarkerClass> beenLocations;

    static MarkerHandler getInstance() {
        return ourInstance;
    }

    private MarkerHandler() {
        beenLocations = new ArrayList<MarkerClass>();
    }

    public void setNewMarker(LatLng newLocat){
        beenLocations.add(new MarkerClass(newLocat, ""));
    }
}
