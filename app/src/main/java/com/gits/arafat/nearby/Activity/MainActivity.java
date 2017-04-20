package com.gits.arafat.nearby.Activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gits.arafat.nearby.Api.GetNearbyPlacesData;
import com.gits.arafat.nearby.Manifest;
import com.gits.arafat.nearby.Others.Utility;
import com.gits.arafat.nearby.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private Spinner spinner;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationManager mLocationManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        init();
        Utility.getPermission(this,new String[]{
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET});
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
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Utility.LOCATION_REFRESH_TIME, Utility.LOCATION_REFRESH_DISTANCE, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setCurrentLocation();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                setCurrentLocation();
                search();
                Utility.moveMap(mMap);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(context,"disabling gps may caused your location inaccurate",Toast.LENGTH_SHORT).show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void init(){
        spinner= (Spinner) findViewById(R.id.searchSpinner);

        populateSpinner();
    }
    private void populateSpinner(){
        spinner.setAdapter(new ArrayAdapter<Utility.Type>(this, android.R.layout.simple_spinner_dropdown_item,  Utility.Type.values()));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng dhaka = new LatLng(23.7806286, 90.279369);
        mMap.addMarker(new MarkerOptions().position(dhaka).title("Marker in Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));

        if (Utility.checkLocationPermission(context)) return;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        setCurrentLocation();
        search();
        Utility.moveMap(mMap);

    }
    private void setCurrentLocation() {
        if (Utility.checkLocationPermission(context)) return;
        Utility.setCurrentLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
    }


    private void getNearbyResult(double latitude, double longitude, Utility.Type search){
        String url = Utility.getUrl(latitude, longitude, search);
        Object[] DataTransfer = new Object[2];
        if (mMap==null) return;
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
    }
    public void onClickPlusButton(View view){
        if (mMap==null) return;
        if(Utility.increaseRadious()){
            search();
            Utility.zoomOnRadius(mMap);
        }
    }
    public void onClickMinusButton(View view){
        if (mMap==null) return;
        if(Utility.decreaseRadious()){
            search();
            Utility.zoomOnRadius(mMap);
        }
    }
    private void search(){
        if (Utility.getCurrentLocation() != null) {
            getNearbyResult(Utility.getCurrentLocation().getLatitude(),Utility.getCurrentLocation().getLongitude(), Utility.Type.valueOf(Utility.getLowerCaseWithReplace(spinner.getSelectedItem().toString())));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setCurrentLocation();
        search();
        Utility.moveMap(mMap);
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
