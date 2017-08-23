package com.example.user.timekeeper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by User on 01-08-2017.
 */

public class GPSTracker implements LocationListener {
    Context context;
    boolean isGPSenabled;
    boolean isNetworkenabled;
    public GPSTracker(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,"GPS permisson is not granted!",Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(isNetworkenabled){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return location;
        }
         if (isGPSenabled) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }

        else if(!isGPSenabled){
                Toast.makeText(context,"Enable GPS",Toast.LENGTH_SHORT);

        }
        return null;
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
}
