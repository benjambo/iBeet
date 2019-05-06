package com.example.ibeet;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

class MarkerHandler {
    ArrayList<Location> beenLocations;
    ArrayList<LatLng> locationsArray;
    private Float lastDistanceDiff;
    private Float totalDistanceTravelled;
    private Float averageSpeed;

    int currentIndex;

    public MarkerHandler() {
        beenLocations = new ArrayList<>();
        locationsArray = new ArrayList<>();
        totalDistanceTravelled = 0.0f;
        averageSpeed = 0.0f;
        currentIndex = 0;
    }

    //Add a new marker to map. give location in parameters. Marker has custom marker with title "I have been here".
    public void setNewMarker(Location location){
        //Set location to be stored as LatLng (newLocat) to ass marker
        LatLng newLocat = new LatLng(location.getLatitude(), location.getLongitude());
        //Store location as LatLng for tracker line drawing
        locationsArray.add(newLocat);
        //Store location as Location for distance calculations
        beenLocations.add(location);
        //Distance calculation
        if (currentIndex ==0){ } else {
            lastDistanceDiff = location.distanceTo(beenLocations.get(currentIndex-1));
            totalDistanceTravelled += lastDistanceDiff;
        }
        averageSpeed = totalDistanceTravelled / (currentIndex*10);
        currentIndex++;
    }

    public String getTotalDistance(){
        return "Distance travelled today: " + String.valueOf(totalDistanceTravelled) + " m";
    }

    public String getAverageSpeed(){
        return "Average speed today: " + String.valueOf(averageSpeed) + " m/s";
    }

    public LatLng getLocationLatLng(int Index){
        return locationsArray.get(Index);
    }

    public int getCurrentIndex(){
        return currentIndex;
    }
}
