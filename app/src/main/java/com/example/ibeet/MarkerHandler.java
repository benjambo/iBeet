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
    private Double Lat;
    private Double Lng;

    int currentIndex;

    public MarkerHandler() {
        beenLocations = new ArrayList<>();
        locationsArray = new ArrayList<>();
        totalDistanceTravelled = 0.0f;
        averageSpeed = 0.0f;
        currentIndex = 0;
        Lng = 0d;
        Lat = 0d;
    }

    //Add a new marker to map. give location in parameters. Marker has custom marker with title "I have been here".
    public void setNewMarker(Location location){
        //Set location to be stored as LatLng (newLocat) to ass marker
        LatLng newLocat = new LatLng(location.getLatitude(), location.getLongitude());
        Lat = location.getLatitude();
        Lng = location.getLongitude();
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
        return "Distance travelled: " + String.valueOf(String.format("%.2f",totalDistanceTravelled)) + " m";
    }

    public Float getTotalDistanceFloat(){
        return totalDistanceTravelled;
    }

    public String getAverageSpeed(){
        return "Average speed: " + String.valueOf(String.format("%.2f",averageSpeed)) + " m/s";
    }

    public Float getAverageSpeedFloat(){
        return averageSpeed;
    }

    public LatLng getLocationLatLng(int Index){
        return locationsArray.get(Index);
    }

    public Double getLocationLat(){
        return Lat;
    }

    public Double getLocationLng(){
        return Lng;
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

}
