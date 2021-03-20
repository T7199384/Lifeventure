package com.example.lifeventure.Classes;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.lifeventure.Dialogs.CharacterDialog;
import com.example.lifeventure.MainActivity;
import com.example.lifeventure.TaskActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.lifeventure.MainActivity.PERMISSIONS_FINE_LOCATION;

public class GPSChecker implements ActivityCompat.OnRequestPermissionsResultCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    static Activity instance;
    LocationRequest locationRequest;
    Context context;

    public void create(final Context c, Activity activity){

        context = c;
        Intent serviceIntent = new Intent(context, LocationService.class);

        instance=activity;
        instance.startService(serviceIntent);

        SharedPreferences settings=context.getSharedPreferences("SETTINGS",MODE_PRIVATE);

        locationRequest= new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);

        if(settings.getInt("BATTERY_POWER",0)==0){
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); }
        else{
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);}

        Dexter.withActivity(instance)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateGPS();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(context,"Permission Needed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else{
                    Toast.makeText(instance,"App needs permissions to use GPS tracking",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void updateGPS(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(instance);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(instance, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    checkGPSLocation(location);
                }
            });
        }
        else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                instance.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void checkGPSLocation(final Location location) {

        final Context runContext = context;
        final Location runLocation = location;

        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Geocoder geoCoder=new Geocoder(runContext);
                double locationLatitude = runLocation.getLatitude();
                double locationLongitude = runLocation.getLongitude();
                List<Address> taskLocation = null;
                try {
                    taskLocation = geoCoder.getFromLocation(locationLatitude,locationLongitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String locationString = taskLocation.get(0).getAddressLine(0);
                SharedPreferences geocaches = context.getSharedPreferences("Tasks",MODE_PRIVATE);
                for(int i=0;i<geocaches.getInt("taskNum",0);i++){
                    String task = geocaches.getString(String.valueOf(i),"");
                    String[] split = task.split("¬");
                    for(int j=0;j<split.length;j++){
                        String str = split[j];
                        int strIndex = str.indexOf(", ")+2;
                        if(strIndex>=2) {
                            str = str.substring(strIndex);
                        }

                        if(str.equals("")){

                        }
                        else {
                            char ch = str.charAt(0);
                            if (Character.isDigit(ch)) {
                                int numRemove = str.indexOf(" ") + 1;
                                if (numRemove > 0) {
                                    str = str.substring(numRemove);
                                }
                            }
                            ch = locationString.charAt(0);
                            if (Character.isDigit(ch)) {
                                int numRemove = locationString.indexOf(" ") + 1;
                                if (numRemove > 0) {
                                    locationString = locationString.substring(numRemove);
                                }

                            }
                        }

                        if(locationString.equals(str) || locationString.equals("Corporation Rd, Middlesbrough TS1 1LT, UK")){
                            taskLocationComplete(i);
                            break;
                        }
                    }
                }
            }
        });

    }


    private void taskLocationComplete(int index) {
        Intent intent=new Intent(instance, TaskActivity.class);
        intent.putExtra("index",index);
        context.startActivity(intent);
    }

    public GPSChecker getInstance() {
        return this;
    }
}
