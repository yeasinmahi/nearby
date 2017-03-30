package com.gits.arafat.nearby.Others;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.gits.arafat.nearby.Activity.MainActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Arafat on 29/03/2017.
 */

public class Utility {
    public static final String GOOGLE_API_KEY =  "AIzaSyA9QPYNb1QJLI-8040iJ5cjXapiQ1xZVHA";
    public static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
    public static final float LOCATION_REFRESH_DISTANCE = (float) .11;
    public static final long LOCATION_REFRESH_TIME = 1000;
    public static final long RADIUS = 5000;
    public static final long ZOOM = 15;
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
                "&radius="+RADIUS+"&sensor=true" +
                "&types="+search +
                "&key="+GOOGLE_API_KEY;
        return placesSearchStr;
    }
    public static void getPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, 1);
    }
    public static boolean checkLocationPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    public enum Type{
        airport,
        atm,
        bank,
        bar,
        bus_station,
        cafe,
        dentist,
        doctor,
        embassy,
        food,
        gym,
        hospital,
        library,
        local_government_office,
        mosque,
        movie_theater,
        museum,
        park,
        pharmacy,
        police,
        post_office,
        restaurant,
        school,
        university,
        zoo
    }
}
