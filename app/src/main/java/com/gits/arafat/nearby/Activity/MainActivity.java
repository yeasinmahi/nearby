package com.gits.arafat.nearby.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gits.arafat.nearby.Api.GetNearbyPlacesData;
import com.gits.arafat.nearby.Others.Utility;
import com.gits.arafat.nearby.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private LocationManager mLocationManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        Utility.getPermission(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Utility.checkLocationPermission(context)) return;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Utility.LOCATION_REFRESH_TIME, Utility.LOCATION_REFRESH_DISTANCE, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getCurrentLocation();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                getCurrentLocation();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(context,"disabling gps may caused your location inaccurate",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng dhaka = new LatLng(-23.7806286, 90.279369);
        mMap.addMarker(new MarkerOptions().position(dhaka).title("Marker in Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));

        if (Utility.checkLocationPermission(context)) return;
        mMap.setMyLocationEnabled(true);

    }
    private void getCurrentLocation() {
        mMap.clear();
        if (Utility.checkLocationPermission(context)) return;
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (currentLocation != null) {
            //Getting longitude and latitude
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .radius(Utility.RADIUS)
                    .strokeWidth((float) 1)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#80dee4ef")));
            //moving the map to location
            moveMap();
            getNearbyResult(currentLocation.getLatitude(),currentLocation.getLongitude(), Utility.Type.atm);
        }
    }
    private void moveMap() {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(Utility.ZOOM));
        //mMap.getUiSettings().setZoomControlsEnabled(true);

    }
    private void getNearbyResult(double latitude, double longitude, Utility.Type search){
        String url = Utility.getUrl(latitude, longitude, search);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context,"Google Map api connection suspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context,"Google Map api connection refused",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
