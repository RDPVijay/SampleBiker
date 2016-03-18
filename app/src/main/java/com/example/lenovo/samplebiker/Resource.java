package com.example.lenovo.samplebiker;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Lenovo on 3/9/2016.
 */
public class Resource extends Service implements LocationListener {

    private Context context;

    boolean isGpsEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    public Location location;
    public static double lat, lon;

    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = (float) 10.0;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5;

    protected LocationManager locationManager;

    public Resource (Context c){

        this.context = c;
        getLocation();
        Log.v("Resource","Constructor");

    }

    private Location getLocation() {

        try {
            Log.v("Getlocation","try");
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled){
                Log.v("Getlocation", "if");

            }
            else {
                this.canGetLocation = false;
                if (isNetworkEnabled){

                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        return location;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network","Network");
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){

                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Log.v("SourceNET", ""+lat);

                        }
                    }

                }

                if (isGpsEnabled) {
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPSEnabled", "GPSEnabled");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){

                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.v("SourceGPS", ""+lat);

                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return location;

    }

    public double getLatitude(){
        if (location != null)
            lat = location.getLatitude();
        return lat;
    }

    public double getLongitude(){
        if (location != null)
            lon = location.getLongitude();
        return lon;
    }

    public boolean canGetLocation(){
        return  this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
