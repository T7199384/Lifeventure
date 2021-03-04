package com.example.lifeventure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lifeventure.Dialogs.CharacterDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CharacterDialog.CharacterListener{

    public static final int PERMISSIONS_FINE_LOCATION=101;
    Button tasks;
    Button profile;
    Button fight;
    Button settings;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    private int gender;
    private int lvl;
    private int exp;

    public static final String PROFILE = null;
    public static final int PROFILE_GENDER = 2;
    public static final int PROFILE_LEVEL = 1;
    public static final int PROFILE_EXP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks= findViewById(R.id.taskButtonMain);
        profile= findViewById(R.id.profileButtonMain);
        fight = findViewById(R.id.fightButtonMain);
        settings = findViewById(R.id.settingsButtonMain);

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TaskActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FightActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        loadProfile();
        if (gender==-1){
            CharacterDialog characterDialog = new CharacterDialog();
            characterDialog.show(getSupportFragmentManager(), "Pick a character");
        }

        SharedPreferences settings=getSharedPreferences("SETTINGS",MODE_PRIVATE);

        locationRequest= new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);

        if(settings.getInt("BATTERY_POWER",0)==0){
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); }
        else{
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);}

        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSIONS_FINE_LOCATION:
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                updateGPS();
            }
            else{
                Toast.makeText(this,"App needs permissions to use GPS tracking",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateGPS(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    checkGPSLocation(location);
                }
            });
        }
        else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void checkGPSLocation(Location location) {

        //TODO Make this work in the background and constantly

        Geocoder geoCoder=new Geocoder(this);
        double locationLatitude = location.getLatitude();
        double locationLongitude = location.getLongitude();
        List<Address> taskLocation = null;
        try {
            taskLocation = geoCoder.getFromLocation(locationLatitude,locationLongitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String locationString = taskLocation.get(0).getAddressLine(0);
        SharedPreferences geocaches = getSharedPreferences("Tasks",MODE_PRIVATE);
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

    private void taskLocationComplete(int index) {
        Intent intent=new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }

    public void createProfile(int character) {
        SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(PROFILE_GENDER), character);

        editor.apply();
        Toast.makeText(this, "Profile Created", Toast.LENGTH_SHORT).show();
    }

    public void loadProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        gender = sharedPreferences.getInt(String.valueOf(PROFILE_GENDER),-1);
        lvl = sharedPreferences.getInt(String.valueOf(PROFILE_LEVEL),1);
        exp = sharedPreferences.getInt(String.valueOf(PROFILE_EXP), 0);
    }

    @Override
    public void apply(int uCharacter) {
        createProfile(uCharacter);
    }
}
