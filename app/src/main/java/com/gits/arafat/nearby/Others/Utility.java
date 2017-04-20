package com.gits.arafat.nearby.Others;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Arafat on 29/03/2017.
 */

public class Utility {
    public static final String GOOGLE_API_KEY =  "AIzaSyA9QPYNb1QJLI-8040iJ5cjXapiQ1xZVHA";
    public static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
    public static final float LOCATION_REFRESH_DISTANCE = (float) .01;
    public static final long LOCATION_REFRESH_TIME = 1000;
    public static int RADIUS = 2500;
    public static  float ZOOM = 13;
    private static Location currentLocation = null;
    public static boolean increaseRadious(){
        if (RADIUS<5000){
            RADIUS+=500;
            updateZome();
            return true;
        }
        return false;
    }
    public static boolean decreaseRadious(){
        if (RADIUS>1000){
            RADIUS-=500;
            updateZome();
            return true;
        }
        return false;
    }
    private static void updateZome(){
        switch (RADIUS){
            case 5000: ZOOM=12;
                break;
            case 4500: ZOOM= (float) 12.2;
                break;
            case 4000: ZOOM= (float) 12.4;
                break;
            case 3500: ZOOM= (float) 12.6;
                break;
            case 3000: ZOOM=(float) 12.8;
                break;
            case 2500: ZOOM=(float) 13;
                break;
            case 2000: ZOOM=(float) 13.3;
                break;
            case 1500: ZOOM=(float) 13.7;
                break;
            case 1000: ZOOM=(float) 14.3;
                break;
        }
    }
    public static void setCurrentLocation(Location location){
        if (location!=null)
        currentLocation=location;
    }
    public static Location getCurrentLocation(){
        if (currentLocation==null){
            currentLocation = new Location("");
            currentLocation.setLatitude(23.7806286);
            currentLocation.setLongitude(90.279369);
        }
        return currentLocation;
    }
    public static LatLng getLatLng(){
        return new LatLng(getCurrentLocation().getLatitude(),getCurrentLocation().getLongitude());
    }
    private static List<HashMap<String, String>> nearbyPlacesList=null;
    public static void setNearbyPlacesList(List<HashMap<String, String>> n){
        nearbyPlacesList=n;
    }
    public static List<HashMap<String, String>> getNearbyPlacesList(){
        return nearbyPlacesList;
    }
    public static String getUrl(double latitude, double longitude, Type search) {
        // TODO: 29/03/2017
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location=" + latitude + "," + longitude +
                "&radius="+RADIUS+
                "&types="+search.getFullName() +
                "&key=AIzaSyC8biYWHCCndi47BZuFXom2C9rl4vVbTKY"+
                "&sensor=false";
        return placesSearchStr;
    }
    public static void getPermission(Activity activity,String [] permissions){
        ActivityCompat.requestPermissions(activity, permissions, 1);
    }
    public static boolean checkLocationPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    public enum Type{
        atm("atm"),
        bank("bank"),
        bar("bar"),
        bus_station("bus_station"),
        cafe("cafe"),
        dentist("dentist"),
        doctor("doctor"),
        embassy("embassy"),
        food("food"),
        gym("gym"),
        hospital("hospital"),
        library("library"),
        local_government_office("local_government_office"),
        mosque("mosque"),
        movie_theater("movie_theater"),
        museum("museum"),
        park("park"),
        pharmacy("pharmacy"),
        police("police"),
        post_office("post_office"),
        restaurant("restaurant"),
        school("school"),
        university("university"),
        zoo("zoo");

        private String fullName;
        Type(String s) {
            this.fullName = getUpperCaseWithReplace(s);
        }
        @Override public String toString(){
            return fullName;
        }

        public String getFullName() {
            return getLowerCaseWithReplace(fullName);
        }
    }
    public static String getUpperCaseWithReplace(String s){
        return s.toUpperCase().replace("_"," ");
    }
    public static String getLowerCaseWithReplace(String s){
        return s.toLowerCase().replace(" ","_");
    }
    public static void addCircle(GoogleMap mMap) {
        mMap.addCircle(new CircleOptions()
                .center(Utility.getLatLng())
                .radius(Utility.RADIUS)
                .strokeWidth((float) 1)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#80dee4ef")));
    }

    public static void moveMap(GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Utility.getLatLng()));
        zoomOnRadius(mMap);
    }
    public static void zoomOnRadius(GoogleMap mMap){
        mMap.animateCamera(CameraUpdateFactory.zoomTo(Utility.ZOOM));
    }
    public static Location getLocation(Double lat, double lon){
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }
}
