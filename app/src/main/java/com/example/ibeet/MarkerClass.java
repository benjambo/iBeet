package com.example.ibeet;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerClass {
    private String viesti;
    private LatLng location;

    public MarkerClass(LatLng location, String viesti){
        this.location = location;
        this.viesti = viesti;
    }

    public String getViesti() {
        return viesti;
    }

    public LatLng getLocation() {
        return location;
    }
}
