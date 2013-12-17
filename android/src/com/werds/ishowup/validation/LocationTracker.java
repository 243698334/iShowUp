package com.werds.ishowup.validation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class LocationTracker extends Service implements LocationListener {

        private final Context mContext;
        
        private boolean networkEnabled = false;
        private boolean gpsEnabled = false;
        private boolean locationAvailable = false;
        
        private Location location;
        private double latitude;
        private double longitude;
        
        protected LocationManager locationManager;
        
        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_BETWEEN_UPDATES = 10;
        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;
        
        public LocationTracker(Context context) {
                this.mContext = context;
                getLocation(); // Get location when instance is created.
        }
        
        public double getLatitude() {
                if (location != null) {
                        latitude = location.getLatitude();
                }
                return this.latitude;
        }
        
        public double getLongitude() {
                if (location != null) {
                        longitude = location.getLongitude();
                }
                return this.longitude;
        }
        
        public boolean getLocationAvailability() {
                return this.locationAvailable;
        }
        
        public Location getLocation() {
                locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
                
                // Get service status
                networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!gpsEnabled && !networkEnabled) {
                        this.locationAvailable = false;
                        return null;
                } else this.locationAvailable = true;
                
                if (networkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_BETWEEN_UPDATES, this);
                        //Log.d("Network", "Network");
                        if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                }
                        }
                }
                // Replace location data if GPS is enabled.
                if (gpsEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_BETWEEN_UPDATES, this);
                        //Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                }
                        }
                }
                return location;
        }
        
        public void showServiceAlert() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Oops!");
                alertDialog.setMessage("GPS is not enabled. Wanna turn it on?");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                        }
                });
                alertDialog.show();
        }
        
        @Override
        public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public IBinder onBind(Intent intent) {
                // TODO Auto-generated method stub
                return null;
        }
        
}