package com.captaincool.projectapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionResult_activity extends FragmentActivity implements OnMapReadyCallback,TaskLoadedCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    LocationManager manager;
    LocationListener listener;
    LatLng userLocation;
    String mode;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,200,listener);
                Log.i("mylog","here 1");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_result_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        getDirection = findViewById(R.id.btnGetDirection);
        Intent i = getIntent();
        mode = i.getStringExtra("mode");
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(DirectionResult_activity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), mode), mode);
            }
        });
        //27.658143,85.3199503
        //27.667491,85.3208583
//        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                mMap.clear();
                Toast.makeText(DirectionResult_activity.this,location.toString(),Toast.LENGTH_SHORT).show();
                userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                place1 = new MarkerOptions().position(userLocation).title("Your Location");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(Build.VERSION.SDK_INT <23)
        {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,200,listener);
            Log.i("mylog","here 2");
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else
            {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,200,listener);
                Log.i("mylog","here 3");
                String provider = manager.GPS_PROVIDER;
                Location lastKnowLocation = manager.getLastKnownLocation(provider);
                mMap.clear();
                Toast.makeText(DirectionResult_activity.this,"Last Known Location is "+lastKnowLocation.toString(),Toast.LENGTH_SHORT).show();
                userLocation = new LatLng(lastKnowLocation.getLatitude(),lastKnowLocation.getLongitude());
                place1 = new MarkerOptions().position(userLocation).title("Your Location");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,12));
            }
        }

        Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        mMap.setOnMapClickListener(this);
        mMap.addMarker(place2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom (userLocation,12));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        place2 = new MarkerOptions().position(latLng).title("Want to go");
        mMap.addMarker(place2);
        mMap.addMarker(place1);
    }
}
